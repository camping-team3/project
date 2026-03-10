# 🏕️ 캠핑장 예약 시스템 (ERP)

## 1. 프로젝트 마일스톤
- [ ] **Phase 1: 인프라 및 도메인 모델 설계** (25%) - 🟢 진행 중
- [ ] **Phase 2: 회원 및 인증 시스템** (0%)
- [ ] **Phase 3: 예약 및 결제 핵심 로직** (0%)
- [ ] **Phase 4: 관리자 대시보드 및 콘텐츠 관리** (0%)
- [ ] **Phase 5: 안정화 및 최종 검수** (0%)

## 2. 프로젝트 맵
| 경로 | 설명 |
|------|------|
| `.ai/` | AI 지침 및 컨텍스트 관리 |
| `.person/` | 기획서(PRD), 기술스택 등 핵심 설계 문서 |
| `src/main/java/.../domain/` | 비즈니스 로직 및 엔티티 |
| `src/main/java/.../domain/user/` | 회원 관리 도메인 (User 엔티티 구현 완료) |
| `src/main/java/.../domain/reservation/` | 예약 관리 도메인 (`AI-CONTEXT.md` 포함) |
| `src/main/java/.../domain/site/` | 사이트 관리 도메인 (`AI-CONTEXT.md` 포함) |
| `src/main/java/.../domain/payment/` | 결제 관리 도메인 (`AI-CONTEXT.md` 포함) |
| `src/main/java/.../domain/board/` | 게시판 관리 도메인 (`AI-CONTEXT.md` 포함) |
| `src/main/java/.../global/` | 전역 설정, 예외 처리, 공통 유틸리티 |
| `src/main/java/.../global/_core/` | 핵심 기반 기술 (인터셉터, 전역 예외 처리 고도화) |
| `src/main/resources/templates/` | Mustache 기반 SSR 뷰 템플릿 (`AI-CONTEXT.md` 포함) |
| `src/main/resources/db/` | 테스트 데이터 및 스키마 관련 |

## 3. 핵심 기술 스택
- **Backend**: Java 21, Spring Boot 3.x, JPA
- **Database**: MySQL, Redis (Lock 관리)
- **Frontend**: Mustache, Bootstrap 5, Vanilla CSS
- **Auth**: HttpSession + HandlerInterceptor (Login, Admin 구현 완료)

## 4. AI 작업 지침 (Global)
- **SSR 원칙**: 데이터 처리는 서버에서 완료 후 Mustache에 전달한다.
- **상태 변경**: 반드시 `POST` 요청과 PRG 패턴을 사용한다.
- **예외 처리**: `global/_core/handler`의 전역 예외 처리기를 활용한다. (Script/JSON 자동 분기)
- **코드 스타일**: `.ai/rules/common-rule.md`를 준수한다.

## 5. 최근 업데이트 내역
- (2026-03-10) `AdminInterceptor` 추가 및 `WebMvcConfig` 경로 매핑 완료.
- (2026-03-10) `GlobalExceptionHandler` AJAX/일반 요청 응답 분기 로직 구현.
- (2026-03-10) `User` 엔티티 기본 필드 구현.
- (2026-03-10) 프로젝트 초기화 및 `deepinit` 스킬 실행 완료.
