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

        // 예약 가능한 사이트 목록 조회 (가예약 Lock 로직 포함)
        @Query("SELECT s FROM Site s JOIN FETCH s.zone z WHERE " +
                        "(:zoneId IS NULL OR z.id = :zoneId) AND " +
                        "(:peopleCount IS NULL OR s.maxPeople >= :peopleCount) AND " +
                        "s.id NOT IN (" +
                        "  SELECT r.site.id FROM Reservation r " +
                        "  WHERE (r.checkIn < :checkOut AND r.checkOut > :checkIn) " +
                        "  AND r.status IN :statuses" +
                        ") AND " +
                        "s.id NOT IN (" +
                        "  SELECT cr.newSite.id FROM ReservationChangeRequest cr " +
                        "  WHERE (cr.newCheckIn < :checkOut AND cr.newCheckOut > :checkIn) " +
                        "  AND cr.status = 'PENDING'" + // 승인 대기 중인 변경 요청 건들
                        ")")
        List<Site> findAvailableSites(@Param("checkIn") LocalDate checkIn,
                        @Param("checkOut") LocalDate checkOut,
                        @Param("statuses") List<ReservationStatus> statuses,
                        @Param("zoneId") Long zoneId,
                        @Param("peopleCount") Integer peopleCount);

        // 중복 예약 여부 체크 (가예약 Lock 로직 포함)
        @Query("SELECT (COUNT(r) > 0 OR COUNT(cr) > 0) FROM Site s " +
                        "LEFT JOIN Reservation r ON r.site.id = s.id AND r.status IN :statuses AND (r.checkIn < :checkOut AND r.checkOut > :checkIn) "
                        +
                        "LEFT JOIN ReservationChangeRequest cr ON cr.newSite.id = s.id AND cr.status = 'PENDING' AND (cr.newCheckIn < :checkOut AND cr.newCheckOut > :checkIn) "
                        +
                        "WHERE s.id = :siteId")
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

    /**
     * 특정 사용자의 모든 예약 내역을 최신순(생성일 기준)으로 조회
     */
    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 특정 상태이면서 체크아웃 날짜가 기준 날짜 이전인 예약 목록 조회
     */
    List<Reservation> findByStatusAndCheckOutBefore(ReservationStatus status, LocalDate date);

    /**
     * 특정 시각 이전에 생성된 특정 상태의 예약 목록 조회 (선점 만료 체크용)
     */
    java.util.List<Reservation> findByStatusAndCreatedAtBefore(ReservationStatus status, java.time.LocalDateTime threshold);

    /**
     * [추가] 특정 상태의 예약 건수 조회 (대시보드 통계용)
     */
    long countByStatus(ReservationStatus status);

    /**
     * [추가] 여러 상태에 해당하는 예약 목록 조회 (페이징)
     */
    @Query("SELECT r FROM Reservation r JOIN FETCH r.user JOIN FETCH r.site s JOIN FETCH s.zone " +
            "WHERE r.status IN :statuses")
    Page<Reservation> findByStatuses(@Param("statuses") List<ReservationStatus> statuses, Pageable pageable);

    /**
     * [추가] 특정 유저의 특정 상태들에 해당하는 예약 건수 조회
     */
    long countByUserIdAndStatusIn(Long userId, List<ReservationStatus> statuses);

    /**
     * [추가] 특정 유저의 특정 상태들에 해당하거나, 최근 일정 기간 내에 생성된 예약 목록 조회
     */
    @Query("SELECT r FROM Reservation r JOIN FETCH r.site s JOIN FETCH s.zone " +
            "WHERE r.user.id = :userId " +
            "AND (r.status IN :activeStatuses OR (r.status IN :pastStatuses AND r.createdAt >= :since)) " +
            "ORDER BY r.createdAt DESC")
    List<Reservation> findRecentMypageReservations(@Param("userId") Long userId,
                                                   @Param("activeStatuses") List<ReservationStatus> activeStatuses,
                                                   @Param("pastStatuses") List<ReservationStatus> pastStatuses,
                                                   @Param("since") java.time.LocalDateTime since);
}
