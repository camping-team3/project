# .ai/ — AI 협업 도구 모음

<<<<<<< HEAD
## 1. 프로젝트 마일스톤
- [x] **Phase 1: 인프라 및 도메인 모델 설계** (100%) - 🔵 완료
- [ ] **Phase 2: MVP 핵심 도메인** (45%) - 🟢 진행 중
- [ ] **Phase 3: 확장 도메인** (0%)
- [ ] **Phase 4: 고도화 및 안정화** (0%)
- [ ] **Phase 5: 안정화 및 최종 검수** (0%)
=======
## 목적
AI 에이전트가 이 프로젝트에서 일관되게 작업할 수 있도록 규칙, 스킬, 컨텍스트를 정의한다.
루트 AI-CONTEXT 계층의 최상단 노드이며, 모든 하위 `AI-CONTEXT.md`의 최종 부모다.
>>>>>>> 99492c1d3d3dca07c67d1b454c7e8e3371a97993

## 프로젝트 개요
**캠핑장 예약 시스템 (Forest Haven ERP)**
- 단일 캠핑장을 위한 풀스택 SSR 웹 서비스
- 고객 예약·결제, 관리자 운영·통합 대시보드
- Stack: Java 21 + Spring Boot + Mustache + H2/MySQL + Bootstrap 5

## 디렉토리 구조
| 경로 | 설명 |
|------|------|
<<<<<<< HEAD
| `.ai/` | AI 지침 및 컨텍스트 관리 |
| `.person/` | **PRD, ERD, 아키텍처 등 설계 문서 (최신화 완료)** |
| `src/main/java/.../domain/` | 도메인 주도 설계(DDD) 기반 패키지 구조 |
| `src/main/java/.../domain/user/` | 회원 관리 (User, Role, Status 구현) |
| `src/main/java/.../domain/reservation/` | 예약 관리 (Reservation, Payment, Refund 엔티티 완료) |
| `src/main/java/.../domain/site/` | **공간 관리 (Zone & Site CRUD 및 가용성 필터링 완료)** |
| `src/main/java/.../domain/notice/` | 시스템 공지사항 관리 (Notice) |
| `src/main/java/.../domain/gallery/` | 캠핑장 포토 갤러리 (Gallery, Image) |
| `src/main/java/.../domain/qna/` | 사용자 문의 및 관리자 답변 (Qna, Comment) |
| `src/main/java/.../domain/review/` | 예약 기반 이용 후기 (Review, Image) |
| `src/main/java/.../domain/image/` | 공통 이미지 메타데이터 관리 (Image) |
| `src/main/java/.../global/` | 전역 설정, 예외 처리, 공통 유틸리티 |
| `src/main/java/.../global/_core/` | **핵심 기반 기술 (인터셉터, 전역 예외 처리, 공통 응답)** |
| `src/main/resources/templates/` | **Mustache 뷰 템플릿 (Admin 관리 페이지 및 메인 검색 UI 구축)** |
| `src/main/resources/db/` | `testData.sql` (데이터 설계 및 대량 삽입 예정) |

## 3. 비즈니스 로직 매핑
- **인증/인가**: `HttpSession` + `HandlerInterceptor` 기반. `LoginInterceptor`는 일반 회원 경로를, `AdminInterceptor`는 `/admin/**` 경로를 철저히 보호함.
- **예외 처리**: `GlobalExceptionHandler`가 요청 헤더를 분석하여 AJAX 요청 시 JSON(`Resp`)을, 일반 요청 시 알럿창 후 뒤로가기(`Script`)를 자동 반환함.
- **데이터 관리**: 모든 엔티티는 `BaseTimeEntity`를 상속받아 생성/수정 시간을 자동 기록함.
- **예약 흐름**: PRD에 따라 `PENDING` -> `CONFIRMED` -> `CANCEL_REQ` -> `CANCEL_COMP` 순서로 상태가 전이되도록 엔티티 설계됨.
- **공간/요금**: `Zone`의 요금 정책을 기반으로 `Site` 검색 시 숙박 기간에 따른 총 요금이 자동 계산됨. (DTO 생성자 패턴 적용)

