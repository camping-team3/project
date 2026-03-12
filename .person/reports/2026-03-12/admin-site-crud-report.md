# Report: 관리자용 Zone & Site CRUD 기능 개발 (Phase 2 - Step 1)

- **Reporter**: KimNaKim
- **Date**: 2026-03-12

## 1. 작업 요약
관리자가 캠핑장의 구역(Zone)과 개별 사이트(Site)를 체계적으로 관리할 수 있도록 백엔드 로직(Repository, Service)과 어드민 전용 UI(Mustache)를 구축함.

## 2. 상세 변경 사항
### 2.1 백엔드 도메인 레이어 (`domain/site/`)
- **ZoneRepository**: 신규 생성.
- **SiteRepository**: 성능 최적화를 위한 `fetch join` 쿼리(`findAllWithZone`) 추가.
- **SiteRequest**: 구역 및 사이트 등록을 위한 `ZoneSaveDTO`, `SiteSaveDTO` 정의.
- **ZoneService**: 구역 목록 조회, 상세 조회, 저장, 삭제 로직 구현.
- **SiteService**: 기존 뼈대 코드에 사이트 저장, 삭제, 구역 포함 전체 조회 로직 구현.

### 2.2 컨트롤러 및 뷰 레이어
- **AdminSiteController**: `/admin/site/management` 등 관리자 전용 API 및 폼 매핑 구현.
- **templates/admin/site/**: 
    - `management.mustache`: 구역/사이트 통합 관리 대시보드.
    - `zoneSaveForm.mustache`: 신규 구역 등록 폼.
    - `siteSaveForm.mustache`: 특정 구역 내 사이트 등록 폼.

## 3. 검증 결과
- `./gradlew.bat classes`를 통한 컴파일 및 의존성 체크 성공.
- `AdminInterceptor`에 의해 `/admin/**` 경로가 보호되므로 보안 원칙 준수 확인.
- `Zone`과 `Site`의 1:N 관계가 JPA 연관관계와 UI 구조에 정확히 반영됨.

## 4. 향후 계획
- **Phase 2 - Step 2**: 사용자용 메인 페이지에서 등록된 구역/사이트 목록을 조회하고, 날짜별 예약 가능 여부를 필터링하는 기능 개발 예정.
