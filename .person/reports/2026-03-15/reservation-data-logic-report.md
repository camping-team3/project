# [Task 1-2] 데이터 조회 로직 (Repository & Service) 완료 보고서

- **Date**: 2026-03-15
- **Reporter**: parkcoding
- **Task**: 1-2. 데이터 조회 로직 (Repository & Service) 구현

## 📝 작업 요약
예약 시스템의 핵심인 '가용 사이트 조회' 로직을 Repository와 Service 레이어에 구현했습니다. 사용자가 원하는 날짜와 인원, 구역에 맞춰 예약 가능한 사이트를 실시간으로 필터링하고 요금을 계산합니다.

## 🛠️ 변경 사항

### 1. ReservationRepository (Persistence Layer)
- `findAvailableSites` 메서드 추가:
    - JPQL `@Query`를 통해 기간이 중복되는 기존 예약을 제외한 사이트 목록을 조회합니다.
    - 중복 조건: `(checkIn < existing.checkOut) AND (checkOut > existing.checkIn)`
    - 상태 조건: `PENDING`, `CONFIRMED`, `CANCEL_REQ` 상태인 예약을 유효한 예약으로 간주하여 필터링합니다.
    - 성능 최적화를 위해 구역 ID 및 수용 인원 필터를 쿼리 레벨에서 처리합니다.

### 2. ReservationService (Business Layer)
- `findAvailableSites` 메서드 구현:
    - 검색 조건(`SearchDTO`)이 없는 경우 오늘부터 1박을 기본값으로 설정합니다.
    - Repository에서 가져온 가용 사이트 목록을 기반으로 총 숙박 요금을 계산합니다.
    - 요금 계산 방식: `해당 구역의 기본 요금(normalPrice) * 숙박 일수` (성수기 정책은 Phase 4로 연기됨에 따라 제외).
    - 결과를 `SiteResponse.ListDTO` 리스트로 변환하여 반환합니다.

### 3. data.sql (Test Data)
- `reservation_tb`에 `people_count` 컬럼이 추가됨에 따라 테스트 데이터를 보강했습니다. (Integrity Constraint Violation 해결)

## ✅ 검증 결과
- **단위 테스트**: `./gradlew test`를 통해 `data.sql` 정합성 및 컨텍스트 로딩 확인 완료.
- **로직 검증**: 날짜 중복 시 사이트가 조회 결과에서 제외되는 쿼리 논리 검증 완료.

## 💡 비유로 설명하기
이 작업은 **"식당의 빈 테이블 예약판"**을 만든 것과 같습니다. 
- **Repository**는 "이미 예약된 손님이 있는 테이블은 빼고 보여줘"라는 장부 검색기와 같습니다.
- **Service**는 "손님이 없으면 오늘 날짜로 보여주고, 며칠 머무시는지에 따라 총 음식 값을 미리 계산해서 메뉴판"을 보여주는 점원 역할을 합니다.
- **data.sql 수정**은 장부에 "몇 명인지" 적는 칸을 새로 만들어서 기존 기록들도 모두 채워 넣은 것입니다.
