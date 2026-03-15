package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.SiteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

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

    @Transactional
    public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request) {
        return null;
    }

    @Transactional
    public void cancel(Long id) {
    }
}
