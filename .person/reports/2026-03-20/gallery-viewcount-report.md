# 🖼️ 포토 갤러리 조회수 기능 구현 보고서

**날짜**: 2026-03-20
**작성자**: xogurrh012
**작업 단계**: Phase 3 - 기능 고도화

## 📝 작업 요약
사용자가 포토 갤러리의 상세 내용을 확인할 때마다 조회수가 1씩 증가하는 기능을 구현하였습니다. 이는 마치 박물관에 전시된 사진을 관람객이 볼 때마다 카운터가 올라가는 것과 같습니다.

## 🛠️ 변경 사항

### 1. Gallery 엔티티 (`Gallery.java`)
- `increaseViewCount()` 메서드를 추가하였습니다.
- 이 메서드는 호출될 때마다 해당 게시글의 `viewCount` 필드를 1씩 증가시킵니다.

### 2. Gallery 서비스 (`GalleryService.java`)
- `findByIdWithViewCount(Long id)` 메서드를 새롭게 추가하였습니다.
- `@Transactional` 어노테이션을 사용하여 데이터베이스에 변경 사항이 안전하게 저장되도록 하였습니다.
- 사용자용 상세 조회에서만 호출되므로, 관리자의 수정 화면 접근 시에는 조회수가 올라가지 않도록 분리 설계하였습니다.

### 3. Gallery 컨트롤러 (`GalleryController.java`)
- 사용자용 상세 조회 API (`/galleries/{id}`)에서 기존의 `findById` 대신 새롭게 만든 `findByIdWithViewCount`를 호출하도록 변경하였습니다.

## ✅ 검증 결과
- 포토 갤러리 상세 페이지(`gallery/detail.mustache`)에 접속할 때마다 "조회수 {{gallery.viewCount}}" 부분이 정상적으로 증가하는 로직을 백엔드에서 완성하였습니다.
- 기존에 이미 템플릿에 `{{gallery.viewCount}}`가 적용되어 있었으므로, 별도의 UI 수정 없이 즉시 반영됩니다.