## 4. AI 작업 지침 (Global)
- **SSR 원칙**: 데이터 처리는 서버에서 완료 후 Mustache에 전달한다.
- **상태 변경**: 반드시 `POST` 요청과 PRG 패턴을 사용한다.
- **예외 처리**: `global/_core/handler`의 전역 예외 처리기를 활용한다.
- **코드 스타일**: `.ai/rules/common-rule.md`를 준수하며, **DTO 엔티티 생성자 패턴**을 강제한다.

## 5. 작업 히스토리 (History)
- **(2026-03-12) 사용자용 가용 사이트 필터링 및 요금 계산 구현**
    - 입실/퇴실 날짜 기반 실시간 예약 가능 여부 조회 로직 구축 (`SiteRepository` 서브쿼리 활용).
    - `reservation` 패키지 수정 없이 외부 참조만으로 가용성 체크 완료 (Surgical Approach).
    - 숙박 일수 기반 `totalPrice` 합산 및 `index.mustache` 검색 UI 연동.
- **(2026-03-12) DTO 엔티티 생성자 패턴(Constructor Pattern) 도입**
    - Service에서 `set` 메서드 호출을 금지하고 DTO 내부에서 엔티티를 받아 데이터를 가공하는 구조로 전면 리팩토링.
    - 공통 규칙(`common-rule.md`)에 해당 패턴을 CRITICAL 규칙으로 명문화.
- **(2026-03-12) 관리자용 구역(Zone) 및 사이트(Site) CRUD 완성**
    - `ZoneRepository`, `ZoneService`, `AdminSiteController` 및 전용 UI(management, save-form) 구축.
    - 구역과 사이트의 1:N 관계를 UI 및 데이터 로직에 반영.
- **(2026-03-11) 비즈니스 규칙(business-rule.md) 최신화**
    - `board` 패키지 해체 및 독립 도메인 구조를 반영한 규칙 전면 재구성.
- **(2026-03-11) 전 도메인 테스트 더미 데이터(testData.sql) 생성**
    - `ERD.md` 및 `PRD.md` 명세를 기반으로 11개 테이블에 대한 유의미한 더미 데이터 구축.
- **(2026-03-11) 도메인 패키지 구조 재설계 및 board 패키지 해체**
    - `board` 패키지에 모여 있던 엔티티들을 `notice`, `gallery`, `qna`, `review`, `image`로 분리하여 독립 도메인화.
=======
| `rules/` | 프로젝트 공통 규칙 및 비즈니스 정책 |
| `skills/` | AI 에이전트 스킬 정의 (deepinit, plan, deep-interview 등) |

## 주요 규칙 파일
| 파일 | 설명 |
|------|------|
| `rules/common-rule.md` | 패키지 구조, 코딩 컨벤션, Web/Response 전략, 워크플로우 |
| `rules/business-rule.md` | 예약 라이프사이클, 요금/환불 규정, 도메인별 비즈니스 규칙 |

## AI 스킬 목록
| 스킬 | 트리거 | 설명 |
|------|--------|------|
| `deepinit` | `/deepinit` | 코드베이스 인덱싱 및 계층형 AI-CONTEXT.md 생성 |
| `plan` | `/plan` | 구현 계획 수립 |
| `deep-interview` | `/deep-interview` | 요구사항 심층 인터뷰 |

## AI 작업 지침
- 코드 작업 전 반드시 `rules/common-rule.md` → `rules/business-rule.md` → 해당 디렉토리 `AI-CONTEXT.md` 순서로 읽는다.
- 작업 완료 후 `.person/reports/{YYYY-MM-DD}/{기능명}-report.md` 생성 필수 (Reporter: git user.name 포함).
- `<!-- MANUAL -->` 태그가 있는 섹션은 deepinit 업데이트 시에도 보존한다.

## 연결 문서
- 프로젝트 루트: `../AI-CONTEXT.md`
- 세션 가이드: `../AI-GUIDE.md`
- PRD: `../.person/docs/PRD.md`
- 로드맵: `../.person/docs/phases.md`
>>>>>>> 99492c1d3d3dca07c67d1b454c7e8e3371a97993
