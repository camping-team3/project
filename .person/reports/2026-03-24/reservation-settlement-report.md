# 📋 예약 변경/취소 정산 자동화 시스템 구현 보고서

- **작성일**: 2026-03-24
- **작성자**: handikim (Git user.name)
- **작업 단계**: Phase 3 - Task 6 (Reservation Extension - Settlement)

## 1. 작업 개요
고객의 예약 변경 및 취소 시 발생하는 금액 차액을 시스템이 자동으로 계산하고, 이에 따른 추가 결제(PortOne 연동) 및 부분 환불(위약금 규정 적용)을 처리하는 로직을 구현하였습니다.

## 2. 주요 변경 사항

### 2.1 도메인 모델 및 정산 타입 정의
- `SettlementType` Enum 생성: `ADDITIONAL_PAY`(추가 결제), `PARTIAL_REFUND`(부분 환불), `NONE`(차액 없음)
- `ReservationChangeRequest` 엔티티 확장: `newTotalPrice`, `settlementType` 필드 추가 및 정산 정보 업데이트 메서드 구현

### 2.2 예약 변경 차액 산출 로직 (`ReservationService`)
- 새로운 예약 조건(사이트, 날짜, 인원)에 따른 총액 재계산 로직 구현
- `Zone`의 기본 요금(`normalPrice`) 및 인원 추가 요금(`extraPersonFee`) 정책 반영
- 기존 금액과의 비교를 통한 정산 타입 자동 판별

### 2.3 추가 결제 프로세스 및 API 구축
- `ReservationResponse.ChangePaymentDTO` 추가: 결제창 호출용 데이터 구조
- `ReservationController` API 엔드포인트 추가:
  - `GET /api/reservation/change/{requestId}/payment-data`: 결제 필요 데이터 조회
  - `POST /api/reservation/change/payment/success`: 결제 성공 후 예약 정보 최종 업데이트 및 확정
- 결제 성공 시 `Reservation` 정보 업데이트 및 상태를 `CONFIRMED`로 즉시 복구

### 2.4 위약금 규정 기반 부분 환불 시스템 (`RefundService`)
- `business-rule.md`의 환불 규정(7일 전 100%, 3~6일 전 50%)을 차액 환불에 적용
- `PortOneService`를 통한 실제 결제 금액의 부분 취소(Partial Cancel) 연동
- 환불 이력(`Refund` 엔티티) 자동 기록 및 로그 출력

## 3. 비즈니스 로직 분석 (비유 설명)
이 시스템은 마치 **'캠핑장의 똑똑한 키오스크'**와 같습니다. 
사용자가 "방을 바꾸고 싶어요"라고 하면, 키오스크는 즉시 "새 방은 기존보다 2만원 더 비싸네요" 혹은 "더 싼 방이라 1만원 돌려드릴게요"라고 계산해 줍니다. 
특히 환불할 때는 "손님, 오늘 바꾸시면 위약금 때문에 50%만 돌려받으실 수 있어요"라고 정책을 꼼꼼히 체크하여 주인(관리자)의 손해를 방지하고 손님에게 정확한 금액을 돌려주는 역할을 수행합니다.

## 4. 검증 결과
- [x] 박수 및 인원 추가 요금 기반 금액 계산 로직 확인
- [x] 금액 증가 시 `ADDITIONAL_PAY` 타입 부여 확인
- [x] 금액 감소 시 `PARTIAL_REFUND` 타입 부여 및 위약금 규정 적용 확인
- [x] API 엔드포인트(GET/POST) 정상 선언 및 서비스 로직 연결 확인

## 5. 향후 과제
- 관리자 예약 상세 페이지에 위 정산 정보를 시각적으로 노출 (UI 작업)
- 성수기(Peak) 기간 자동 판별 로직 고도화 연동
