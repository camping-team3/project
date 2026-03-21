# 📋 Forest Haven ERP Project TODO

## 🎯 결제 도메인 목표 (Payment Domain Goal)
- 포트원(PortOne) V2를 활용한 실시간 예약 결제 및 자동 환불 시스템 구축
- 10분간의 사이트 선점(Lock) 및 사용자 이탈 시 즉시 해제 로직 구현
- 웹훅(Webhook)을 통한 서버 간 결제 검증 및 데이터 정합성 확보

---

## 🛠️ 개발 단계 및 태스크 (Implementation Plan)

### 1단계: 환경 구축 및 데이터 모델 설계 (Phase 3-2.1)
- [x] **Task 1: 결제 환경 설정 및 Mock 구조 설계**
- [x] **Task 2: Payment(결제 이력) 엔티티 및 Repository 구현**
- [x] **Task 3: 예약 선점(Lock) 스케줄러 구현**

### 2단계: 결제 프로세스 및 선점 해제 구현 (Phase 3-2.2)
- [x] **Task 4: 가예약(Lock) 생성 및 즉시 해제 API 구현**
  - `ReservationService.getPaymentForm`: 결제 페이지 진입 시 `PENDING` 상태의 예약 데이터 생성 (물리적 선점 시작)
  - `DELETE /api/reservations/{id}/lock`: 세션 유저 검증 후 `PENDING` 예약을 **물리적으로 삭제(Hard Delete)** 하는 API 구현
- [ ] **Task 5: 결제 페이지(payment.mustache) 고도화**
  - PortOne V2 SDK 연동 및 컨트롤러에서 `storeId`, `channelKey` (상수 정의) 전달
  - `beforeunload` / `visibilitychange` 이벤트를 활용한 브라우저 이탈 감지 스크립트 추가
  - 이탈 시 `fetch` (keepalive: true)를 통한 Task 4 API 호출 로직 구현

### 3단계: 결제 검증 및 웹훅 처리 (Phase 3-2.3)
- [ ] **Task 6: 결제 검증 및 웹훅 컨트롤러 구현**
- [ ] **Task 7: 결제 성공 방어 로직 강화**

### 4단계: 자동 환불 시스템 및 위약금 로직 (Phase 3-2.4)
- [ ] **Task 8: 위약금 계산기 및 자동 환불 서비스**
