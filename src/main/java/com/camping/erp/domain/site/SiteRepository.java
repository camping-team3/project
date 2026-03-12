package com.camping.erp.domain.site;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {
    
    @Query("select s from Site s join fetch s.zone where s.zone.id = :zoneId")
    List<Site> findAllByZoneId(@Param("zoneId") Long zoneId);

    @Query("select s from Site s join fetch s.zone order by s.zone.id asc, s.siteName asc")
    List<Site> findAllWithZone();

    @Query("SELECT s FROM Site s JOIN FETCH s.zone WHERE s.id NOT IN (" +
            "SELECT r.site.id FROM Reservation r " +
            "WHERE r.status != com.camping.erp.domain.reservation.Reservation.ReservationStatus.CANCEL_COMP " +
            "AND r.checkIn < :checkOut AND r.checkOut > :checkIn" +
            ") ORDER BY s.zone.id ASC, s.siteName ASC")
    List<Site> findAvailableSites(@Param("checkIn") java.time.LocalDate checkIn, @Param("checkOut") java.time.LocalDate checkOut);
}
