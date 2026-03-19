# 📋 Forest Haven ERP Project TODO

## Phase 2: MVP 핵심 도메인 (완료)
- [x] User 도메인 (가입/로그인/관리자)
- [x] Site & Zone 도메인 (목록/상세/관리자)
- [x] Reservation 도메인 (검색/가용조회/신청/관리자)
- [x] Notice & Gallery 도메인 (목록/상세/다중이미지/관리자)

## Phase 3: 확장 도메인 (진행 중)

### 1. Review 도메인 (완료)
- [x] Review 엔티티 및 Repository 구현
- [x] ReviewService (저장/삭제/평점 동기화) 구현
- [x] ReviewController (등록/삭제/목록 API) 구현
- [x] review/new.mustache (리뷰 작성 폼) 연동
- [x] review/list.mustache (전체 리뷰 목록) 구현 및 연동
- [x] 마이페이지 예약 내역(`reservation/list.mustache`)에서 '리뷰 쓰기' 버튼 활성화 (Task 9)
- [x] 관리자 리뷰 관리 페이지(`admin/review/list.mustache`) 구현 및 연동 (Task 10)

### 2. Payment 도메인 (대기)
- [ ] 포트원(PortOne) V2 연동 (JavaScript SDK)
- [ ] 결제 검증 로직 구현 (Backend)
- [ ] 결제 완료 시 예약 상태 자동 변경 (`PENDING` -> `CONFIRMED`)

### 3. Qna & Comment 도메인 (대기)
- [ ] Q&A 목록 및 상세 조회 로직
- [ ] Q&A 작성 및 수정 (답변 완료 전까지만)
- [ ] 관리자 답변(Comment) 등록 및 상태 전이 로직

## 사후 처리 및 문서화
- [x] 2026-03-19 Review 도메인 구현 리포트 작성
- [x] 2026-03-19 Review 전체 목록 페이지 구현 완료
- [x] 2026-03-19 마이페이지 & 관리자 리뷰 연동 완료
- [x] TODO.md 및 phases.md 상태 동기화
