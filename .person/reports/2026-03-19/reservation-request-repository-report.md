# Phase 3 Reservation 도메인 확장 (Task 2: 전용 Repository 인터페이스 생성)

**Date**: 2026-03-19
**Reporter**: parkcoding
**Task**: 2-1. 전용 Repository 인터페이스 신규 생성

## 1. 작업 요약
예약 변경 및 취소 요청을 각각 효율적으로 관리하기 위해 `ReservationChangeRequest`와 `ReservationCancelRequest` 전용 Repository를 생성했습니다.

## 2. 변경 사항 및 분리 이유
- **Repository 분리**: 기존 `ReservationRepository`에 모든 기능을 몰아넣지 않고, 각 엔티티 전용 Repository 2종을 신규 생성했습니다.
- **분리한 이유 (핵심 가치)**:
    - **단일 책임 원칙 (SRP)**: `Reservation` 본체와 각 요청(`Change/Cancel`)의 데이터 처리 책임을 명확히 분리하여 코드의 복잡성을 낮췄습니다.
    - **Spring Data JPA 활용**: 전용 Repository를 만듦으로써 `save()`, `delete()` 같은 표준 CRUD 기능을 별도 코드 작성 없이 즉시 사용할 수 있게 되었습니다.
    - **유지보수 및 가시성**: 향후 "승인 대기 중인 요청 조회"와 같은 전용 쿼리가 늘어날 때, `ReservationRepository` 파일이 너무 거대해지는 것을 방지하고 기능별로 코드를 관리하기 위해 분리했습니다.
- **추가된 핵심 기능**: 
    - `findByStatus`: 관리자가 승인 대기(`PENDING`) 상태인 요청들만 빠르게 모아볼 수 있는 기능을 추가했습니다.
    - `findByReservationId`: 특정 예약 건에 대한 변경/취소 히스토리를 효율적으로 추적할 수 있도록 구성했습니다.

## 3. 검증 결과
- Spring Data JPA의 표준 명명 규칙에 따라 인터페이스가 정의되었습니다.

## 4. 비유로 보는 작업 내용
기존에는 '모든 우편물(예약)'을 하나의 큰 우체국 창구(ReservationRepository)에서 처리했다면, 이제는 **'이사 요청 창구(ChangeRequest)'**와 **'서비스 해지 창구(CancelRequest)'**를 각각 따로 만든 것과 같습니다. 이렇게 창구를 나누면 각 창구의 직원이 해당 업무만 전문적으로 빠르게 처리할 수 있고, "이사 요청 중인 건들만 보여주세요" 같은 질문에도 즉각 답변이 가능해집니다.
