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

        // 프롬프트를 훨씬 엄격하고 구체적으로 수정
        String prompt = "너는 캠핑장 커뮤니티의 악성 리뷰 감별사야. 다음 리뷰 내용을 분석해서 위험 점수를 1~5 사이의 숫자로만 답해.\\n" +
                "점수 기준:\\n" +
                "- 5점: 비속어, 욕설, 사장님에 대한 공격, '돈 아깝다', '다시는 안 온다', '망해라' 등의 명백한 서비스 비방 및 부정적 감정\\n" +
                "- 4점: 반어법(비꼬기), 불친절에 대한 구체적 불만, 타인에게 방문하지 말라고 권고하는 내용\\n" +
                "- 3점: 시설 결함에 대한 반복적 불만, 불쾌한 경험 토로\\n" +
                "- 1점: 순수한 칭찬, 건전한 피드백, 감사 인사\\n\\n" +
                "내용에 부정적인 감정(실망, 분노, 비난)이 조금이라도 담겨 있다면 절대 1점을 주지 마. 무조건 숫자 하나만 응답해.\\n" +
                "리뷰 내용: " + content;

        Map<String, Object> body = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of("text", prompt))
            ))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List candidates = (List) response.getBody().get("candidates");
                Map candidate = (Map) candidates.get(0);
                Map contentMap = (Map) candidate.get("content");
                List parts = (List) contentMap.get("parts");
                String textResponse = ((Map) parts.get(0)).get("text").toString().trim();
                
                // [디버깅 로그] AI의 실제 답변을 서버 콘솔에 출력
                log.info("[AI Raw Response] Review Content: '{}' -> AI Decision: '{}'", content, textResponse);
                
                String scoreStr = textResponse.replaceAll("[^1-5]", "");
                return scoreStr.isEmpty() ? 3 : Integer.parseInt(scoreStr.substring(0, 1));
            }
        } catch (Exception e) {
            log.error("[AI Analysis] API Call Error: {}", e.getMessage());
            return 3; // 에러 발생 시 '경계(3점)'로 표시하여 관리자가 보게 함
        }
        return 1;
    }

    private void updateReviewScore(Long reviewId, Integer score) {
        log.info("[AI Analysis] Updating score for review ID: {} to {}", reviewId, score);
        reviewRepository.findById(reviewId).ifPresent(review -> {
            review.updateAiScore(score);
            // 비동기 스레드이므로 즉시 DB에 반영되도록 flush 호출
            reviewRepository.saveAndFlush(review);
            log.info("[AI Analysis] Successfully updated DB for review ID: {}", reviewId);
        });
    }
}
