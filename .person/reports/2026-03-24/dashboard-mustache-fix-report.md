# Report: 관리자 대시보드 Mustache 문법 오류 수정 및 고도화

- **Date**: 2026-03-24
- **Reporter**: parkcoding (Gemini CLI)
- **Task**: 대시보드 접속 시 발생하던 MustacheParseException 해결 및 DTO 개선

## 📝 에러 발생 및 원인
관리자 대시보드(`dashboard.mustache`) 작업 후 `/admin` 접속 시 다음과 같은 파싱 에러가 발생했습니다.
- **Error**: `MustacheParseException: Section close tag with mismatched open tag 'isCancelReq' != 'isChangeReq'`
- **Cause**: 예약 목록 행 클릭 시 분기 처리를 위해 작성한 `{{^isChangeReq}}{{^isCancelReq}}detail{{/isCancelReq}}{{/isChangeReq}}` 구문이 Mustache 파서에서 태그 불일치로 인식되었습니다.

## 🛠️ 조치 방법 및 변경 사항
템플릿의 복잡도를 낮추고 유지보수성을 높이기 위해 백엔드 DTO에 판단 로직을 위임했습니다.

1. **AdminResponse.java (DTO)**:
   - `ReservationListDTO` 클래스에 `isDetailView()` 메서드 추가.
   - 변경/취소 요청이 아닌 모든 상태(PENDING, CONFIRMED, COMPLETED 등)를 통합 판단하도록 구현.

2. **dashboard.mustache (View)**:
   - 행 클릭(`onclick`) 이벤트의 URL 생성 로직에서 중첩된 부정 섹션(`^`)을 제거.
   - `{{#isDetailView}}detail{{/isDetailView}}` 구문을 적용하여 문법적 안정성 확보.

## ✅ 검증 결과
- **정상 렌더링**: 대시보드 진입 시 더 이상 서버 에러가 발생하지 않음.
- **분기 이동 확인**: 
  - 변경 요청 행 클릭 -> `/change-detail` 이동 확인
  - 취소 요청 행 클릭 -> `/cancel-detail` 이동 확인
  - 일반 예약 행 클릭 -> `/detail` 이동 확인
