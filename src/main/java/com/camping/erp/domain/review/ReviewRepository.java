package com.camping.erp.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    boolean existsByReservationId(Long reservationId);

    Optional<Review> findByReservationId(Long reservationId);
}
