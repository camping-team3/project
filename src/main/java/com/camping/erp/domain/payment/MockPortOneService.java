package com.camping.erp.domain.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@Profile("!prod") // 운영 환경이 아닐 때 활성화
public class MockPortOneService implements PortOneService {

    @Override
    public PortOneResponse.PaymentDetail getPaymentDetail(String impUid) {
        log.info("[Mock] 포트원 결제 단건 조회 요청: impUid={}", impUid);
        
        // 가짜 데이터 반환 (테스트 시나리오에 따라 수정 가능)
        return PortOneResponse.PaymentDetail.builder()
                .impUid(impUid)
                .merchantUid("RSV-1-UUID") // reservationId가 1인 경우 가정
                .amount(100000L) // 기본 결제 금액 가정
                .status("paid")
                .payMethod("card")
                .reservationId(1L)
                .build();
    }

    @Override
    public boolean cancelPayment(String impUid, String reason) {
        log.info("[Mock] 포트원 결제 취소 요청 발송: impUid={}, 사유={}", impUid, reason);
        return true; // 무조건 성공 반환
    }
}
