package com.camping.erp.domain.payment;

import java.util.Map;

/**
 * 결제 도메인의 핵심 인터페이스
 * 실제 연동(PortOne)과 테스트용(Mock) 구현체를 교체 가능하도록 설계
 */
public interface PaymentService {
    
    /**
     * 결제 내역 단건 조회 및 검증
     * @param paymentId 포트원 결제 고유 ID
     * @return 결제 정보 (성공 시 금액 등 포함)
     */
    Map<String, Object> verifyPayment(String paymentId);

    /**
     * 결제 취소 (환불)
     * @param paymentId 취소할 결제 ID
     * @param amount 취소할 금액 (부분 환불 대응용)
     * @param reason 취소 사유
     * @return 취소 결과 성공 여부
     */
    boolean cancelPayment(String paymentId, Long amount, String reason);
}
