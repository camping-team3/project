# 📋 Forest Haven ERP Project TODO

## 🎯 결제 도메인 목표 (Payment Domain Goal)
- 포트원(PortOne) V2를 활용한 실시간 예약 결제 및 자동 환불 시스템 구축
- 10분간의 사이트 선점(Lock) 및 사용자 이탈 시 즉시 해제 로직 구현
- 웹훅(Webhook)을 통한 서버 간 결제 검증 및 데이터 정합성 확보

---

# Phase 3: Reservation 도메인 (확장) 구현

## 🎨 디자인 보존 원칙 (Mandatory)

- 모든 Mustache 파일 수정 시 **기존 HTML 구조, CSS 클래스명, 인라인 스타일을 100% 보존**한다.
- 더미 데이터(예: "A-1 데크", "₩65,000")가 있던 자리에만 Mustache 문법(`{{variable}}`)을 적용한다.
- 신규 로직 추가 시 기존 레이아웃이 깨지지 않도록 `{{#sites}}...{{/sites}}` 등의 섹션을 기존 카드 디자인 블록에 정확히 입힌다.

## 1단계: 데이터 베이스 및 도메인 확장 (DB & Entity)

### Task 1: 기초 도메인 모델 구성

- [x] **1-1. Enum 상태값 확장 및 정의**
  - `ReservationStatus` 수정: `CHANGE_REQ` (변경 요청 중) 상태 추가
    - 경로: `src/main/java/com/camping/erp/domain/reservation/enums/ReservationStatus.java`
  - `RequestStatus` 생성: `PENDING` (승인 대기), `APPROVED` (승인 완료), `REJECTED` (거절됨) 정의
    - 경로: `src/main/java/com/camping/erp/domain/reservation/enums/RequestStatus.java`
- [x] **1-2. ReservationChangeRequest 엔티티 생성**
  - 경로: `src/main/java/com/camping/erp/domain/reservation/ReservationChangeRequest.java`
  - 필드: `id`, `reservation` (ManyToOne), `newStartDate`, `newEndDate`, `newSiteId`, `status` (RequestStatus), `rejectionReason`, `BaseTimeEntity` 상속
- [x] **1-3. ReservationCancelRequest 엔티티 생성**
  - 경로: `src/main/java/com/camping/erp/domain/reservation/ReservationCancelRequest.java`
  - 필드: `id`, `reservation` (ManyToOne), `reason`, `status` (RequestStatus), `rejectionReason`, `BaseTimeEntity` 상속
- [x] **1-4. Reservation 엔티티 연관관계 추가**
  - 경로: `src/main/java/com/camping/erp/domain/reservation/Reservation.java`
  - 내용: `OneToMany`로 `changeRequests`, `cancelRequests` 리스트 추가 (이력 관리용)

### Task 2: 데이터 저장소 및 자동화 설정

- [x] **2-1. 전용 Repository 인터페이스 신규 생성**
  - `ReservationChangeRequestRepository` 생성
  - `ReservationCancelRequestRepository` 생성
  - 목적: 각 요청 건의 저장(save), 상태별 조회(findByStatus) 등 핵심 기능 확보
- [x] **2-2. 예약 상태 자동 업데이트 스케줄러 구현**
  - 경로: `src/main/java/com/camping/erp/global/scheduler/ReservationScheduler.java` (신규 생성)
  - 기능: 매일 자정(00:00) 체크아웃 날짜(`checkOut`)가 현재 날짜 이전인 `CONFIRMED` 예약 건들을 `COMPLETED` 상태로 일괄 변경
  - 로직: `@Scheduled(cron = "0 0 0 * * *")` 활용

## 2단계: 고객 마이페이지 (Customer Side)

### Task 3: 상세 조회 및 요청 프로세스 구현

- [x] **3-1. 예약 목록 페이지 구현**
  - 컨트롤러: `ReservationController` (`/mypage/reservations`)
  - 머스타치: `templates/mypage/reservations.mustache` 수정
  - 기능:
    - 세션 기반 본인 예약 목록 조회
    - `CONFIRMED`: 예약변경/예약취소 버튼 노출
    - `COMPLETED`: [리뷰 작성하기] (Placeholder 확보)
    - `CHANGE_REQ`/`CANCEL_REQ`: "승인 대기 중" 상태 표시
- [x] **3-2. 예약 상세 페이지 구현 (고객용)**
  - 컨트롤러: `/mypage/reservations/{id}/detail` (기존 매핑 활용)
  - 머스타치: `templates/mypage/reservation-detail.mustache` (더미 데이터 교체 및 기능 구현)
  - 세부 구현 내용:
    - `ReservationResponse.DetailDTO` 확장: 요청 이력(변경/취소) 리스트 및 상태 플래그(`canModify`, `isWait` 등) 추가
    - `ReservationService.getReservationDetail` 보완: 연관된 요청 이력 데이터를 포함하여 DTO 변환
    - UI 구현: `reservations.mustache`와 동일한 버튼 스타일(색상, 아이콘, 크기) 적용 및 기능 연결
    - 이력 섹션 추가: 과거 변경/취소 요청들의 처리 상태 및 거절 사유 출력
    - 디자인 원칙: 기존 상세 페이지의 레이아웃과 CSS를 100% 보존하며 데이터만 연동
