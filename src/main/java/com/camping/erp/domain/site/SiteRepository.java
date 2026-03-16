package com.camping.erp.domain.site;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {
    
    @Query("select s from Site s join fetch s.zone where s.zone.id = :zoneId")
    List<Site> findAllByZoneId(@Param("zoneId") Long zoneId);
}
