package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.SiteResponse;
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

    public List<SiteResponse.ListDTO> findAvailableSites(ReservationRequest.SearchDTO searchDTO) {
        // 이미 컨트롤러에서 날짜와 인원 기본값이 채워져 넘어옴을 보장받음
        LocalDate checkIn = searchDTO.getCheckIn();
        LocalDate checkOut = searchDTO.getCheckOut();
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        List<ReservationStatus> activeStatuses = List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.CANCEL_REQ);

        List<Site> availableSites = reservationRepository.findAvailableSites(
                checkIn, checkOut, activeStatuses, searchDTO.getZoneId(), searchDTO.getPeopleCount());

        return availableSites.stream()
                .map(s -> SiteResponse.ListDTO.builder()
                        .id(s.getId())
                        .siteName(s.getSiteName())
                        .zoneName(s.getZone().getName())
                        .maxPeople(s.getMaxPeople())
                        .pricePerNight(s.getZone().getNormalPrice())
                        .totalPrice(s.getZone().getNormalPrice() * nights)
                        .isAvailable(true)
                        .build())
                .toList();
    }

    @Transactional
    public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request) {
        return null;
    }

    @Transactional
    public void cancel(Long id) {
    }
}
