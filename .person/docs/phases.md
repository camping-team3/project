# 🛣️ Phases 단계 (Development Roadmap)

본 문서는 캠핑장 예약 시스템의 단계별 개발 계획과 우선순위를 정의합니다. 모든 개발 작업은 아래 페이즈 순서에 따라 순차적으로 진행됩니다.

---

> 개발 프로세스 및 병렬 개발 규칙은 [dev-guide.md](dev-guide.md)를 참고한다.

---

## 🏗️ Phase 1: 도메인 설계 및 환경 구축 (100%) - 🔵 완료

**목표:** 견고한 기술적 토대 마련 및 데이터베이스 구조 확립

- [x] **프로젝트 초기화:** 패키지 구조(`com.camping.erp`), 기본 폴더링(`domain`, `global`, `layout`) 완료
- [x] **규칙 수립:** `common-rule`, `business-rule`, `AI-CONTEXT` 기반의 협업 가이드 확정
- [x] **데이터베이스 ERD 설계:** 11개 도메인 테이블 설계 및 관계 확정 (ERD.md 준수)
- [x] **JPA Entity 매핑:** 11개 도메인(User, Reservation, Zone, Site, Notice, Gallery 등) Entity 클래스 구현 완료
- [x] **데이터 검증:** `testData.sql` 구축 및 JPA Repository 기본 동작 확인 완료

## 🔑 Phase 2: MVP 핵심 도메인 (20%) - 🟢 진행 중

**목표:** 결제 없이 '예약 → 관리자 수동 확정' 플로우가 동작하는 MVP 완성

> 각 도메인은 **User-facing + Admin CRUD**를 함께 구현한다.

- [ ] **User 도메인:**
  - [x] 고객: 로그인, `LoginInterceptor` 및 `AdminInterceptor` 기반 권한 인가 (Security 기반 마련 완료)
  - [ ] 고객: 회원가입 (진행 예정)
  - [ ] 관리자: 회원 목록 조회, 권한 변경
- [ ] **Zone & Site 도메인:**
  - 고객: 메인 페이지 렌더링, 구역·사이트 목록 조회, 날짜별 예약 가능 사이트 필터링
  - 관리자: 구역·사이트 CRUD, 요금 설정
- [x] **Reservation 도메인:**
  - 고객: 예약 생성 플로우
  - 관리자: 예약 현황 목록 조회, 수동 확정/취소 처리
- [ ] **독립 도메인 (Notice + Gallery):** (board 패키지 해체 및 분리 완료)
  - 고객: 공지사항·갤러리 목록 조회
  - 관리자: 공지·갤러리 CRUD, Image 다중 업로드

## 💬 Phase 3: 확장 도메인 (75%) — 4명 병렬

**목표:** 결제 자동화, 커뮤니티, 마이페이지로 서비스 완성도 확보

> 각 도메인은 **User-facing + Admin CRUD**를 함께 구현한다.

- [ ] **Payment 도메인:**
  - 고객: 포트원(PortOne) V2 연동, 결제 검증, 결제 완료 시 예약 즉시 확정
  - 관리자: 결제 내역 조회
- [ ] **Qna & Comment 도메인:**
  - 고객: Q&A 작성
  - 관리자: 답변 등록, 답변 완료 시 수정/삭제 잠금
- [ ] **Review 도메인:**
  - 고객: '이용 완료' 예약 건 대상 별점·사진 리뷰 작성, Image 다중 업로드
  - 관리자: 리뷰 목록 관리
- [ ] **Reservation 도메인 (확장):**
  - 고객: 마이페이지 예약 내역 조회, 상세 정보 확인, 취소 '요청'
  - 관리자: 취소 요청 목록 조회, 처리

## 🛡️ Phase 4: 고도화 및 안정화 (100%)

**목표:** 운영 고도화, 환불 자동화, 서비스 안정성 확보

- [ ] **Refund 도메인:** 위약금 자동 계산, 관리자 '최종 승인' 후 환불 연동
- [ ] **Admin Dashboard:** 매출·예약 통계 시각화, 실시간 현황 모니터링
- [ ] **Zone 도메인 (확장):** 시즌별(성수기/비수기) 요금 정책 관리
- [ ] **공통 안정화:** `GlobalExceptionHandler` 고도화, 통합 테스트, 동시성 제어(Race Condition) 점검
- [ ] **배포 준비:** 빌드 환경 최적화 및 최종 검수

---

## 🛠️ 개발 원칙 (Core Principles)

1. **SSR 우선:** 모든 화면은 Mustache 템플릿 엔진을 사용하는 서버 사이드 렌더링 방식으로 개발한다.
2. **Form 기반:** 데이터 변경은 `<form method="POST">`와 PRG 패턴을 준수한다.
3. **Surgical Update:** 모든 수정은 기존 규칙(AI-CONTEXT, Common Rule)을 엄격히 따르며 기록한다.
