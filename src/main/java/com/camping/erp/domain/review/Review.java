package com.camping.erp.domain.review;

import com.camping.erp.domain.image.Image;
import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.domain.user.User;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Review(Long id, User user, Reservation reservation, Integer rating, String content) {
        this.id = id;
        this.user = user;
        this.reservation = reservation;
        this.rating = rating;
        this.content = content;
    }
}
