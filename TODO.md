# 📋 Forest Haven ERP Project TODO

## 🎨 디자인 보존 원칙 (Mandatory)

- 모든 Mustache 파일 수정 시 **기존 HTML 구조, CSS 클래스명, 인라인 스타일을 100% 보존**한다.
- 더미 데이터(예: "A-1 데크", "₩65,000")가 있던 자리에만 Mustache 문법(`{{variable}}`)을 적용한다.
- 신규 로직 추가 시 기존 레이아웃이 깨지지 않도록 `{{#sites}}...{{/sites}}` 등의 섹션을 기존 카드 디자인 블록에 정확히 입힌다.

## 🎯 예약 도메인 UI/UX 고도화

- [x] **Task 1: 예약 변경 완료 페이지 UI 통일**
  - [x] `reservation-change-done.mustache` 디자인을 `reservation-cancel-done.mustache` 스타일로 변경
  - [x] 정보 그리드(4:8 비율) 및 카드 레이아웃 적용
  - [x] 안내 배너 및 버튼 그룹 스타일 일관성 확보

- [x] **Task 2: 예약 목록 상세 보기 버튼 제거**
  - [x] `reservations.mustache` 내 중복 기능을 수행하는 '상세 보기' 버튼 삭제
  - [x] 카드 클릭(`onclick`) 기능으로의 일원화 확인
  - [x] 디자인 보존 원칙 준수 (기존 레이아웃 유지)

- [x] **Task 3: 예약 변경 페이지 UI 정밀 조정**
  - [x] 기존 예약 정보의 '예약 완료'를 예약 번호(`RESERV-id`)로 교체
  - [x] 변경 요청 폼(`form`) 내부에 패딩(`p-4`) 추가
  - [x] 체크인/체크아웃/인원 선택 필드를 한 줄(`col-md-4` x 3)로 배치

- [x] **Task 4: 예약 취소 페이지 UI 레이아웃 통일**
  - [x] `reservation-cancel.mustache` 디자인을 `reservation-change.mustache` 스타일로 개편
  - [x] 상단 기존 정보 섹션 (이미지 포함 가로형) 적용
  - [x] 하단 폼 섹션 및 안내 배너 스타일 통일

- [x] **Task 5: 전사 예약 번호 포맷 표준화 (`RESERV-{{id}}`)**
  - [x] 고객용: `complete`, `done` 페이지 포맷 수정 및 스타일 통일
  - [x] 관리자용: `list`, `detail`, `change-detail`, `cancel-detail` 페이지 수정
  - [x] 모든 페이지에서 `RESERV-` 접두사 및 일관된 스타일 적용

## 🎯 관리자 대시보드 통합 및 고도화

### Task 1: 백엔드 데이터 공급 로직 (Admin Dashboard)

- [x] `ReservationService`: `CHANGE_REQ`, `CANCEL_REQ` 상태별 카운트 조회 로직 구현
- [x] `ReservationService`: 변경/취소 요청 통합 목록 조회 기능 (Pageable 지원)
- [x] `AdminController`: 대시보드(`/admin`) 진입 시 통계 데이터 및 최신 요청 목록(5건) Model 주입

### Task 5: 미구현 시즌 관리 메뉴 삭제
- [x] `admin-header.mustache` 내 '시즌 관리' 링크 삭제
- [x] 작업 완료 후 리포트 작성 및 커밋

### Task 6: 관리자 사용자 목록 페이지 미구현 버튼 삭제
- [x] `admin/user/list.mustache` 내 '사용자 추가' 버튼 삭제
- [x] 작업 완료 후 리포트 작성 및 커밋

### Task 2: 대시보드 UI - 요청 현황 카운터

- [x] `dashboard.mustache`: 변경 요청 건수 위젯 구현 (클릭 시 `status=CHANGE_REQ` 리스트 이동)
- [x] `dashboard.mustache`: 취소 요청 건수 위젯 구현 (클릭 시 `status=CANCEL_REQ` 리스트 이동)

### Task 3: 대시보드 UI - 통합 요청 관리 리스트

- [x] `dashboard.mustache`: 변경/취소 요청 통합 리스트 섹션 구현 (최신 5건)
- [x] 리스트 행 클릭 시 해당 예약 상세 페이지로 이동 (`onclick` 로직)
- [x] '전체보기' 버튼 추가 (예약 목록 페이지로 이동)
- [x] 페이지네이션 구현 (Size 5, 버튼 < 1 2 3 4 5 >)

### Task 4: 대시보드 UI - 승인/거절 액션 통합

- [x] 승인/거절 버튼 배치 및 기존 관리자 승인 로직(`POST`) 연결
- [x] 거절 버튼 클릭 시 기존 거절 사유 입력 모달(`reject-modal.mustache`) 연동
- [x] 버튼 디자인 일관성 확보 (`list.mustache` 스타일 복제)

## 🎯 마이페이지 홈 및 예약 내역 구현

### Task 1: 백엔드 데이터 공급 로직 (Mypage Home)

- [x] `ReservationService`: 진행 중인 예약 건수 조회 로직 구현 (PENDING, CONFIRMED, CHANGE_REQ, CANCEL_REQ)
- [x] `ReservationService`: 최근 예약 내역 목록 조회 로직 구현 (진행중 전체 + 1개월 내 이용완료/취소완료)
- [x] `UserResponse.MypageHomeDTO`: 홈 화면용 통합 데이터 DTO 정의

### Task 2: 컨트롤러 연동 (UserController)

