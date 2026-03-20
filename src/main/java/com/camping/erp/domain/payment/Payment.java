package com.camping.erp.domain.payment;

import com.camping.erp.domain.payment.enums.PaymentStatus;
import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "payment_tb")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String impUid; // 포트원 결제 고유 번호

    @Column(nullable = false, unique = true)
    private String merchantUid; // 주문 번호 (예: ORD-20260320-001)

    private String payMethod; // 결제 수단 (CARD, EASY_PAY 등)

    @Column(nullable = false)
    private Long amount; // 결제 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // PAID, CANCELLED

    private LocalDateTime paidAt; // 결제 완료 시각

    private String cancelReason; // 취소 사유

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation; // 1:1 연관관계 (결제 정보의 주인)

    // --- 비즈니스 로직 ---

    /**
     * 결제 취소 상태로 변경
     */
    public void cancel(String reason) {
        this.status = PaymentStatus.CANCELLED;
        this.cancelReason = reason;
    }
}
