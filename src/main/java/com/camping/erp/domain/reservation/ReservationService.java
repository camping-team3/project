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
        LocalDate checkIn = searchDTO.getCheckIn() != null ? searchDTO.getCheckIn() : LocalDate.now();
        LocalDate checkOut = searchDTO.getCheckOut() != null ? searchDTO.getCheckOut() : LocalDate.now().plusDays(1);

        // 유효한 체크인-아웃 기간인지 검증 (최소 1박)
        if (!checkOut.isAfter(checkIn)) {
            checkOut = checkIn.plusDays(1);
        }

        List<ReservationStatus> activeStatuses = List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.CANCEL_REQ);

        List<Site> availableSites = reservationRepository.findAvailableSites(
                checkIn, checkOut, activeStatuses, searchDTO.getZoneId(), searchDTO.getPeopleCount());

        long days = ChronoUnit.DAYS.between(checkIn, checkOut);

        return availableSites.stream()
                .map(s -> SiteResponse.ListDTO.builder()
                        .id(s.getId())
                        .siteName(s.getSiteName())
                        .zoneName(s.getZone().getName())
                        .maxPeople(s.getMaxPeople())
                        .price(s.getZone().getNormalPrice() * days)
                        .isAvailable(true)
                        .build())
                .toList();
    }

    @Transactional
    public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request) {
        // 직접 구현하세요.
        return null;
    }

    @Transactional
    public void cancel(Long id) {
        // 직접 구현하세요.
    }
}
