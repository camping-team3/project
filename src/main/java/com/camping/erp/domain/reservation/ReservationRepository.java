package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.site.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

        // 예약 가능한 사이트 목록 조회
        @Query("SELECT s FROM Site s JOIN FETCH s.zone z WHERE " +
                        "(:zoneId IS NULL OR z.id = :zoneId) AND " +
                        "(:peopleCount IS NULL OR s.maxPeople >= :peopleCount) AND " +
                        "s.id NOT IN (" +
                        "SELECT r.site.id FROM Reservation r " +
                        "WHERE (r.checkIn < :checkOut AND r.checkOut > :checkIn) " +
                        "AND r.status IN :statuses" +
                        ")")
        List<Site> findAvailableSites(@Param("checkIn") LocalDate checkIn,
                        @Param("checkOut") LocalDate checkOut,
                        @Param("statuses") List<ReservationStatus> statuses,
                        @Param("zoneId") Long zoneId,
                        @Param("peopleCount") Integer peopleCount);

        @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
                        "WHERE r.site.id = :siteId " +
                        "AND (r.checkIn < :checkOut AND r.checkOut > :checkIn) " +
                        "AND r.status IN :statuses")
        boolean existsBySiteIdAndDateRange(@Param("siteId") Long siteId,
                        @Param("checkIn") LocalDate checkIn,
                        @Param("checkOut") LocalDate checkOut,
                        @Param("statuses") List<ReservationStatus> statuses);

        @Query(value = "SELECT r FROM Reservation r JOIN FETCH r.user JOIN FETCH r.site s JOIN FETCH s.zone " +
                        "WHERE (:keyword IS NULL OR r.user.name LIKE %:keyword% OR CAST(r.id AS string) LIKE %:keyword%) "
                        +
                        "AND (:checkIn IS NULL OR r.checkIn = :checkIn) " +
                        "AND (:status IS NULL OR r.status = :status)", countQuery = "SELECT COUNT(r) FROM Reservation r "
                                        +
                                        "WHERE (:keyword IS NULL OR r.user.name LIKE %:keyword% OR CAST(r.id AS string) LIKE %:keyword%) "
                                        +
                                        "AND (:checkIn IS NULL OR r.checkIn = :checkIn) " +
                                        "AND (:status IS NULL OR r.status = :status)")
        Page<Reservation> findAllAdminSearch(@Param("keyword") String keyword,
                        @Param("checkIn") LocalDate checkIn,
                        @Param("status") ReservationStatus status,
                        Pageable pageable);
}