- [x] `UserController`: `/mypage` 진입 시 `MypageHomeDTO` 데이터 Model 주입
- [x] 세션 유저 존재 여부 및 예외 처리 강화

### Task 3: 마이페이지 홈 UI 구현 (home.mustache)

- [x] `home.mustache` 기본 레이아웃 및 요약 섹션(진행 중 건수) 강조
- [x] 최근 예약 내역 리스트(테이블/카드) 및 상태 표시 구현
- [x] 데이터 부재 시 Empty State(안내 문구 및 예약 버튼) 구현

### Task 4: 디자인 정밀 조정 및 사후 처리

- [x] 기존 디자인 시스템(Bootstrap 5) 준수 여부 및 반응형 확인
- [x] 작업 완료 보고서 작성 및 TODO 동기화, 커밋

### 4단계: 자동 환불 시스템 및 위약금 로직 (Phase 3-2.4)
- [x] **Task 8: 위약금 계산기 및 자동 환불 서비스**
  - [x] 이용일 기준 환불 규정(7일 전 100%, 3~6일 전 50%, 0~2일 전 0%) 자동 산출 로직
  - [x] 관리자 승인 시 포트원 취소 API 연동을 통한 자동 환불 실행
  - [x] Refund 이력 저장 및 예약 상태 업데이트 (`CANCEL_COMP`)

---

# Phase 4: AI 기반 리뷰 관리 및 평점 시스템 고도화

## 1단계: 데이터 모델 및 서버 로직 확장 (Back-end)

- [x] **Task 1: Review 엔티티 및 DTO 확장**
  - 경로: `src/main/java/com/camping/erp/domain/review/Review.java`
  - 내용: `aiDangerScore` (Integer), `isDeleted` (boolean), `adminReason` (String), `isReviewed` (boolean) 필드 추가
  - `ReviewResponse.AdminListDTO` 생성: 관리자 전용 데이터 구조 정의
- [x] **Task 2: 비속어 필터 및 AI 분석 서비스 구현**
  - `ProfanityFilter`: 서버 측 비속어 사전 대조 로직 (비속어 발견 시 즉시 5점 부여)
  - `AiAnalysisService`: `@Async`를 활용한 Gemini API 연동 (비방 수치 1~5점 판별)
- [x] **Task 3: 평점 재계산 로직 구현**
  - `ReviewService.recalculateAverageRating(Long siteId)`: 삭제/복구 시 호출되어 해당 사이트의 평점을 실시간 갱신

## 2단계: 관리자 리뷰 관리 대시보드 UI (Front-end)

- [x] **Task 4: 관리자 리뷰 목록 페이지 구현**
  - 경로: `/admin/reviews`
  - 기능: 위험도(1~5) 필터링, 검색, 검토 대기 항목 최상단 노출
- [x] **Task 5: [삭제/유지] 액션 처리 API**
  - **[유지]**: `isReviewed = true` 처리
  - **[삭제]**: 사유 입력 후 `isDeleted = true`, `isReviewed = true` 처리 및 평점 재계산

## 3단계: 고객 마이페이지 및 시스템 안정화 (User Side)

- [x] **Task 6: 마이페이지 내 삭제된 리뷰 표시**
  - 작성자 본인에게만 "관리자에 의해 삭제되었습니다" 문구와 사유 노출
- [x] Task 7: 전체 시스템 통합 테스트 및 리포트 작성
  - 비속어 포함 시 즉시 5점 부여 및 관리자 알림 확인
  - 삭제 처리 후 캠핑장 전체 평점 변동 확인

## 🎨 UI/UX 개선 및 유지보수

- [x] Task 1: 메인 페이지 사이트 카드 이미지 교체
  - [x] `index.mustache` 내 추천 사이트 리스트의 이미지를 로컬 이미지(`/images/camping_review2.jpg`)로 변경
  - [x] 기존 이미지 태그의 클래스(`site-card-img`) 및 디자인 구조 100% 보존
  - [x] 작업 완료 후 리포트 작성 및 커밋

- [x] Task 2: 실시간 예약 페이지 버튼 및 요소 색상 수정
  - [x] `reservation/new.mustache` 내 하드코딩된 파란색(`#0d6efd`)을 `--fh-primary` 변수로 교체
  - [x] 사이드바 결제 버튼(`payment-btn`)의 비활성화 상태 색상을 톤앤매너에 맞게 조정 (회색/파란색 제거)
  - [x] 지도 핀(`zone-pin`) 활성화/호버 색상을 `--fh-primary`로 통일
  - [x] 작업 완료 후 리포트 작성 및 커밋

- [x] Task 3: 결제 페이지(`payment.mustache`) 충돌 코드 제거 및 통합
  - [x] Git 머지 충돌 마커(`<<<<<<<`, `=======`, `>>>>>>>`) 제거 및 코드 통합
  - [x] PortOne V2 결제 로직(`dev` 브랜치)과 기존 방문자 정보 동기화 로직(`HEAD`) 병합
  - [x] 중복된 UI 섹션 및 스크립트 이벤트 리스너 정리
  - [x] 작업 완료 후 리포트 작성 및 커밋

- [x] Task 4: 리뷰 목록 페이지(`review/list.mustache`) 기본 이미지 추가
  - [x] 사진이 없는 리뷰(`{{^images}}`) 섹션에 기본 이미지(`/images/camping_review2.jpg`) 출력
  - [x] 기존 카드 레이아웃(`ratio-16x9`) 및 스타일 100% 보존
  - [x] 작업 완료 후 리포트 작성 및 커밋



