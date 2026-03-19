package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.SiteRepository;
import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.user.User;
import com.camping.erp.domain.user.UserRepository;
import com.camping.erp.domain.user.UserResponse;
import com.camping.erp.global.handler.ex.Exception400;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ReservationIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private UserRepository userRepository;

    private UserResponse.LoginDTO testUser;
    private Site testSite;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 (ssar)
        User user = userRepository.findById(2L).orElseThrow();
        testUser = new UserResponse.LoginDTO(user);
        // 테스트용 사이트 (A-1, id=1)
        testSite = siteRepository.findById(1L).orElseThrow();
    }

    @Test
    @DisplayName("시나리오 1: 가용 사이트 검색 테스트")
    void findAvailableSites_Success() {
        // given: 2026-03-20 ~ 2026-03-22 기간 검색 (현재 data.sql에 예약 없는 기간)
        ReservationRequest.SearchDTO searchDTO = new ReservationRequest.SearchDTO();
        searchDTO.setCheckIn(LocalDate.of(2026, 3, 20));
        searchDTO.setCheckOut(LocalDate.of(2026, 3, 22));
        searchDTO.setPeopleCount(2);

        // when
        List<SiteResponse.ResevationAvailableListDTO> sites = reservationService.findAvailableSites(searchDTO);

        // then
        assertThat(sites).isNotEmpty();
        assertThat(sites.stream().anyMatch(s -> s.getId().equals(testSite.getId()))).isTrue();
    }

    @Test
    @DisplayName("시나리오 2: 예약 생성 및 가격 재계산 검증")
    void reserve_Success() {
        // given
        ReservationRequest.ReserveDTO request = new ReservationRequest.ReserveDTO();
        request.setSiteId(testSite.getId());
        request.setCheckIn(LocalDate.of(2026, 3, 25));
        request.setCheckOut(LocalDate.of(2026, 3, 27)); // 2박
        request.setPeopleCount(4); // 기준 2인 + 추가 2인 (20,000원 추가)

        // when
        ReservationResponse.ReserveDTO response = reservationService.reserve(request, testUser);

        // then
        assertThat(response.getId()).isNotNull();
        // 가격 검증: (A구역 평시 50,000 + 추가인원 20,000) * 2박 = 140,000
        assertThat(response.getTotalPrice()).isEqualTo(140000L);

        Reservation saved = reservationRepository.findById(response.getId()).orElseThrow();
        assertThat(saved.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    @DisplayName("시나리오 3: 동일 기간 중복 예약 시도 시 실패(Exception400)")
    void reserve_Duplicate_Fail() {
        // given: 이미 예약된 기간 (2026-03-25 ~ 2026-03-27)
        ReservationRequest.ReserveDTO request1 = new ReservationRequest.ReserveDTO();
        request1.setSiteId(testSite.getId());
        request1.setCheckIn(LocalDate.of(2026, 3, 25));
        request1.setCheckOut(LocalDate.of(2026, 3, 27));
        request1.setPeopleCount(2);
        reservationService.reserve(request1, testUser);

        // when & then: 겹치는 기간으로 다시 예약 시도
        ReservationRequest.ReserveDTO request2 = new ReservationRequest.ReserveDTO();
        request2.setSiteId(testSite.getId());
        request2.setCheckIn(LocalDate.of(2026, 3, 26)); // 중간에 겹침
        request2.setCheckOut(LocalDate.of(2026, 3, 28));
        request2.setPeopleCount(2);

        assertThatThrownBy(() -> reservationService.reserve(request2, testUser))
                .isInstanceOf(Exception400.class)
                .hasMessageContaining("이미 예약된 기간입니다.");
    }
}
