# 예약 선점 스케줄러 구현 리포트
Date: 2026-03-20
Reporter: Git user.name
Task: Phase 3-2.1 Task 3

## 1. 작업 요약
결제 대기 중인 예약 데이터가 무한히 남지 않도록, 10분 유효 시간이 지난 `PENDING` 예약을 자동으로 삭제하는 스케줄러 로직을 구현했습니다.

## 2. 변경 사항
- **Repository 확장**: `ReservationRepository`에 `findByStatusAndCreatedAtBefore` 메서드를 추가하여 만료된 대기 예약을 효율적으로 조회 가능하게 함.
- **스케줄러 고도화**: `ReservationScheduler`에 1분 주기로 실행되는 `removeExpiredPendingReservations` 메서드 추가.
- **비즈니스 로직**: 현재 시각 기준 10분 이전에 생성된 `PENDING` 데이터를 찾아 일괄 삭제(Batch Delete) 처리.

## 3. 검증 결과
- `ReservationRepository`의 커스텀 메서드 정의 정상 확인.
- `@Scheduled(fixedDelay = 60000)`를 통한 주기적 실행 설정 확인.
- 삭제 로직의 트랜잭션 처리 및 로깅 정상 작동 확인.

## 4. 비유로 설명하기
이 작업은 **'도서관에서 예약 도서를 찾아가지 않는 사람들을 위해 타이머를 설치한 것'**과 같습니다. 예약자가 10분 안에 대출(결제)을 완료하지 않으면, 시스템(스케줄러)이 자동으로 예약 딱지를 떼어내서 다른 사람이 책을 빌릴 수 있게 해줍니다.
