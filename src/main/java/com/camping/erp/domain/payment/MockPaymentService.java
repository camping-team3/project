package com.camping.erp.domain.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Profile("!prod") // 운영 환경이 아닐 때 활성화
public class MockPaymentService implements PaymentService {

    @Override
    public Map<String, Object> verifyPayment(String paymentId) {
        log.info("[Mock] 결제 검증 요청 수신: {}", paymentId);
        
        // 가짜 데이터 반환
        Map<String, Object> mockData = new HashMap<>();
        mockData.put("status", "PAID");
        mockData.put("amount", 50000L); // 실제 결제 금액 검증 시 활용
        mockData.put("paymentId", paymentId);
        
        return mockData;
    }

    @Override
    public boolean cancelPayment(String paymentId, Long amount, String reason) {
        log.info("[Mock] 결제 취소 요청 수신: id={}, amount={}, reason={}", paymentId, amount, reason);
        return true; // 무조건 성공 반환
    }
}