- [x] **3-3. 예약 변경 요청 기능 및 가예약(Lock) 로직 구현**
  - 컨트롤러: `/mypage/reservation/{id}/change-form` (GET, POST)
  - 머스타치: `templates/mypage/reservation-change.mustache`, `templates/mypage/reservation-change-done.mustache`
  - 서비스 로직:
    - 변경 요청 시 `Reservation`의 상태를 `CHANGE_REQ`로 변경
    - 중복 예약 체크 시 `Reservation.status = CHANGE_REQ`인 원본 자리 보호 및 `ReservationChangeRequest.status = PENDING`인 새로운 자리 가예약 처리
- [x] **3-4. 예약 취소 요청 기능 구현**
  - 컨트롤러: `/mypage/reservation/{id}/cancel-form` (GET, POST)
  - 머스타치: `templates/mypage/reservation-cancel.mustache`, `templates/mypage/reservation-cancel-done.mustache`
  - 서비스 로직: 취소 요청 시 `Reservation`의 상태를 `CANCEL_REQ`로 변경
- [x] **3-5. 상태 기반 UI 제어 로직 적용**
  - 로직: `checkInDate`가 현재 날짜 이후인 경우에만 변경/취소 버튼 노출 (Mustache 내에서 처리)

## 3단계: 관리자 예약 관리 (Admin Side)

### Task 4: 관리자 통합 승인 및 관리 기능 구현

- [ ] **4-1. 관리자 예약 목록 필터링 확장**
  - 경로: `src/main/java/com/camping/erp/domain/reservation/ReservationController.java` (관리자 매핑)
  - 기능: `CHANGE_REQ`, `CANCEL_REQ` 상태별 필터링 기능 추가
- [ ] **4-2. 관리자 요청 상세 및 비교 페이지 구현**
  - 경로: `/admin/reservation/change-detail/{requestId}` 및 `/admin/reservation/cancel-detail/{requestId}`
  - 기능: 원본 예약 정보와 요청 정보 비교 뷰
- [ ] **4-3. 승인/거절 처리 API 및 비즈니스 로직 구현**
  - 승인 시: `Reservation` 엔티티 정보 업데이트 및 상태를 `CONFIRMED`로 복구, 요청 상태는 `APPROVED`로 변경
  - 거절 시: `Reservation` 상태를 `CONFIRMED`로 복구, 요청 상태는 `REJECTED`로 변경 및 사유 저장

## 4단계: 통합 테스트 및 검증

### Task 5: 시스템 안정성 및 규칙 검증

- [ ] **5-1. 가예약(Lock) 정합성 테스트**: 변경 요청 중인 사이트(원본 및 신규)에 대해 타 사용자의 중복 예약 시도 차단 여부 확인
- [ ] **5-2. 비즈니스 규칙 및 예외 처리 검증**: 이용일(체크인)이 이미 지난 예약에 대해 변경/취소 요청 시도 시 예외 처리 확인
- [ ] **5-3. 최종 리포트 작성 및 TODO.md 완료 체크**
  - 경로: `.person/reports/{YYYY-MM-DD}/reservation-extension-report.md` 작성

---

## 🛠️ 개발 단계 및 태스크 (Implementation Plan) - Phase 3-2. 결제

### 1단계: 환경 구축 및 데이터 모델 설계 (Phase 3-2.1)
- [x] **Task 1: 결제 환경 설정 및 Mock 구조 설계**
- [x] **Task 2: Payment(결제 이력) 엔티티 및 Repository 구현**
- [x] **Task 3: 예약 선점(Lock) 스케줄러 구현**

### 2단계: 결제 프로세스 및 선점 해제 구현 (Phase 3-2.2)
- [x] **Task 4: 가예약(Lock) 생성 및 즉시 해제 API 구현**
- [x] **Task 5: 결제 페이지(payment.mustache) 고도화**

### 3단계: 결제 검증 및 웹훅 처리 (Phase 3-2.3)
- [x] **Task 6: 결제 검증 및 웹훅 컨트롤러 구현**
- [x] **Task 7: 결제 성공 방어 로직 강화**

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
- [ ] **Task 7: 전체 시스템 통합 테스트 및 리포트 작성**
  - 비속어 포함 시 즉시 5점 부여 및 관리자 알림 확인
  - 삭제 처리 후 캠핑장 전체 평점 변동 확인
