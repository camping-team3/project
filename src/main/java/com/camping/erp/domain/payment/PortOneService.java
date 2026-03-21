package com.camping.erp.domain.payment;

/**
 * 포트원 서버와 실제 통신을 담당하는 서비스 인터페이스
 */
public interface PortOneService {
    
    /**
     * 포트원 결제 단건 조회
     * @param impUid 결제 고유 번호
     * @return 상세 결제 정보 (금액, 상태 등)
     */
    PortOneResponse.PaymentDetail getPaymentDetail(String impUid);

    /**
     * 포트원 결제 취소 요청 (환불)
     * @param impUid 취소할 결제 고유 번호
     * @param reason 취소 사유
     * @return 취소 성공 여부
     */
    boolean cancelPayment(String impUid, String reason);
}
