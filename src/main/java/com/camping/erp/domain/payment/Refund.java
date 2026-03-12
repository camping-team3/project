package com.camping.erp.domain.payment;

import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.global._core.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refund_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refund extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private Long refundAmount;

    @Column(nullable = false)
    private java.time.LocalDateTime cancelledAt;

    @Builder
    public Refund(Long id, Reservation reservation, String reason, Long refundAmount, java.time.LocalDateTime cancelledAt) {
        this.id = id;
        this.reservation = reservation;
        this.reason = reason;
        this.refundAmount = refundAmount;
        this.cancelledAt = cancelledAt;
    }
}
