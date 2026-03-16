<!-- Parent: ../AI-CONTEXT.md -->

# global/_core/handler/

## 목적
전역 예외 처리. 요청 유형(AJAX vs 일반 폼)을 자동 판별하여
JSON 응답 또는 JavaScript `alert + redirect` Script를 반환한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `GlobalExceptionHandler.java` | `@RestControllerAdvice`. 모든 커스텀 예외를 포착하여 AJAX/Script 분기 응답 |
| `ex/Exception400.java` | 잘못된 요청 (400 Bad Request) — 유효성 검증 실패, 잘못된 입력 |
| `ex/Exception401.java` | 미인증 (401 Unauthorized) → `/login-form`으로 이동 |
| `ex/Exception403.java` | 권한 부족 (403 Forbidden) → `history.back()` |
| `ex/Exception404.java` | 리소스 없음 (404 Not Found) → `history.back()` |
| `ex/Exception500.java` | 서버 내부 오류 (500 Internal Server Error) → `history.back()` |

## AJAX 판별 로직
```java
// X-Requested-With: XMLHttpRequest 헤더 OR Accept: application/json 헤더
private boolean isAjaxRequest(HttpServletRequest request)
```
> AJAX 요청은 `Resp.fail(status, msg)` JSON 반환
> 일반 요청은 `<script>alert('msg'); history.back();</script>` Script 반환

## 예외 사용 패턴
```java
// Service 또는 Controller에서
throw new Exception400("입력값이 올바르지 않습니다");
throw new Exception401("로그인이 필요합니다");
throw new Exception403("접근 권한이 없습니다");
throw new Exception404("존재하지 않는 리소스입니다");
throw new Exception500("서버 오류가 발생했습니다");
```

## AI 작업 지침
- **새 예외 유형 추가**: `ex/` 하위에 클래스 생성 후 `GlobalExceptionHandler`에 `@ExceptionHandler` 메서드 추가
- **401 특수 처리**: 미인증만 `history.back()` 대신 `/login-form`으로 이동 — 다른 예외와 혼동 금지
- **예외 선택 기준**: 비즈니스 로직 실패 → 400, 인증 없음 → 401, 권한 없음 → 403, 데이터 없음 → 404, 기타 → 500

## 의존성
- 내부: `global/_core/util/Resp`
- 외부: Spring WebMvc (`@RestControllerAdvice`, `@ExceptionHandler`), Jakarta Servlet API
