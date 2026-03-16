package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.SiteRepository;
import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.user.User;
import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.handler.ex.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SiteRepository siteRepository;

    public List<SiteResponse.ListDTO> findAvailableSites(ReservationRequest.SearchDTO searchDTO) {
        LocalDate checkIn = searchDTO.getCheckIn();
        LocalDate checkOut = searchDTO.getCheckOut();

        List<ReservationStatus> activeStatuses = List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.CANCEL_REQ);

        List<Site> availableSites = reservationRepository.findAvailableSites(
                checkIn, checkOut, activeStatuses, searchDTO.getZoneId(), searchDTO.getPeopleCount());

        return availableSites.stream()
                .map(s -> SiteResponse.ListDTO.builder()
                        .id(s.getId())
                        .siteName(s.getSiteName())
                        .zoneName(s.getZone().getName())
                        .basePeople(2) // 모든 사이트 기준 2인 고정
                        .maxPeople(s.getMaxPeople())
                        .pricePerNight(s.getZone().getNormalPrice()) // 1박 요금 고정
                        .isAvailable(true)
                        .build())
                .toList();
    }

    // 결제 폼 데이터 준비
    public ReservationResponse.PaymentFormDTO getPaymentForm(ReservationRequest.ReserveDTO request) {
        Site site = siteRepository.findById(request.getSiteId())
                .orElseThrow(() -> new Exception404("해당 사이트를 찾을 수 없습니다."));

        long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
        int basePeople = 2; // 비즈니스 룰: 기준 인원 2명
        int extraPeople = Math.max(0, request.getPeopleCount() - basePeople);
        
        long pricePerNight = site.getZone().getNormalPrice();
        long extraPriceTotal = (long) extraPeople * 10000 * nights;
        long totalPrice = (pricePerNight + (extraPeople * 10000)) * nights;

        return ReservationResponse.PaymentFormDTO.builder()
                .siteId(site.getId())
                .siteName(site.getSiteName())
                .zoneName(site.getZone().getName())
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .nights(nights)
                .peopleCount(request.getPeopleCount())
                .basePeople(basePeople)
                .pricePerNight(pricePerNight)
                .extraPrice(extraPriceTotal)
                .totalPrice(totalPrice)
                .build();
    }

    // 예약 실행 (최종 검증 및 저장)
    @Transactional
    public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request, User sessionUser) {
        // 1. 사이트 존재 확인
        Site site = siteRepository.findById(request.getSiteId())
                .orElseThrow(() -> new Exception404("해당 사이트를 찾을 수 없습니다."));

        // 2. 최종 중복 체크
        List<ReservationStatus> activeStatuses = List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED);
        boolean isExist = reservationRepository.existsBySiteIdAndDateRange(
                site.getId(), request.getCheckIn(), request.getCheckOut(), activeStatuses);
        
        if (isExist) {
            throw new Exception400("이미 예약된 기간입니다.");
        }

        // 3. 서버 측 가격 재계산 및 검증 (보안)
        long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
        int extraPeople = Math.max(0, request.getPeopleCount() - 2);
        long calculatedPrice = (site.getZone().getNormalPrice() + ((long) extraPeople * 10000)) * nights;

        // 4. 엔티티 생성 및 저장
        Reservation reservation = Reservation.builder()
                .user(sessionUser)
                .site(site)
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .peopleCount(request.getPeopleCount())
                .totalPrice(calculatedPrice)
                .status(ReservationStatus.PENDING)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        return ReservationResponse.ReserveDTO.builder()
                .id(saved.getId())
                .siteName(site.getSiteName())
                .checkIn(saved.getCheckIn())
                .checkOut(saved.getCheckOut())
                .totalPrice(saved.getTotalPrice())
                .build();
    }

    @Transactional
    public void cancel(Long id) {
        // 취소 로직은 추후 구현
    }
}
