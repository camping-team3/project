package com.camping.erp.domain.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("mock") // mock 프로필이 활성화될 때만 동작하도록 변경
public class MockPortOneService implements PortOneService {

    @Override
    public PortOneResponse.PaymentDetail getPaymentDetail(String impUid) {
        log.info("[Mock] 포트원 결제 단건 조회 요청: impUid={}", impUid);
        
        // 가짜 데이터 반환
        return PortOneResponse.PaymentDetail.builder()
                .impUid(impUid)
                .merchantUid("RSV-1-UUID")
                .amount(100000L)
                .status("paid")
                .payMethod("card")
                .reservationId(1L)
                .build();
    }

    @Override
    public boolean cancelPayment(String impUid, Long amount, String reason) {
        log.info("[Mock] 포트원 결제 취소 요청 발송: impUid={}, 환불금액={}, 사유={}", impUid, amount, reason);
        return true; // 무조건 성공 반환
    }
}
