package com.camping.erp.domain.site;

import com.camping.erp.global._core.handler.ex.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ZoneService {
    private final ZoneRepository zoneRepository;

    public List<Zone> findAll() {
        return zoneRepository.findAll();
    }

    public Zone findById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 구역을 찾을 수 없습니다. id: " + id));
    }

    @Transactional
    public void save(SiteRequest.ZoneSaveDTO requestDTO) {
        zoneRepository.save(requestDTO.toEntity());
    }

    @Transactional
    public void update(Long id, SiteRequest.ZoneUpdateDTO requestDTO) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 구역을 찾을 수 없습니다. id: " + id));
        zone.update(requestDTO.getName(), requestDTO.getNormalPrice(), requestDTO.getPeakPrice());
    }

    @Transactional
    public void togglePeak(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 구역을 찾을 수 없습니다. id: " + id));
        zone.togglePeak();
    }

    @Transactional
    public void delete(Long id) {
        zoneRepository.deleteById(id);
    }
}
