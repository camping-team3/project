package com.camping.erp.domain.payment;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

/**
 * 포트원 API 응답을 매핑하기 위한 DTO
 */
public class PortOneResponse {

    @Getter
    @Builder
    public static class PaymentDetail {
        private String impUid;      // 포트원 결제 고유 번호
        private String merchantUid; // 주문 번호 (예: RSV-123-UUID)
        private Long amount;        // 결제 금액
        private String status;      // 결제 상태 (paid, cancelled 등)
        private String payMethod;   // 결제 수단 (card 등)
        
        // 조건부 복구를 위해 customData에 담아 보낼 정보들
        private Long reservationId;
        private Long userId;
        private Long siteId;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Integer peopleCount;
    }
}
