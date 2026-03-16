<!-- Parent: ../AI-CONTEXT.md -->

# global/_core/

## 목적
애플리케이션의 공통 인프라 로직 및 기반 기술 계층.

## 주요 패키지
| 패키지 | 설명 |
|--------|------|
| `auth/` | `LoginInterceptor` 등 세션 기반 인증 처리 |
| `handler/` | `GlobalExceptionHandler`를 통한 전역 예외 처리 |
| `util/` | `Resp<T>`를 활용한 공통 응답 규격 |
| `filter/` | Servlet Filter 기반 보안 또는 로깅 (필요시) |

## AI 작업 지침
- **응답 일관성**: AJAX 응답은 반드시 `Resp.ok()` 또는 `Resp.fail()`을 사용하여 JSON 규격을 맞춘다.
- **예외 처리**: `Exception4xx/5xx`를 던지면 `GlobalExceptionHandler`가 이를 가로채서 적절한 응답(Script 또는 JSON)을 반환한다.

## 테스트
- `GlobalExceptionHandler`가 각 예외에 대해 올바른 HTTP 상태 코드를 반환하는지 테스트한다.
