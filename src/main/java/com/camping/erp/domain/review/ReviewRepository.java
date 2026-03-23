package com.camping.erp.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r " +
           "join fetch r.user " +
           "join fetch r.reservation res " +
           "join fetch res.site s " +
           "join fetch s.zone z " +
           "order by r.createdAt desc")
    Page<Review> findAllWithDetails(Pageable pageable);

    @Query("select avg(r.rating) from Review r")
    Double findAverageRating();

    @Query("SELECT r FROM Review r WHERE r.reservation.site.id = :siteId AND r.isDeleted = false")
    List<Review> findActiveReviewsBySiteId(Long siteId);

    @Query("SELECT r FROM Review r WHERE r.reservation.site.zone.id = :zoneId AND r.isDeleted = false")
    List<Review> findActiveReviewsByZoneId(Long zoneId);

    boolean existsByReservationId(Long reservationId);

    Optional<Review> findByReservationId(Long reservationId);

    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
}
