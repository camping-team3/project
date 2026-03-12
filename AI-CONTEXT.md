# 캠핑장 예약 시스템 (Forest Haven ERP)

## 목적
단일 캠핑장 사업장을 위한 풀스택 예약·운영 관리 웹 서비스.
고객은 실시간 사이트 예약·결제를, 관리자는 예약/사용자/콘텐츠를 통합 관리한다.

## 기술 스택
| 항목 | 내용 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 |
| Build | Gradle |
| DB (개발) | H2 In-memory (`MODE=MySQL`) |
| DB (운영) | MySQL |
| ORM | Spring Data JPA (FetchType.LAZY 기본) |
| View | Mustache SSR |
| UI | Bootstrap 5 + Vanilla CSS |
| Auth | HttpSession + HandlerInterceptor |
| 결제 | 포트원(PortOne) V2 |

## 프로젝트 구조
| 경로 | 설명 |
|------|------|
| `src/main/java/com/camping/erp/domain/` | 비즈니스 도메인 레이어 (9개 Bounded Context) |
| `src/main/java/com/camping/erp/global/` | 공통 인프라 레이어 (설정, 인터셉터, 예외처리) |
| `src/main/resources/templates/` | Mustache SSR 뷰 템플릿 |
| `src/main/resources/static/` | 정적 자원 (CSS, JS, 업로드 이미지) |
| `src/main/resources/db/` | 테스트 데이터 SQL |
| `zBackup/html/` | UI 디자인 목업 (HTML/CSS 프로토타입 37개) |
| `.ai/` | AI 협업 규칙 및 스킬 정의 |
| `.person/` | 프로젝트 문서 (PRD, ERD, 로드맵, 보고서) |

## 도메인 목록 (9개)
| 도메인 | 주요 엔티티 | 설명 |
|--------|------------|------|
| `user` | User | 회원 관리, 세션 인증, 권한(ADMIN/USER) |
| `site` | Zone, Site | 캠핑 구역·사이트, 시즌별 요금 정책 |
| `reservation` | Reservation | 예약 라이프사이클 관리 |
| `payment` | Payment | 포트원 결제 연동 |
| `refund` | Refund | 위약금 계산, 환불 이력 |
| `notice` | Notice | 공지사항 CRUD |
| `gallery` | Gallery, Image | 포토갤러리 다중 이미지 |
| `qna` | Qna, Comment | Q&A 게시판, 답변 완료 잠금 |
| `review` | Review | 이용 완료 예약 기반 별점 리뷰 |

## 개발 단계 (Phases)
| Phase | 상태 | 목표 |
|-------|------|------|
| Phase 1 | ✅ 완료 (100%) | 도메인 설계 및 환경 구축 |
| Phase 2 | 🟢 진행 중 (20%) | MVP 핵심 도메인 (User, Site, Reservation, Notice, Gallery) |
| Phase 3 | ⏳ 대기 | 결제·Q&A·리뷰·마이페이지 |
| Phase 4 | ⏳ 대기 | 환불 자동화, 통계 대시보드, 안정화 |

## 핵심 비즈니스 규칙 요약
- **예약 상태**: `PENDING` → `CONFIRMED` → `CANCEL_REQ` → `CANCEL_COMP`
- **결제 즉시 확정**: 포트원 결제 완료 시 관리자 승인 없이 `CONFIRMED` 전이
- **취소 환불**: 관리자 수동 최종 승인 필요
- **오버부킹 방지**: 결제 진입 시 5분간 사이트 선점 Lock
- **Q&A 수정 잠금**: 답변 완료(`isAnswered=true`) 게시글은 수정 불가
- **리뷰 권한**: `CONFIRMED` + 이용 종료일 경과한 예약만 작성 가능

## AI 작업 지침
- **규칙 확인**: `.ai/rules/common-rule.md` + `.ai/rules/business-rule.md` 먼저 읽기
- **컨텍스트 탐색**: 작업 대상 디렉토리의 `AI-CONTEXT.md` 먼저 읽기
- **Form 기반**: 상태 변경은 `<form method="POST">` + PRG 패턴 사용
- **AJAX 제한**: 캘린더 가용성 확인, 이메일 중복 체크 등 꼭 필요한 경우만
- **보고서 필수**: 작업 완료 후 `.person/reports/{YYYY-MM-DD}/{기능명}-report.md` 생성
- **패키지명 주의**: `com.camping.erp` (오타: `caping` 아님)

## 연결 문서
- AI 가이드: `AI-GUIDE.md`
- AI 도구: `.ai/AI-CONTEXT.md`
- PRD: `.person/docs/PRD.md`
- 기술스택: `.person/docs/techstack.md`
- 로드맵: `.person/docs/phases.md`
- ERD: `.person/docs/ERD.md`
