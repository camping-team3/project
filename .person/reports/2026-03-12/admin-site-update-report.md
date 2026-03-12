# Report: 관리자용 Zone & Site 수정(Update) 기능 개발 (Phase 2 - Step 3-1)

- **Reporter**: KimNaKim
- **Date**: 2026-03-12

## 1. 작업 요약
관리자가 캠핑장의 구역(Zone)과 개별 사이트(Site) 정보를 사후에 수정할 수 있도록 엔티티 수정 로직, 서비스 메서드, 어드민 수정 폼(Mustache)을 구축함.

## 2. 상세 변경 사항
### 2.1 엔티티 레이어 (`Zone`, `Site`)
- 각 엔티티에 비즈니스 로직에 따른 `update` 메서드 추가 (필드 캡슐화 및 더티 체킹 활용).

### 2.2 DTO 및 서비스 레이어
- **DTO**: `ZoneUpdateDTO`, `SiteUpdateDTO` (수정 요청용) 및 `ZoneUpdateFormDTO`, `SiteUpdateFormDTO` (수정 폼 데이터 전달용) 추가.
- **패턴 준수**: 팀장님의 요구사항에 따라 수정 폼 DTO는 엔티티 객체를 매개변수로 받는 생성자를 통해 필드를 초기화함.
- **Service**: `ZoneService`와 `SiteService`에 각각 `@Transactional` 어노테이션이 적용된 `update` 메서드 구현.

### 2.3 컨트롤러 및 뷰 레이어
- **SiteController**: 통합된 컨트롤러 내부에 수정 폼 조회(`GET`) 및 수정 처리(`POST`) 핸들러 7종 추가.
- **Mustache**: 
    - `management.mustache`: 각 구역 및 사이트 옆에 '수정' 버튼 배치.
    - `zoneUpdateForm.mustache`, `siteUpdateForm.mustache`: 기존 데이터가 채워진 상태의 수정 폼 페이지 구축.

## 3. 검증 결과
- `./gradlew.bat classes` 컴파일 성공.
- 수정 로직에서 JPA 더티 체킹이 정상 작동하도록 서비스 레이어에 트랜잭션 처리가 완료됨을 확인.
- 수정 폼 로딩 시 DTO 생성자를 통해 엔티티의 최신 데이터가 안전하게 바인딩됨을 확인.

## 4. 향후 계획
- **Phase 2 - Step 3-2**: 요금 계산 로직 고도화(성수기 체크 등) 및 검색 UX 보강을 통해 Site 도메인 최종 마무리 예정.
