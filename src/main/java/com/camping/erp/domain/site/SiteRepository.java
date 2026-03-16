package com.camping.erp.domain.site;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {
    
    @Query("select s from Site s join fetch s.zone where s.zone.id = :zoneId")
    List<Site> findAllByZoneId(@Param("zoneId") Long zoneId);

    @Query("select s from Site s join fetch s.zone")
    List<Site> findAllWithZone();

    @Query("SELECT s FROM Site s JOIN FETCH s.zone " +
           "WHERE s.id NOT IN (" +
           "  SELECT r.site.id FROM Reservation r " +
           "  WHERE r.status IN :statuses " +
           "  AND r.checkIn < :checkOut AND r.checkOut > :checkIn" +
           ")")
    List<Site> findAvailableSites(@Param("checkIn") LocalDate checkIn, 
                                  @Param("checkOut") LocalDate checkOut, 
                                  @Param("statuses") List<ReservationStatus> statuses);
}
