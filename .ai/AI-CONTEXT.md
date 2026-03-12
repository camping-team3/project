# .ai/ — AI 협업 도구 모음

## 목적
AI 에이전트가 이 프로젝트에서 일관되게 작업할 수 있도록 규칙, 스킬, 컨텍스트를 정의한다.
루트 AI-CONTEXT 계층의 최상단 노드이며, 모든 하위 `AI-CONTEXT.md`의 최종 부모다.

## 프로젝트 개요
**캠핑장 예약 시스템 (Forest Haven ERP)**
- 단일 캠핑장을 위한 풀스택 SSR 웹 서비스
- 고객 예약·결제, 관리자 운영·통합 대시보드
- Stack: Java 21 + Spring Boot + Mustache + H2/MySQL + Bootstrap 5

## 디렉토리 구조
| 경로 | 설명 |
|------|------|
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
