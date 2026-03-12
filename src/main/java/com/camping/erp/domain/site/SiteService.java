package com.camping.erp.domain.site;

import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.handler.ex.Exception404;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SiteService {

    private final SiteRepository siteRepository;
    private final ZoneRepository zoneRepository;

    public List<SiteResponse.MainDTO> getAvailableZonesWithSites(LocalDate checkIn, LocalDate checkOut) {
        List<Site> availableSites;
        long nights = 1;

        if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
            availableSites = siteRepository.findAllWithZone();
        } else {
            availableSites = siteRepository.findAvailableSites(checkIn, checkOut);
            nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        }

        final long finalNights = nights;

        // 구역별로 그룹화하여 DTO 생성 (DTO 내부 생성자 활용)
        return zoneRepository.findAll().stream()
                .map(zone -> new SiteResponse.MainDTO(zone, availableSites, finalNights))
                .collect(Collectors.toList());
    }

    public List<Site> findAllWithZone() {
        return siteRepository.findAllWithZone();
    }

    public Site findById(Long id) {
        return siteRepository.findById(id)
                .orElseThrow(() -> new Exception400("해당 사이트를 찾을 수 없습니다. id: " + id));
    }

    @Transactional
    public void save(SiteRequest.SiteSaveDTO requestDTO) {
        Zone zone = zoneRepository.findById(requestDTO.getZoneId())
                .orElseThrow(() -> new Exception404("해당 구역을 찾을 수 없습니다. id: " + requestDTO.getZoneId()));
        siteRepository.save(requestDTO.toEntity(zone));
    }

    @Transactional
    public void update(Long id, SiteRequest.SiteUpdateDTO requestDTO) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 사이트를 찾을 수 없습니다. id: " + id));
        Zone zone = zoneRepository.findById(requestDTO.getZoneId())
                .orElseThrow(() -> new Exception404("해당 구역을 찾을 수 없습니다. id: " + requestDTO.getZoneId()));
        site.update(zone, requestDTO.getSiteName(), requestDTO.getMaxPeople());
    }

    @Transactional
    public void delete(Long id) {
        siteRepository.deleteById(id);
    }
}
