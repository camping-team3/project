# 📋 Forest Haven ERP Project TODO

## 🎯 결제 도메인 목표 (Payment Domain Goal)
- 포트원(PortOne) V2를 활용한 실시간 예약 결제 및 자동 환불 시스템 구축
- 10분간의 사이트 선점(Lock) 및 사용자 이탈 시 즉시 해제 로직 구현
- 웹훅(Webhook)을 통한 서버 간 결제 검증 및 데이터 정합성 확보

---

## 🛠️ 개발 단계 및 태스크 (Implementation Plan)

### 1단계: 환경 구축 및 데이터 모델 설계 (Phase 3-2.1)
- [x] **Task 1: 결제 환경 설정 및 Mock 구조 설계**
  - [x] `application.properties` 내 포트원 API 키(`STORE_ID`, `CHANNEL_KEY` 등) 구조 정의
  - [x] 테스트 계정 부재 시를 대비한 Mock 결제 서비스 인터페이스 설계
- [x] **Task 2: Payment(결제 이력) 엔티티 및 Repository 구현**
  - [x] `Payment` 엔티티 생성: 결제 고유 ID(imp_uid), 결제 수단, 결제 금액, 상태(PAID, CANCELLED), 결제 시각 등 저장
  - [x] `Reservation` 엔티티와 1:1 연관관계 설정
- [x] **Task 3: 예약 선점(Lock) 스케줄러 구현**
  - [x] `ReservationScheduler`: `PENDING` 상태로 생성된 지 10분이 초과된 예약 데이터를 1분 주기로 자동 삭제

### 2단계: 결제 프로세스 및 선점 해제 구현 (Phase 3-2.2)
- [ ] **Task 4: 선점 즉시 해제 API 구현**
  - `DELETE /api/reservations/{id}/lock`: 사용자가 결제창 이탈(뒤로가기/창닫기) 시 호출할 선점 해제 로직
- [ ] **Task 5: 결제 페이지(payment.mustache) 고도화**
  - 포트원 V2 JavaScript SDK 연동
  - `beforeunload` 이벤트를 활용한 브라우저 이탈 감지 및 Task 4 API 호출 스크립트 추가

### 3단계: 결제 검증 및 웹훅 처리 (Phase 3-2.3)
- [ ] **Task 6: 결제 검증 및 웹훅 컨트롤러 구현**
  - `POST /api/payment/webhook`: 포트원으로부터 결제 성공 신호를 받아 서버 측 최종 검증 수행
  - 결제 내역 단건 조회 API 연동 및 `Reservation` 상태 변경(`PENDING` -> `CONFIRMED`)
- [ ] **Task 7: 결제 성공 방어 로직 강화**
  - 결제 처리 직전 데이터 존재 여부 재검증 (스케줄러에 의해 삭제되었을 경우에 대한 예외 처리)

### 4단계: 자동 환불 시스템 및 위약금 로직 (Phase 3-2.4)
- [ ] **Task 8: 위약금 계산기 및 자동 환불 서비스**
  - 이용일 기준 환불 규정(7일 전 100%, 3~6일 전 50%, 0~2일 전 0%) 자동 산출 로직
  - 관리자 승인 시 포트원 취소 API 연동을 통한 자동 환불 실행
