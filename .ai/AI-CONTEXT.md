# 🏕️ 캠핑장 예약 시스템 (ERP)

## 1. 프로젝트 마일스톤
- [x] **Phase 1: 인프라 및 도메인 모델 설계** (100%) - 🔵 완료
- [ ] **Phase 2: 회원 및 인증 시스템** (20%) - 🟢 진행 중
- [ ] **Phase 3: 예약 및 결제 핵심 로직** (0%)
- [ ] **Phase 4: 관리자 대시보드 및 콘텐츠 관리** (0%)
- [ ] **Phase 5: 안정화 및 최종 검수** (0%)

## 2. 프로젝트 맵
| 경로 | 설명 |
|------|------|
| `.ai/` | AI 지침 및 컨텍스트 관리 |
| `.person/` | **PRD, ERD, 아키텍처 등 설계 문서 (최신화 완료)** |
| `src/main/java/.../domain/` | 도메인 주도 설계(DDD) 기반 패키지 구조 |
| `src/main/java/.../domain/user/` | 회원 관리 (User, Role, Status 구현) |
| `src/main/java/.../domain/reservation/` | 예약 관리 (Reservation, Payment, Refund 엔티티 완료) |
| `src/main/java/.../domain/site/` | 사이트 관리 (Zone, Site 계층형 모델링 완료) |
| `src/main/java/.../domain/notice/` | 시스템 공지사항 관리 (Notice) |
| `src/main/java/.../domain/gallery/` | 캠핑장 포토 갤러리 (Gallery, Image) |
| `src/main/java/.../domain/qna/` | 사용자 문의 및 관리자 답변 (Qna, Comment) |
| `src/main/java/.../domain/review/` | 예약 기반 이용 후기 (Review, Image) |
| `src/main/java/.../domain/image/` | 공통 이미지 메타데이터 관리 (Image) |
| `src/main/java/.../global/` | 전역 설정, 예외 처리, 공통 유틸리티 |
| `src/main/java/.../global/_core/` | **핵심 기반 기술 (인터셉터, 전역 예외 처리, 공통 응답)** |
| `src/main/resources/templates/` | Mustache 뷰 템플릿 (Layout, Index 구조화) |
| `src/main/resources/db/` | `testData.sql` (데이터 설계 및 대량 삽입 예정) |

## 3. 비즈니스 로직 매핑
- **인증/인가**: `HttpSession` + `HandlerInterceptor` 기반. `LoginInterceptor`는 일반 회원 경로를, `AdminInterceptor`는 `/admin/**` 경로를 철저히 보호함.
- **예외 처리**: `GlobalExceptionHandler`가 요청 헤더를 분석하여 AJAX 요청 시 JSON(`Resp`)을, 일반 요청 시 알럿창 후 뒤로가기(`Script`)를 자동 반환함.
- **데이터 관리**: 모든 엔티티는 `BaseTimeEntity`를 상속받아 생성/수정 시간을 자동 기록함.
- **예약 흐름**: PRD에 따라 `PENDING` -> `CONFIRMED` -> `CANCEL_REQ` -> `CANCEL_COMP` 순서로 상태가 전이되도록 엔티티 설계됨.

## 4. AI 작업 지침 (Global)
- **SSR 원칙**: 데이터 처리는 서버에서 완료 후 Mustache에 전달한다.
- **상태 변경**: 반드시 `POST` 요청과 PRG 패턴을 사용한다.
- **예외 처리**: `global/_core/handler`의 전역 예외 처리기를 활용한다.
- **코드 스타일**: `.ai/rules/common-rule.md`를 준수한다.

## 5. 작업 히스토리 (History)
- **(2026-03-11) 비즈니스 규칙(business-rule.md) 최신화**
    - `board` 패키지 해체 및 독립 도메인 구조를 반영한 규칙 전면 재구성.
    - `Review` 작성 권한, `Qna` 상태 전이, `Image` 관리 정책 등 상세 비즈니스 로직 명시.
- **(2026-03-11) 전 도메인 테스트 더미 데이터(testData.sql) 생성**
    - `ERD.md` 및 `PRD.md` 명세를 기반으로 11개 테이블에 대한 유의미한 더미 데이터 구축.
    - 테이블 역할 기술 및 Enum 상태값(Status, Role 등) 가이드를 주석으로 포함하여 데이터 가독성 증대.
    - FK 제약 조건을 고려한 삽입 순서 최적화.
- **(2026-03-11) 도메인 패키지 구조 재설계 및 board 패키지 해체**
    - `board` 패키지에 모여 있던 엔티티들을 `notice`, `gallery`, `qna`, `review`, `image`로 분리하여 독립 도메인화.
    - 각 도메인별 `Repository`, `Service`, `Controller`, `AI-CONTEXT.md` 기본 구조 구축.
    - 불필요한 `Board.java`, `BoardService.java` 제거 및 의존성 정리.
- **(2026-03-11) 도메인 모델링 및 인프라 고도화**
    - `Reservation`, `Zone`, `Site`, `Notice` 등 전체 엔티티 모델링 완료 (ERD 명세 준수).
    - `LoginInterceptor` 추가 및 `WebMvcConfig` 인터셉터 경로 매핑 (Security 기반 마련).
    - `User` 엔티티에 `UserRole`, `UserStatus` Enum 적용 및 익명화 정책 반영 준비.
- **(2026-03-10) 프로젝트 초기화 및 기반 기술 구축**
    - `BaseTimeEntity`, `Resp` 유틸리티 추가.
    - `GlobalExceptionHandler` 구현 (Exception400~500 및 AJAX/Script 응답 분기).
    - `deepinit` 스킬을 통한 코드베이스 인덱싱 및 `.ai` 지침 체계 수립.
