package com.camping.erp.domain.site;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SiteService {

    private final SiteRepository siteRepository;

    public List<SiteResponse.ListDTO> findAll() {
        // 직접 구현하세요.
        return null;
    }

    public SiteResponse.DetailDTO findById(Long id) {
        // 직접 구현하세요.
        return null;
    }
}
