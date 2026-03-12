<!-- Parent: ../AI-CONTEXT.md -->

# global/config/

## 목적
Spring MVC 및 서드파티 라이브러리의 Bean 설정 클래스 모음.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `WebMvcConfig.java` | `LoginInterceptor`, `AdminInterceptor` 등록 및 보호 경로 패턴 설정 |

## 인터셉터 경로 설정 (WebMvcConfig)
| 인터셉터 | 보호 경로 | 예외 경로 |
|----------|----------|----------|
| `LoginInterceptor` | `/mypage/**`, `/reservation/**`, `/admin/**`, `/boards/**`, `/replies/**` | `/boards/[0-9]+` (게시글 상세 비로그인 허용) |
| `AdminInterceptor` | `/admin/**` | 없음 |

## AI 작업 지침
- **인터셉터 추가**: 새 인터셉터 구현 후 이 파일의 `addInterceptors()`에 경로 패턴과 함께 등록
- **경로 우선순위**: `LoginInterceptor` → `AdminInterceptor` 순서로 체인이 적용됨
- **경로 확장**: 새 기능 경로(예: `/qna/**`)에 인증이 필요하면 `LoginInterceptor`의 `addPathPatterns()`에 추가

## 의존성
- 내부: `global/_core/auth/LoginInterceptor`, `global/_core/auth/AdminInterceptor`
- 외부: Spring WebMvc (`WebMvcConfigurer`, `InterceptorRegistry`)
