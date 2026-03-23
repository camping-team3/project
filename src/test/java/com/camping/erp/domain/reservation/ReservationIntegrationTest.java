package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.SiteRepository;
import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.user.User;
import com.camping.erp.domain.user.UserRepository;
import com.camping.erp.domain.user.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        // 테스트용 사용자 (ssar, id=2) 조회 및 DTO 변환
        User user = userRepository.findById(2L).orElseThrow();
        // LoginDTO 생성자에 User 엔티티를 전달하여 생성 (기존 빌더 오류 수정)
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
        request.setPeopleCount(4); // 기준 2인 + 추가 2인 (A구역 추가 인당 10,000원)

        // when: 변경된 규격(LoginDTO)에 따라 호출
        ReservationResponse.ReserveDTO response = reservationService.reserve(request, testUser);

        // then
        assertThat(response.getId()).isNotNull();
        // 가격 검증: (A구역 평시 50,000 + 추가인원 2명 * 10,000) * 2박 = 140,000
        assertThat(response.getTotalPrice()).isEqualTo(140000L);

        Reservation saved = reservationRepository.findById(response.getId()).orElseThrow();
        assertThat(saved.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    @DisplayName("시나리오 3: 변경/취소 요청 중인 사이트의 중복 예약 차단 검증")
    void findAvailableSites_BlockingRequestedSites() {
        // ... (기존 코드 유지)
    }

    @Test
    @DisplayName("시나리오 4: 과거 날짜 예약 변경 요청 차단 검증")
    void requestChange_PastDate_Fail() {
        // given: 과거 날짜 예약 생성 (상태: CONFIRMED)
        Reservation pastReservation = Reservation.builder()
                .user(userRepository.findById(testUser.getId()).orElseThrow())
                .site(testSite)
                .checkIn(LocalDate.now().minusDays(5))
                .checkOut(LocalDate.now().minusDays(3))
                .peopleCount(2)
                .totalPrice(100000L)
                .status(ReservationStatus.CONFIRMED)
                .build();
        Reservation saved = reservationRepository.save(pastReservation);

        ReservationRequest.ChangeDTO changeDTO = new ReservationRequest.ChangeDTO();
        changeDTO.setReservationId(saved.getId());
        changeDTO.setNewCheckIn(LocalDate.now().plusDays(10));
        changeDTO.setNewCheckOut(LocalDate.now().plusDays(12));
        changeDTO.setNewSiteId(testSite.getId());

        // when & then: Exception400 발생 확인
        org.junit.jupiter.api.Assertions.assertThrows(com.camping.erp.global.handler.ex.Exception400.class, () -> {
            reservationService.requestChange(changeDTO, testUser);
        });
    }

    @Test
    @DisplayName("시나리오 5: 이용일 3일 이내 예약 취소 요청 차단 검증")
    void requestCancel_Within3Days_Fail() {
        // given: 이용일이 내일인 예약 생성 (상태: CONFIRMED)
        Reservation nearReservation = Reservation.builder()
                .user(userRepository.findById(testUser.getId()).orElseThrow())
                .site(testSite)
                .checkIn(LocalDate.now().plusDays(1))
                .checkOut(LocalDate.now().plusDays(3))
                .peopleCount(2)
                .totalPrice(100000L)
                .status(ReservationStatus.CONFIRMED)
                .build();
        Reservation saved = reservationRepository.save(nearReservation);

        ReservationRequest.CancelDTO cancelDTO = new ReservationRequest.CancelDTO();
        cancelDTO.setReservationId(saved.getId());
        cancelDTO.setReason("개인 사정");

        // when & then: Exception400 발생 확인
        org.junit.jupiter.api.Assertions.assertThrows(com.camping.erp.global.handler.ex.Exception400.class, () -> {
            reservationService.requestCancel(cancelDTO, testUser);
        });
    }
}
