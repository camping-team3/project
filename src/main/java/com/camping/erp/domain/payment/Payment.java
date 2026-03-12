package com.camping.erp.domain.payment;

import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    private String impUid;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String status;

    private LocalDateTime payDate;

    @Builder
    public Payment(Long id, Reservation reservation, String impUid, Long amount, String status, LocalDateTime payDate) {
        this.id = id;
        this.reservation = reservation;
        this.impUid = impUid;
        this.amount = amount;
        this.status = status;
        this.payDate = payDate;
    }
}
