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
        List<ReservationStatus> statuses = List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED,
                ReservationStatus.CANCEL_REQ);
        List<Site> sites = siteRepository.findAvailableSites(checkIn, checkOut, statuses);
        return sites.stream().map(SiteResponse.ListDTO::new).toList();
    }

    public SiteResponse.DetailDTO findById(Long id) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 사이트를 찾을 수 없습니다."));
        return new SiteResponse.DetailDTO(site);
    }

    @Transactional
    public void saveZone(SiteRequest.ZoneSaveDTO requestDTO) {
        zoneRepository.save(requestDTO.toEntity());
    }

    @Transactional
    public void updateZone(Long id, SiteRequest.ZoneSaveDTO requestDTO) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 구역을 찾을 수 없습니다."));
        zone.update(requestDTO.getName(), requestDTO.getNormalPrice(), requestDTO.getPeakPrice(), requestDTO.getBasePeople(), requestDTO.getExtraPersonFee());
    }

    @Transactional
    public void deleteZone(Long id) {
        zoneRepository.deleteById(id);
    }

    @Transactional
    public void saveSite(SiteRequest.SiteSaveDTO requestDTO) {
        Zone zone = zoneRepository.findById(requestDTO.getZoneId())
                .orElseThrow(() -> new RuntimeException("해당 구역을 찾을 수 없습니다."));
        siteRepository.save(requestDTO.toEntity(zone));
    }

    @Transactional
    public void updateSite(Long id, SiteRequest.SiteSaveDTO requestDTO) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 사이트를 찾을 수 없습니다."));
        Zone zone = zoneRepository.findById(requestDTO.getZoneId())
                .orElseThrow(() -> new RuntimeException("해당 구역을 찾을 수 없습니다."));
        site.update(requestDTO.getSiteName(), requestDTO.getMaxPeople(), zone, requestDTO.isAvailable());
    }

    @Transactional
    public void deleteSite(Long id) {
        siteRepository.deleteById(id);
    }
}
