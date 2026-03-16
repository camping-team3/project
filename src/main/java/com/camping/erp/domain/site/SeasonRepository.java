package com.camping.erp.domain.site;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    
    @Query("SELECT COUNT(s) > 0 FROM Season s WHERE (s.startDate <= :endDate AND s.endDate >= :startDate)")
    boolean existsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
