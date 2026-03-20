package com.camping.erp.domain.payment;

import com.camping.erp.global.config.PortOneProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@Profile("prod") // 운영 환경에서만 활성화
@RequiredArgsConstructor
public class PortOneService implements PaymentService {

    private final PortOneProperties properties;

    @Override
    public Map<String, Object> verifyPayment(String paymentId) {
        log.info("[PortOne] 실제 API 호출 예정: {}", paymentId);
        // TODO: WebClient 또는 RestTemplate으로 포트원 V2 API 호출 구현 예정
        return null;
    }

    @Override
    public boolean cancelPayment(String paymentId, Long amount, String reason) {
        log.info("[PortOne] 실제 API 취소 호출 예정: {}", paymentId);
        return false;
    }
}
