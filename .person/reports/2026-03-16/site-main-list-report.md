Reporter: KimNaKim
Date: 2026-03-16

## 작업 요약
메인 페이지(`index.mustache`)의 캠핑 사이트 목록을 정적 하드코딩 데이터에서 DB 기반의 동적 데이터로 전환하였습니다.

## 변경 사항

### 1. Backend (Java)
- **`SiteRepository`**: N+1 문제를 방지하기 위해 `Zone` 엔티티를 Fetch Join으로 한 번에 가져오는 `findAllWithZone()` 메서드를 추가했습니다.
- **`SiteResponse`**: 화면 렌더링에 최적화된 `ListDTO`를 구현했습니다. (사이트명, 구역명, 가격, 기본 별점 포함)
- **`SiteService`**: Repository에서 엔티티 목록을 가져와 DTO 목록으로 변환하는 `findAll()` 메서드를 구현했습니다.
- **`AuthController`**: 메인 경로(`/`) 호출 시 `SiteService`를 통해 데이터를 조회하고, `model.addAttribute("sites", sites)`를 통해 뷰로 전달하도록 수정했습니다.

### 2. Frontend (Mustache) - **[중요 변경사항]**
- **`index.mustache`**: 기존에 하드코딩되어 있던 4개의 사이트 카드 섹션을 삭제하고, Mustache의 반복문인 `{{#sites}} ... {{/sites}}` 구문을 사용하여 동적으로 렌더링되도록 수정했습니다.
- **데이터 바인딩**: DTO의 필드(`{{siteName}}`, `{{zoneName}}`, `{{price}}`, `{{id}}`, `{{rating}}`)를 템플릿의 각 위치에 정확히 매핑했습니다.
- **상세보기 링크**: `onclick="location.href='/sites/{{id}}'"`와 같이 동적 ID를 사용하도록 변경했습니다.

## 검증 결과
- `SiteRepository` 쿼리 실행 시 `join fetch`를 통해 `zone_tb`와 정상적으로 조인됨을 확인했습니다.
- 메인 페이지 접속 시 DB에 저장된 `Site` 데이터 개수만큼 카드가 동적으로 생성되는 것을 확인했습니다.
- 별점(Rating)은 아직 리뷰 도메인이 구현되지 않아 `5.0`으로 고정 처리해 두었습니다. (추후 연동 예정)

## 비유로 설명하는 현재 상태
이전의 메인 페이지가 **"음식 사진만 붙어있는 고정된 메뉴판"**이었다면, 이번 작업을 통해 **"주방(DB)의 재료 상황에 따라 실시간으로 업데이트되는 디지털 전광판"**으로 교체한 것과 같습니다. 이제 주방에서 새로운 사이트를 추가하면 메인 화면에도 자동으로 나타나게 됩니다.
