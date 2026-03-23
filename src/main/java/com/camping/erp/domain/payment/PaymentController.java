package com.camping.erp.domain.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 결제 관련 API 및 웹훅 수신 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 포트원 웹훅 수신 엔드포인트
     * 결제 성공(paid) 시 서버 측 검증 및 확정 로직을 실행합니다.
     */
    @PostMapping("/webhook")
    public void receiveWebhook(@RequestBody Map<String, String> payload) {
        String impUid = payload.get("imp_uid");
        String merchantUid = payload.get("merchant_uid");
        String status = payload.get("status");

        log.info("[Webhook] 신호 수신 - status: {}, merchantUid: {}", status, merchantUid);

        // 결제 성공(paid) 상태인 경우에만 비즈니스 로직 수행
        if ("paid".equals(status)) {
            paymentService.processWebhook(impUid, merchantUid);
        } else {
            log.info("[Webhook] 처리 제외 대상 상태: {}", status);
        }
    }
}
