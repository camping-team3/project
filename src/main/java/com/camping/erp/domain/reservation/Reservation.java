package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.user.User;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reservation_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(nullable = false)
    private Integer peopleCount;  // 예약 인원

    private String visitorName;   // 방문자 성함
    private String visitorPhone;  // 방문자 연락처

    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // PENDING, CONFIRMED, CANCEL_REQ, CANCEL_COMP, COMPLETED

    @Builder
    public Reservation(Long id, User user, Site site, LocalDate checkIn, LocalDate checkOut, Long totalPrice, Integer peopleCount, String visitorName, String visitorPhone, ReservationStatus status) {
        this.id = id;
        this.user = user;
        this.site = site;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.peopleCount = peopleCount;
        this.visitorName = visitorName;
        this.visitorPhone = visitorPhone;
        this.status = status;
    }
}
