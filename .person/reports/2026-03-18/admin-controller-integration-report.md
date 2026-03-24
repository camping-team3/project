# 관리자 컨트롤러 통합 및 서버 기동 오류 해결 보고서

**날짜**: 2026-03-18
**작성자**: handikim (Git user.name 결과 반영)
**작업 단계**: 관리자 컨트롤러 통합 및 중복 매핑 제거

## 1. 작업 개요
서버 기동 시 발생한 `Ambiguous mapping` 오류를 해결하고, 프로젝트 구조에 맞춰 공지사항(`Notice`) 및 갤러리(`Gallery`)의 관리자 로직을 `AdminController`로 통합했습니다.

## 2. 변경 사항
- **AdminController.java**:
  - `NoticeService`, `GalleryService` 주입 추가.
  - 기존 `AdminNoticeController` 및 `AdminGalleryController`에 분산되어 있던 CRUD 로직(목록 조회, 저장, 수정, 삭제)을 모두 통합.
  - 경로를 `/admin/notices`, `/admin/galleries` 등 명시적 절대 경로로 설정하여 충돌 방지.
- **파일 삭제**:
  - `src/main/java/com/camping/erp/domain/notice/AdminNoticeController.java` 삭제.
  - `src/main/java/com/camping/erp/domain/gallery/AdminGalleryController.java` 삭제.

## 3. 검증 결과
- **서버 기동**: 중복 매핑(`GET /admin/galleries` 등)이 제거되어 Spring Application이 정상적으로 구동됨을 확인.
- **기능 작동**: 공지사항 및 갤러리의 어드민 페이지가 `AdminController`를 통해 정상적으로 연결됨.

## 4. 설명 (비유)
이전 상태는 마치 한 집(URL 경로)에 두 명의 집주인(컨트롤러)이 동시에 살면서 "내 집이다"라고 우기는 상황이었습니다. 이로 인해 손님(요청)이 올 때 누구에게 가야 할지 몰라 서버가 멈춰버린 것입니다. 이번 작업을 통해 집주인을 한 명으로 정리하고, 모든 열쇠를 `AdminController`라는 통합 관리인에게 맡겨 집안의 모든 관리가 한곳에서 이루어지도록 정리했습니다.

---
보고서 작성 완료.
