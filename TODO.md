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
- [x] **Task 5: 결제 페이지(payment.mustache) 고도화**

### 3단계: 결제 검증 및 웹훅 처리 (Phase 3-2.3)
- [x] **Task 6: 결제 검증 및 웹훅 컨트롤러 구현**
  - [x] `POST /api/payment/webhook`: 포트원으로부터 결제 성공 신호를 받아 서버 측 최종 검증 수행
  - [x] 결제 내역 단건 조회 API 연동 및 `Reservation` 상태 변경(`PENDING` -> `CONFIRMED`)
- [x] **Task 7: 결제 성공 방어 로직 강화**
  - [x] 결제 처리 직전 데이터 존재 여부 재검증 및 조건부 자동 복구 로직 구현
  - [x] 오버부킹 시 즉시 자동 환불 로직 구현

### 4단계: 자동 환불 시스템 및 위약금 로직 (Phase 3-2.4)
- [ ] **Task 8: 위약금 계산기 및 자동 환불 서비스**
  - 이용일 기준 환불 규정(7일 전 100%, 3~6일 전 50%, 0~2일 전 0%) 자동 산출 로직
  - 관리자 승인 시 포트원 취소 API 연동을 통한 자동 환불 실행
