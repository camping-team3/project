# 📘 개발 가이드 (Development Guide)

본 문서는 캠핑장 예약 시스템의 개발 프로세스와 병렬 개발 시 협업 규칙을 정의합니다.

---

## 🔄 개발 프로세스 (Development Process)

> **Phase(단계) → Task(기능) → Plan(계획) → Report(보고) → 기록**

1. **Phase & Task (고정):** Phase는 전체 개발 단계이며, 그 안에 Task(기능)들이 정리되어 있다. Task의 순서와 정의는 `phases.md`에서 확정되며 고정이다.
2. **Plan (계획):** 각 개발자는 자신이 맡은 Task를 기반으로 `/plan` 스킬을 사용하여 개발 계획을 수립한다.
3. **Report (보고):** Plan 실행 후 생성되는 Report를 팀 블로그에 업로드한다.
4. **하브루타 노트 (개인 기록):** 각자 하브루타 노트에 개발 일기를 작성하여 학습과 회고를 기록한다.

---

## 🤝 병렬 개발 가이드 (Parallel Development Guide)

### 1단계: 코딩 전 사전 합의 (킥오프)

- **Entity & ID 규격:** Phase 1에서 확정된 ERD 기반 Entity를 공유 브랜치에 먼저 머지
- **SessionUser DTO:** 세션에 담길 유저 정보 구조 확정 → 전원이 동일한 세션 객체 사용
- **URL 라우팅 분담:** A(`/auth/**`), B(`/zones/**`, `/sites/**`, `/`), C(`/reservations/**`), D(`/notices/**`, `/galleries/**`)
- **Admin URL 규칙:** 각 도메인 앞에 `/admin` prefix 통일 (예: `/admin/users/**`, `/admin/zones/**`)

### 2단계: 독립 개발 (각자 브랜치)

- 의존하는 도메인이 미완성이면 **하드코딩 Mock**으로 대체
  - B·C·D → User 미완성 시: `SessionUser`를 테스트용 상수로 생성
  - C → Site 미완성 시: `testData.sql`의 사이트 ID를 직접 참조
- 각 도메인별 `testData.sql` 섹션을 나눠서 충돌 방지
- Mustache 레이아웃(`{{> layout/header}}`)은 공통 파일이므로 **먼저 합의 후 고정**

### 3단계: 통합 (머지 & 연결)

- 4개 브랜치를 `develop`에 순차 머지: **User → Zone/Site → Reservation → Board** 순서
- Mock 데이터를 실제 서비스 호출로 교체
- 통합 후 전체 플로우 수동 테스트: 회원가입 → 사이트 조회 → 예약 생성 → 관리자 확정
