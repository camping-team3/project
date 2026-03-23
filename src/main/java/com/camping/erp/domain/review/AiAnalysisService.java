package com.camping.erp.domain.review;

import com.camping.erp.global.util.ProfanityFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiAnalysisService {

    private final ReviewRepository reviewRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.key:}") // 키가 없을 경우를 대비해 기본값 비워둠
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    @Async
    @Transactional
    public void analyzeReviewAsync(Long reviewId, String content) {
        log.info("[AI Analysis] Starting analysis for review ID: {}", reviewId);

        // 1. 서버 측 비속어 필터 (즉시 5점)
        if (ProfanityFilter.containsProfanity(content)) {
            log.info("[AI Analysis] Profanity detected in review ID: {}. Setting score to 5.", reviewId);
            updateReviewScore(reviewId, 5);
            return;
        }

        // 2. Gemini API 키 확인
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("[AI Analysis] Gemini API Key is missing. Skipping AI analysis for review ID: {}", reviewId);
            updateReviewScore(reviewId, 1); // 기본 점수 부여
            return;
        }

        // 3. Gemini API를 통한 문맥 분석
        try {
            Integer score = callGeminiApi(content);
            log.info("[AI Analysis] Completed for review ID: {}. Score: {}", reviewId, score);
            updateReviewScore(reviewId, score);
        } catch (Exception e) {
            log.error("[AI Analysis] Failed for review ID: {}. Error: {}", reviewId, e.getMessage());
            updateReviewScore(reviewId, 1);
        }
    }

    private Integer callGeminiApi(String content) {
        String url = GEMINI_API_URL + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = "다음 리뷰 텍스트의 비방/욕설/공격성 수치를 1(매우 낮음)에서 5(매우 높음) 사이의 숫자로만 응답해줘. " +
                "비속어가 조금이라도 섞여 있다면 반드시 5점이야. " +
                "응답은 숫자 하나만 보내야 해. 텍스트: " + content;

        Map<String, Object> body = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of("text", prompt))
            ))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                List candidates = (List) response.getBody().get("candidates");
                Map candidate = (Map) candidates.get(0);
                Map contentMap = (Map) candidate.get("content");
                List parts = (List) contentMap.get("parts");
                String textResponse = ((Map) parts.get(0)).get("text").toString().trim();
                
                // 숫자만 추출 (1-5 사이)
                String scoreStr = textResponse.replaceAll("[^1-5]", "");
                return scoreStr.isEmpty() ? 1 : Integer.parseInt(scoreStr.substring(0, 1));
            } catch (Exception e) {
                log.error("[AI Analysis] Parsing error: {}", e.getMessage());
                return 1;
            }
        }
        return 1;
    }

    private void updateReviewScore(Long reviewId, Integer score) {
        reviewRepository.findById(reviewId).ifPresent(review -> {
            review.updateAiScore(score);
            // JPA 변경 감지에 의해 자동 저장되지만 명시적으로 호출 가능
            reviewRepository.save(review);
        });
    }
}
