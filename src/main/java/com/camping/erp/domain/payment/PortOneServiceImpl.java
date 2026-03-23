package com.camping.erp.domain.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 포트원 V2 API와 실제 통신을 담당하는 서비스 구현체
 */
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class PortOneServiceImpl implements PortOneService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${portone.api-url:https://api.portone.io}")
    private String apiUrl;

    @Value("${portone.api-secret}")
    private String apiSecret;

    /**
     * 포트원 결제 단건 조회 (V2)
     */
    @Override
    public PortOneResponse.PaymentDetail getPaymentDetail(String paymentId) {
        log.info("[PortOne] 결제 조회 요청 시작: paymentId={}", paymentId);
        String url = String.format("%s/payments/%s", apiUrl, paymentId);
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createHeaderEntity(),
                    Map.class
            );

            log.info("[PortOne] 응답 코드: {}", response.getStatusCode());
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> data = response.getBody();
                log.info("[PortOne] 조회 성공 - 데이터: {}", data);
                
                // 1. 금액 정보 파싱 (amount.total 또는 amount.paid)
                Map<String, Object> amountObj = (Map<String, Object>) data.get("amount");
                Long amount = 0L;
                if (amountObj != null) {
                    Object total = amountObj.get("total");
                    if (total == null) total = amountObj.get("paid");
                    if (total != null) amount = ((Number) total).longValue();
                }

                // 2. 결제 상태 및 수단
                String status = (String) data.get("status");
                String method = "unknown";
                Map<String, Object> methodObj = (Map<String, Object>) data.get("method");
                if (methodObj != null) {
                    method = (String) methodObj.get("type");
                }
                
                // 3. customData 파싱 (JSON 문자열 또는 Map 대응)
                Object customDataObject = data.get("customData");
                Map<String, Object> customDataMap = null;

                if (customDataObject instanceof Map) {
                    customDataMap = (Map<String, Object>) customDataObject;
                } else if (customDataObject instanceof String) {
                    try {
                        customDataMap = objectMapper.readValue((String) customDataObject, Map.class);
                    } catch (Exception e) {
                        log.warn("[PortOne] customData JSON 파싱 실패: {}", e.getMessage());
                    }
                }

                // 4. 예약 ID 추출 (customData 내부에 저장된 정보)
                Long reservationId = null;
                if (customDataMap != null && customDataMap.get("reservationId") != null) {
                    reservationId = ((Number) customDataMap.get("reservationId")).longValue();
                }
                
                return PortOneResponse.PaymentDetail.builder()
                        .impUid(paymentId)
                        .merchantUid(paymentId) 
                        .amount(amount)
                        .status(status != null ? status.toLowerCase() : "unknown")
                        .payMethod(method)
                        .reservationId(reservationId)
                        .userId(customDataMap != null && customDataMap.get("userId") != null ? Long.parseLong(customDataMap.get("userId").toString()) : null)
                        .siteId(customDataMap != null && customDataMap.get("siteId") != null ? Long.parseLong(customDataMap.get("siteId").toString()) : null)
                        .checkIn(customDataMap != null && customDataMap.get("checkIn") != null ? java.time.LocalDate.parse(customDataMap.get("checkIn").toString()) : null)
                        .checkOut(customDataMap != null && customDataMap.get("checkOut") != null ? java.time.LocalDate.parse(customDataMap.get("checkOut").toString()) : null)
                        .peopleCount(customDataMap != null && customDataMap.get("peopleCount") != null ? Integer.parseInt(customDataMap.get("peopleCount").toString()) : null)
                        .build();
            }
        } catch (HttpClientErrorException e) {
            log.error("[PortOne] API 호출 오류 - 상태: {}, 메시지: {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("[PortOne] 결제 조회 중 예외 발생: {}", e.getMessage(), e);
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
        } catch (HttpClientErrorException e) {
            log.error("[PortOne] 취소 실패 - 상태: {}, 메시지: {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("[PortOne] 결제 취소 중 예외 발생: {}", e.getMessage(), e);
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
