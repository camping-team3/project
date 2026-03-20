# Phase 3 Reservation 도메인 확장 (1단계: ReservationChangeRequest 엔티티 생성)

**Date**: 2026-03-19
**Reporter**: parkcoding
**Task**: 1-2. ReservationChangeRequest 엔티티 생성

## 1. 작업 요약
고객의 예약 변경 요청 정보를 관리자 승인 전까지 안전하게 보관하고 관리하기 위한 전용 엔티티를 생성했습니다.

## 2. 변경 사항
- **필드 설계 및 일관성 확보**: 기존 `Reservation` 엔티티와 필드명을 일치시켜 (`newCheckIn`, `newCheckOut`, `newPeopleCount`, `newVisitorName`, `newVisitorPhone`) 개발 생산성과 가독성을 높였습니다.
- **객체 지향적 설계**: 단순히 ID만 저장하는 방식 대신 `Site` 엔티티와의 `ManyToOne` 연관관계를 직접 맺어 데이터 무결성을 보장하고 객체 탐색을 용이하게 했습니다.
- **도메인 모델 캡슐화**: 엔티티 내부에 `approve()`, `reject()` 메서드를 정의하여 비즈니스 상태 변화 로직을 엔티티 스스로 관리하도록 설계했습니다.
- **공통 기반 상속**: `BaseTimeEntity`를 상속받아 요청 생성일과 수정일이 자동으로 기록되도록 했습니다.

## 3. 검증 결과
- `Reservation` 및 `Site` 엔티티와의 연관관계가 지연 로딩(`LAZY`)으로 올바르게 설정되었습니다.
- Lombok 어노테이션(`@Builder`, `@Getter` 등)이 적절히 적용되어 안전한 객체 생성이 가능함을 확인했습니다.

## 4. 비유로 보는 작업 내용
기존 예약서(Reservation)에 직접 낙서를 하는 대신, **'예약 변경 신청서'**라는 별도의 문서를 만든 것과 같습니다. 이 신청서에는 "언제로 날짜를 바꾸고 싶은지", "누가 올 것인지"를 꼼꼼하게 적을 수 있는 칸을 마련했고, 관리자가 '승인' 도장을 찍거나 '거절' 사유를 적을 수 있는 공간도 별도로 확보한 상태입니다.
