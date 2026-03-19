# Phase 3: Reservation 도메인 (확장) 구현

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
- [ ] **1-4. Reservation 엔티티 연관관계 추가**
    - 경로: `src/main/java/com/camping/erp/domain/reservation/Reservation.java`
    - 내용: `OneToMany`로 `changeRequests`, `cancelRequests` 리스트 추가 (이력 관리용)

### Task 2: 데이터 저장소 및 초기 데이터 설정
- [ ] **2-1. Repository 기능 확장 및 더미 데이터 추가**
    - 경로: `src/main/java/com/camping/erp/domain/reservation/ReservationRepository.java` (조회 쿼리 추가)
    - 경로: `src/main/resources/db/data.sql` (테스트용 요청 데이터 `INSERT` 문 추가)

## 2단계: 고객 마이페이지 (Customer Side)

### Task 3: 상세 조회 및 요청 프로세스 구현
- [ ] **3-1. 예약 상세 페이지 구현**
    - 컨트롤러: `ReservationController` (`/mypage/reservation/detail/{id}`)
    - 머스타치: `templates/mypage/reservation/detail.mustache`
    - 기능: 기본 예약 정보 + 변경/취소 요청 이력(상태, 거절 사유 포함) 출력
- [ ] **3-2. 예약 변경 요청 기능 및 가예약(Lock) 로직 구현**
    - 컨트롤러: `/mypage/reservation/change-form/{id}` (GET, POST)
    - 서비스 로직:
        - 변경 요청 시 `Reservation`의 상태를 `CHANGE_REQ`로 변경
        - 중복 예약 체크 시 `Reservation.status = CHANGE_REQ`인 원본 자리 보호 및 `ReservationChangeRequest.status = PENDING`인 새로운 자리 가예약 처리
- [ ] **3-3. 예약 취소 요청 기능 구현**
    - 컨트롤러: `/mypage/reservation/cancel-form/{id}` (GET, POST)
    - 서비스 로직: 취소 요청 시 `Reservation`의 상태를 `CANCEL_REQ`로 변경
- [ ] **3-4. 상태 기반 UI 제어 로직 적용**
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
