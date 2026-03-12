# Report: 실시간 성수기(Peak) 토글 및 동적 요금 엔진 구현 (Phase 2 - Step 3-2)

- **Reporter**: KimNaKim
- **Date**: 2026-03-12

## 1. 작업 요약
관리자가 복잡한 날짜 설정 없이 구역별로 성수기 모드를 실시간으로 ON/OFF 할 수 있는 기능을 구현하고, 이에 따라 사용자 예약 요금이 즉시 변동되는 동적 요금 엔진을 구축함.

## 2. 상세 변경 사항
### 2.1 엔티티 및 DB (`Zone`, `testData.sql`)
- `Zone` 엔티티에 `isPeak`(boolean) 필드 및 `togglePeak()` 비즈니스 로직 추가.
- `testData.sql`에 `is_peak` 컬럼 기초 데이터(false) 반영.

### 2.2 비즈니스 로직 및 DTO (`SiteService`, `SiteResponse`)
- **요금 엔진**: `SiteResponse`의 DTO 생성자 내부에서 구역의 `isPeak` 상태를 체크하여 `peakPrice` 또는 `normalPrice`를 선택 적용하도록 로직 고도화.
- **DTO**: `MainDTO` 및 `ZoneUpdateFormDTO`에 `isPeak` 필드를 포함하여 UI 연동 지원.

### 2.3 컨트롤러 및 UI (`SiteController`, `Mustache`)
- **토글 API**: `SiteController`에 `/admin/zone/{id}/toggle-peak` POST 매핑 추가.
- **관리자 UI**: `management.mustache`에 실시간 상태 표시 배지 및 성수기 전환 버튼 구현.
- **사용자 UI**: `index.mustache`에 성수기 적용 구역 대상 🔥 배지 노출 및 자동 계산된 총 요금 표시.

## 3. 검증 결과
- `.\gradlew.bat classes` 빌드 성공.
- 관리자 페이지에서 토글 시 DB 값이 즉시 반전되며, 메인 페이지의 사이트 총 요금이 실시간으로 (박수 * 적용가) 계산되어 출력됨을 확인.
- Mustache의 조건부 렌더링(`{{#isPeak}}`)을 통해 상태에 따른 UI 분기가 정상 작동함을 확인.

## 4. 향후 계획
- **Phase 2 마무리**: `Site & Zone` 도메인의 모든 핵심 기능이 완료되었으므로, 다음 페이즈인 `Reservation`(예약 생성 및 관리)으로 전환 준비.
