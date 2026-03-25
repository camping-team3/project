# 마이페이지 예약 목록 이미지 출력 오류 수정 리포트

**Reporter:** Gemini-CLI
**Date:** 2026-03-25
**Task:** 마이페이지 예약 목록(`mypage/reservations`) 이미지 경로 정상화

## 1. 문제 분석
- **현상**: 마이페이지 예약 목록 및 상세 페이지에서 캠핑 사이트 이미지가 엑스박스(broken image)로 표시됨.
- **원인**: `ReservationResponse.java` 내 `ListDTO`, `DetailDTO`, `ChangeFormDTO`의 `fromEntity` 메서드에서 존재하지 않는 임시 이미지 경로(`/upload/...`)를 하드코딩하여 사용 중이었음.

## 2. 변경 사항
- **`src/main/java/com/camping/erp/domain/reservation/ReservationResponse.java`**:
  - 하드코딩된 `siteImage` 경로를 실제 프로젝트 내에 존재하는 기본 이미지 경로(`/images/camping_review2.jpg`)로 변경.
  - 추후 Site/Zone 엔티티에 이미지 연관관계가 확정되면 해당 데이터를 사용하도록 TODO 주석 유지 및 설명 보완.

## 3. 검증 결과
- `ReservationResponse.java` 수정 완료.
- `reservations.mustache`에서 `{{siteImage}}` 변수를 통해 정상적으로 존재하는 이미지를 참조하도록 변경됨.
- 컴파일 및 런타임 시 해당 DTO를 사용하는 모든 페이지(목록, 상세, 변경 폼)에서 이미지가 정상 출력될 것으로 예상됨.

## 4. 비유로 설명하기
마치 식당 메뉴판에 사진이 없어서 손님들이 음식을 고르기 힘들었던 상황이었습니다. 실제 음식 사진(DB 연동 이미지)이 준비되기 전까지, 식당의 가장 대표적인 풍경 사진(기본 이미지)으로 메뉴판을 깔끔하게 채워 넣어 손님들이 기분 좋게 메뉴를 고를 수 있도록 조치한 것과 같습니다.
