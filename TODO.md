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

### Task 4: 관리자 통합 승인 및 관리 기능 구현 (UI/UX 고도화)

- [x] **4-1. 관리자 예약 목록 필터링 확장 및 UI 고도화**
  - [x] `CHANGE_REQ` 상태 추가: 필터 옵션 및 목록 내 상태 텍스트('변경 요청')/디자인(취소 요청과 동일한 `info` 클래스) 적용
  - [x] UI 정리: '신규 예약 등록' 버튼 제거 및 관리열 아이콘 정리
  - [x] 클릭 이벤트 통합 (Row Click):
    - `PENDING`, `CONFIRMED`, `CANCEL_COMP`, `COMPLETED`: 클릭 시 `admin/reservation/detail` 이동
    - `CHANGE_REQ`: 클릭 시 `admin/reservation/change-detail` 이동
    - `CANCEL_REQ`: 클릭 시 `admin/reservation/cancel-detail` 이동
  - [x] 관리열 버튼 제어: `CHANGE_REQ`, `CANCEL_REQ` 상태일 때만 '승인/거절' 버튼 노출
- [ ] **4-2. 관리자 요청 상세 및 비교 페이지 구현**
  - [ ] `/admin/reservation/change-detail/{id}` 및 `/admin/reservation/cancel-detail/{id}` 구현
  - [ ] 기능: 원본 예약 정보와 요청 정보 비교 뷰 제공 및 승인/거절 버튼 배치
- [ ] **4-3. 관리자 예약 상세 페이지 구현 (detail.mustache)**
  - [ ] 경로: `admin/reservation/detail.mustache` 구현
  - [ ] 내용: 예약자/방문자 정보, 예약 정보, 이용 일정 상세 노출 (요금 산출 내역 제외)
  - [ ] 기능: '사용자 상세 정보보기' 버튼 클릭 시 `admin/user/detail.mustache` 이동
  - [ ] 이력: 예약 관련 모든 요청 이력(변경/취소) 섹션 추가
  - [ ] 버튼: 예약 승인 버튼 숨김 처리
- [ ] **4-4. 승인/거절 처리 API 및 거절 사유 입력 기능 구현**
  - [ ] `reject-modal.mustache` 구현: 거절 사유 입력 폼, 취소 시 이전 페이지 이동, 확정 시 처리 로직 실행
  - [ ] 승인 로직: `Reservation` 업데이트(상태 `CONFIRMED` 복구), 요청 상태 `APPROVED` 변경 후 목록 리다이렉트
  - [ ] 거절 로직: `Reservation` 상태 `CONFIRMED` 복구, 요청 상태 `REJECTED` 및 사유 저장 후 목록 리다이렉트

## 4단계: 통합 테스트 및 검증

### Task 5: 시스템 안정성 및 규칙 검증

- [ ] **5-1. 가예약(Lock) 정합성 테스트**: 변경 요청 중인 사이트(원본 및 신규)에 대해 타 사용자의 중복 예약 시도 차단 여부 확인
- [ ] **5-2. 비즈니스 규칙 및 예외 처리 검증**: 이용일(체크인)이 이미 지난 예약에 대해 변경/취소 요청 시도 시 예외 처리 확인
- [ ] **5-3. 최종 리포트 작성 및 TODO.md 완료 체크**
  - 경로: `.person/reports/{YYYY-MM-DD}/reservation-extension-report.md` 작성
