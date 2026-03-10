<!-- Parent: ../../../../../.ai/AI-CONTEXT.md -->

# global/

## 목적
애플리케이션 전반에서 사용되는 공통 기술 요소, 설정, 필터, 유효성 검사기 및 유틸리티.

## 주요 패키지
| 패키지 | 설명 |
|--------|------|
| `_core/` | 핵심 기반 기술 (인증 인지, 필터, 핸들러, 유틸) |
| `config/` | Spring Boot 및 서드파티 라이브러리 설정 |

## AI 작업 지침
- **예외 처리**: `GlobalExceptionHandler`와 커스텀 `Exception4xx/5xx`를 사용해 일관된 에러 응답(`Resp`)을 반환한다.
- **인증/인가**: `LoginInterceptor`를 통해 세션 기반 인증을 관리하며, 경로 기반 권한 제어를 수행한다.
- **응답 규격**: 모든 API 응답은 `Resp<T>` 정적 팩토리 메서드를 통해 생성한다.

## 테스트
- 공통 기능에 대해 `Unit Test`를 수행한다.

## 의존성
- 외부: Spring WebMvc, Servlet API, Jackson
