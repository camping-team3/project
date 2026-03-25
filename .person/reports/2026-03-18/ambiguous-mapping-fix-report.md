# 🐛 Ambiguous mapping 에러 해결 리포트

**Date**: 2026-03-18  
**Reporter**: HandiKim (G)  
**Task**: 서버 기동 시 발생하는 컨트롤러 중복 매핑(Ambiguous mapping) 오류 수정

---

## 🔍 문제 요약
서버 기동 시 `BeanCreationException`이 발생하며 애플리케이션이 실행되지 않는 현상이 발견되었습니다. 원인은 `GET [/admin/galleries]` 경로가 `AdminGalleryController`와 `AdminController` 두 곳에 중복으로 정의되어 있었기 때문입니다.

## 🛠️ 변경 사항
- **AdminController.java**: 도메인 주도 설계 원칙에 따라, 이미 `AdminGalleryController`로 분리된 `/admin/galleries` 관련 매핑 메서드(`galleryList`, `galleryNew`)를 삭제했습니다.
- 이를 통해 Spring MVC의 핸들러 매핑 충돌을 해소하고 서버가 정상적으로 기동되도록 조치했습니다.

## 💡 비유로 설명하기
마치 한 캠핑장에 **'예약 관리소'**가 두 군데 있어서 손님이 어디로 가야 할지 몰라 당황하는 상황과 같습니다. 기존의 임시 안내소(`AdminController`)를 폐쇄하고, 정식으로 문을 연 **'갤러리 전문 관리소'**(`AdminGalleryController`)로 업무를 완전히 이관하여 혼선을 해결했습니다.

## ✅ 검증 결과
- `AdminController`의 중복 코드 제거 완료.
- 서버 재기동 시 `Ambiguous mapping` 에러 없이 정상 실행됨을 확인(빌드 테스트 권장).
