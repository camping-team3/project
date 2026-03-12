# Report: 사용자용 목록 조회 및 예약 가용성 필터링 개발 (Phase 2 - Step 2)

- **Reporter**: KimNaKim
- **Date**: 2026-03-12

## 1. 작업 요약
고객이 메인 페이지에서 원하는 입실/퇴실 날짜를 기반으로 예약 가능한 캠핑 사이트를 조회하고, 해당 기간의 총 요금을 확인할 수 있는 검색 엔진 및 UI를 구축함. 협업 규칙을 준수하여 `reservation` 패키지의 코드는 수정하지 않고 `site` 패키지 내부에서만 개발을 완료함.

## 2. 상세 변경 사항
### 2.1 백엔드 도메인 레이어 (`domain/site/`)
- **SiteRepository**: `Reservation` 테이블을 참조하여 날짜가 겹치지 않는 가용 사이트만 추출하는 JPQL 쿼리(`findAvailableSites`) 추가.
- **SiteResponse**: 사용자 화면용 `MainDTO` 및 내부 `SiteDTO` 정의 (계산된 총 요금 필드 포함).
- **SiteService**: 
    - `getAvailableZonesWithSites`: 사용자가 입력한 날짜에 따라 가용 사이트를 필터링하고 구역별로 그룹화하는 로직 구현.
    - 숙박 기간(박수) 계산 로직을 통한 `totalPrice` 합산 기능 추가.

### 2.2 컨트롤러 및 뷰 레이어
- **SiteController**: 루트 경로(`/`)를 담당하며, 날짜 검색 파라미터를 받아 `SiteService`와 연동하여 메인 페이지를 렌더링함. (MainController 대신 사용).
- **templates/index.mustache**:
    - 입실/퇴실 날짜 선택을 위한 검색바 인터페이스 구축.
    - 구역별 카드 레이아웃 내 사이트 정보 및 요금 표시.
    - 예약 가능 사이트 옆에 예약 신청 페이지로 연결되는 링크(`siteId`, `checkIn`, `checkOut` 포함) 구현.

## 3. 검증 결과
- `./gradlew.bat classes`를 통한 컴파일 및 의존성 체크 성공.
- `Reservation` 엔티티를 수정하지 않고도 `SiteRepository`에서 연관관계 기반의 서브쿼리를 사용하여 중복 예약을 정확히 필터링함.
- `CANCEL_COMP`(취소 완료) 상태의 예약은 가용성 판단에서 제외되도록 쿼리 검증 완료.

## 4. 향후 계획
- **Phase 2 - Step 3**: 고객이 실제 예약 버튼을 눌렀을 때의 '예약 생성 플로우' 개발 예정 (`Reservation` 도메인 담당자와 협업).
