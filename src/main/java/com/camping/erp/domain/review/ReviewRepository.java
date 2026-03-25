package com.camping.erp.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r " +
           "join fetch r.user " +
           "join fetch r.reservation res " +
           "join fetch res.site s " +
           "join fetch s.zone z " +
           "where r.isDeleted = false " +
           "order by r.createdAt desc")
    Page<Review> findAllWithDetails(Pageable pageable);

    @Query("select avg(r.rating) from Review r where r.isDeleted = false")
    Double findAverageRating();

    @Query("SELECT r FROM Review r WHERE r.reservation.site.id = :siteId AND r.isDeleted = false")
    List<Review> findActiveReviewsBySiteId(@Param("siteId") Long siteId);

    @Query("SELECT r FROM Review r WHERE r.reservation.site.zone.id = :zoneId AND r.isDeleted = false")
    List<Review> findActiveReviewsByZoneId(@Param("zoneId") Long zoneId);

    boolean existsByReservationId(Long reservationId);

    Optional<Review> findByReservationId(Long reservationId);

    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * [공통] 삭제되지 않은 실제 서비스 중인 리뷰의 총 개수를 조회합니다.
     * 사용자 페이지와 관리자 페이지의 총 개수를 일치시키기 위해 사용합니다.
     */
    long countByIsDeletedFalse();

    /**
     * [관리자용] 리뷰 목록 조회 (필터링 및 검색 포함)
     * @param isPending true일 경우 '검토 대기 중(isReviewed=false)'인 리뷰만 조회합니다.
     * @param keyword 작성자 이름 또는 리뷰 내용에 포함된 검색어입니다.
     */
    @Query("SELECT r FROM Review r " +
           "JOIN FETCH r.user " +
           "JOIN FETCH r.reservation res " +
           "JOIN FETCH res.site s " +
           "JOIN FETCH s.zone z " +
           "WHERE (:isPending = false OR r.isReviewed = false) " + // 필터가 'pending'이면 검토 안 된 것만 가져옴
           "AND (r.user.username LIKE %:keyword% OR r.content LIKE %:keyword%) " + // 검색어 조건
           "AND r.isDeleted = false") // 삭제되지 않은 리뷰만 대상
    Page<Review> findAllForAdmin(@Param("isPending") boolean isPending, 
                                 @Param("keyword") String keyword, 
                                 Pageable pageable);
}
