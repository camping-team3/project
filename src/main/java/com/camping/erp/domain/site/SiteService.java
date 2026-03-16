package com.camping.erp.domain.site;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SiteService {

    private final SiteRepository siteRepository;
    private final ZoneRepository zoneRepository;

    public List<SiteResponse.ListDTO> findAll() {
        List<Site> sites = siteRepository.findAllWithZone();
        return sites.stream().map(SiteResponse.ListDTO::new).toList();
    }

    public List<SiteResponse.AdminZoneDTO> findAllForAdmin() {
        List<Zone> zones = zoneRepository.findAllWithSites();
        return zones.stream().map(SiteResponse.AdminZoneDTO::new).toList();
    }

    public List<SiteResponse.ListDTO> findAvailableSites(LocalDate checkIn, LocalDate checkOut) {
        List<ReservationStatus> statuses = List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.CANCEL_REQ);
        List<Site> sites = siteRepository.findAvailableSites(checkIn, checkOut, statuses);
        return sites.stream().map(SiteResponse.ListDTO::new).toList();
    }

    public SiteResponse.DetailDTO findById(Long id) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 사이트를 찾을 수 없습니다."));
        return new SiteResponse.DetailDTO(site);
    }
}
