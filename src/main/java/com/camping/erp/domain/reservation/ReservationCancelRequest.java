package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.RequestStatus;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

/**
 * 예약 취소 요청 엔티티
 * 고객의 취소 사유와 환불 관련 정보를 관리자 승인 전까지 보관합니다.
 */
@Entity
@Getter
@Table(name = "reservation_cancel_request")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class ReservationCancelRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    // 고객의 취소 사유
    @Column(nullable = false, length = 1000)
    private String reason;

    // 환불 관련 정보
    private Long refundAmount; // 최종 환불 예정 금액

    private String refundBank; // 환불 은행

    private String refundAccount; // 환불 계좌번호

    private String refundAccountHolder; // 예금주명

    // 요청 처리 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    // 관리자 거절 사유
    private String rejectionReason;

    // --- 비즈니스 로직 (캡슐화) ---

    /**
     * 관리자 승인 처리
     */
    public void approve() {
        this.status = RequestStatus.APPROVED;
        this.rejectionReason = null; // 승인 시 기존 거절 사유 초기화
    }

    /**
     * 관리자 거절 처리
     * @param reason 거절 사유
     */
    public void reject(String reason) {
        this.status = RequestStatus.REJECTED;
        this.rejectionReason = reason;
    }
}
