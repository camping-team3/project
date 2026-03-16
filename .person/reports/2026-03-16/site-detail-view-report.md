Reporter: KimNaKim
Date: 2026-03-16

## 작업 요약
캠핑 사이트 상세 페이지(`/sites/{id}`)의 데이터를 동적으로 연동하였습니다.

## 변경 사항

### 1. Backend (Java)
- **`SiteResponse`**: 상세 페이지에 필요한 정보를 포함하는 `DetailDTO`를 구현했습니다. (최대 인원, 평시/성수기 요금 등 포함)
- **`SiteService`**: ID 기반의 사이트 조회 로직인 `findById()`를 구현했습니다. (존재하지 않을 경우 RuntimeException 발생)
- **`SiteController`**: `PathVariable`을 통해 ID를 받아 서비스를 호출하고, 조회된 데이터를 `Model`에 담아 `site/detail.mustache`로 전달하도록 수정했습니다.

### 2. Frontend (Mustache)
- **`site/detail.mustache`**: 
  - 제목: `{{site.siteName}} ({{site.zoneName}} 구역)`으로 동적 변경.
  - 별점: `{{site.rating}}` 반영.
  - 수용 인원: `최대 {{site.maxPeople}}인` 반영.
  - 1박 요금: `{{site.normalPrice}}원 ~` 반영.
  - 기타 하드코딩된 설명 일부를 동적 데이터에 맞게 수정했습니다.

## 검증 결과
- 메인 페이지에서 특정 사이트의 "상세보기" 버튼 클릭 시, 해당 사이트의 ID에 맞는 상세 정보가 화면에 올바르게 출력되는 것을 확인했습니다.
- 존재하지 않는 ID로 접근 시 예외 처리가 작동함을 확인했습니다.

## 비유로 설명하는 현재 상태
이전의 상세 페이지가 **"견본 주택에 붙어 있는 고정된 안내문"**이었다면, 이제는 **"각 호수마다 실제 면적과 가격이 적혀 있는 실시간 안내 태블릿"**으로 교체된 상태입니다. 손님이 어떤 사이트를 클릭하더라도 해당 사이트의 정확한 정보를 보여줄 수 있게 되었습니다.
