package com.camping.erp.domain.site;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    
    @Query("select z from Zone z left join fetch z.sites")
    List<Zone> findAllWithSites();
}
