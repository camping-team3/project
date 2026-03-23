package com.camping.erp.domain.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 포트원 V2 API와 실제 통신을 담당하는 서비스 구현체
 */
@Slf4j
@Service
@Primary // Mock 서비스 대신 우선적으로 주입되도록 설정
@RequiredArgsConstructor
public class PortOneServiceImpl implements PortOneService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${portone.api-url:https://api.portone.io}")
    private String apiUrl;

    @Value("${portone.api-secret}")
    private String apiSecret;

    /**
     * 포트원 결제 단건 조회 (V2)
     * @param paymentId 결제 고유 번호 (V1의 impUid에 해당)
     */
    @Override
    public PortOneResponse.PaymentDetail getPaymentDetail(String paymentId) {
        log.info("[PortOne] 결제 단건 조회 요청: paymentId={}", paymentId);

        String url = String.format("%s/payments/%s", apiUrl, paymentId);
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createHeaderEntity(),
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> data = response.getBody();
                
                // V2 API 응답 구조에 맞게 파싱 (기본적인 필드 위주)
                // 실제 응답에는 amount, status, customData 등이 포함됨
                Map<String, Object> amountObj = (Map<String, Object>) data.get("amount");
                Long amount = amountObj != null ? ((Number) amountObj.get("total")).longValue() : 0L;
                String status = (String) data.get("status");
                
                // customData 파싱 (결제 요청 시 보냈던 예약 정보)
                Map<String, Object> customData = (Map<String, Object>) data.get("customData");
                
                return PortOneResponse.PaymentDetail.builder()
                        .impUid(paymentId)
                        .merchantUid(paymentId) // V2에서는 paymentId가 주문번호 역할을 함
                        .amount(amount)
                        .status(status != null ? status.toLowerCase() : "unknown")
                        .payMethod((String) data.get("method"))
                        .reservationId(customData != null && customData.get("reservationId") != null ? ((Number) customData.get("reservationId")).longValue() : null)
                        .build();
            }
        } catch (Exception e) {
            log.error("[PortOne] 결제 조회 실패: {}", e.getMessage());
        }

        return null;
    }

    /**
     * 포트원 결제 취소 요청 (환불 - V2)
     */
    @Override
    public boolean cancelPayment(String paymentId, Long amount, String reason) {
        log.info("[PortOne] 결제 취소 요청: paymentId={}, amount={}, reason={}", paymentId, amount, reason);

        String url = String.format("%s/payments/%s/cancel", apiUrl, paymentId);
        
        Map<String, Object> body = new HashMap<>();
        body.put("reason", reason);
        if (amount != null) {
            body.put("amount", amount);
        }

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, createHeaders());
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("[PortOne] 결제 취소 성공: paymentId={}", paymentId);
                return true;
            }
        } catch (Exception e) {
            log.error("[PortOne] 결제 취소 실패: {}", e.getMessage());
        }

        return false;
    }

    /**
     * 공통 인증 헤더 생성 (PortOne V2 방식)
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "PortOne " + apiSecret);
        return headers;
    }

    private HttpEntity<String> createHeaderEntity() {
        return new HttpEntity<>(createHeaders());
    }
}
