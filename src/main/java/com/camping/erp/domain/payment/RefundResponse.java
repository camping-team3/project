package com.camping.erp.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 환불 관련 응답 DTO
 */
public class RefundResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefundInfo {
        private Long reservationId;
        private long totalPrice;      // 원결제 금액
        private long refundAmount;     // 환불 예정 금액
        private int refundPercent;     // 환불 비율 (%)
        private int daysUntilCheckIn; // 체크인까지 남은 일수
        private boolean canRefund;     // 자동 환불 가능 여부
    }
}
