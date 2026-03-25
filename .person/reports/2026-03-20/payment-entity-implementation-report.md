# 결제 이력 엔티티 및 Repository 구현 리포트
Date: 2026-03-20
Reporter: Git user.name
Task: Phase 3-2.1 Task 2

## 1. 작업 요약
결제 정보를 영구 저장하기 위한 `Payment` 엔티티와 `PaymentRepository`를 구현하고, 기존 `Reservation` 엔티티와의 1:1 연관관계를 설정했습니다.

## 2. 변경 사항
- **Enum 생성**: 결제 상태(`PAID`, `CANCELLED`)를 관리하는 `PaymentStatus` 추가
- **엔티티 구현**: `Payment` 엔티티를 생성하여 포트원 결제 고유 번호(imp_uid), 주문 번호, 결제 수단, 금액 등을 저장하도록 설계
- **연관관계 설정**: `Reservation`과 `Payment` 간의 1:1 양방향 연관관계 설정 (FK는 `Payment`가 소유)
- **Repository 생성**: 주문 번호나 포트원 결제 번호로 결제 내역을 조회할 수 있는 메서드 추가

## 3. 검증 결과
- 엔티티 매핑 및 연관관계 설정 확인.
- `PaymentRepository`의 쿼리 메서드 정의 정상 확인.
- `Reservation` 엔티티에서 `payment` 필드 참조 가능 여부 확인.

## 4. 비유로 설명하기
이 작업은 **'영수증 보관함(Payment 엔티티)'**을 만들고, 각 **'예약 장부(Reservation)'**에 해당 영수증을 딱 하나씩만 붙여놓을 수 있도록 자리를 만든 것과 같습니다. 이제 예약이 발생하면 누가, 언제, 어떤 수단으로 결제했는지 영수증을 통해 바로 확인할 수 있습니다.
