# Forest Haven ERP 개발 진행 현황 (TODO)

## 🚨 [Task 0] 코드베이스 컨플릭트(Conflict) 완전 해결
- [ ] **0-1. 문서 및 로드맵 정리**
  - [ ] `.person/docs/phases.md`: Notice/Gallery 및 Reservation 진행도 병합
- [ ] **0-2. 머스태시(Mustache) 템플릿 복구**
  - [ ] `src/main/resources/templates/reservation/new.mustache`: 검색/필터 디자인 병합
  - [ ] `src/main/resources/templates/reservation/payment.mustache`: 결제 폼 및 방문자 정보 입력란 병합
  - [ ] `src/main/resources/templates/admin/gallery/update-form.mustache`: 갤러리 수정 로직 병합
- [ ] **0-3. 최종 검증**
  - [ ] 전체 빌드 및 뷰 렌더링 확인 (컨플릭트 마커 잔존 여부 재검색)

## 🔑 Phase 2: MVP 핵심 도메인 (완료 단계)
- [x] **User 도메인 (100%):** 회원가입, 로그인, 관리자 회원 관리
- [x] **Zone & Site 도메인 (100%):** 구역/사이트 CRUD, 실시간 예약 가능 여부 조회
- [x] **Reservation 도메인 (90%):** 예약 신청 flow, 관리자 현황 조회 및 페이징 (컨플릭트 해결 후 100%)
- [x] **독립 도메인 (Notice + Gallery):** 공지사항/갤러리 CRUD 및 이미지 업로드 통합 (컨플릭트 해결 후 100%)

## 💬 Phase 3: 확장 도메인 (진행 예정)
- [ ] **[Task 1] Payment 도메인 (포트원 연동)**
  - [ ] 고객: 포트원 V2 연동 결제 검증 및 예약 즉시 확정
  - [ ] 관리자: 결제 내역 조회 및 취소/환불 연동 기초
- [ ] **[Task 2] Qna & Comment 도메인**
  - [ ] 고객: Q&A 작성 및 비공개 여부 처리
  - [ ] 관리자: 답변 등록 및 답변 완료 시 수정 잠금 로직
- [ ] **[Task 3] Review 도메인**
  - [ ] 고객: '이용 완료' 예약 건 대상 별점/사진 리뷰 작성
  - [ ] 시스템: 리뷰 등록 시 사이트/구역 평점 즉시 반영
- [ ] **[Task 4] 마이페이지 (MyPage) 확장**
  - [ ] 고객: 본인 예약 내역 상세 조회 및 취소 요청

## 🛡️ Phase 4: 고도화 및 안정화 (대기)
- [ ] Refund 도메인 (위약금 자동 계산 및 환불)
- [ ] Admin Dashboard 통계 시각화
- [ ] 시즌별(성수기) 요금 정책 동적 관리
