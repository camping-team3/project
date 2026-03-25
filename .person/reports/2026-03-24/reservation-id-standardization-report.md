# Report: 전사 예약 번호 포맷 표준화

- **Date**: 2026-03-24
- **Reporter**: parkcoding
- **Task**: 고객 및 관리자 페이지의 예약 번호 출력 방식을 `RESERV-{{id}}`로 통일

## 📝 작업 요약
시스템의 일관성을 높이기 위해 제각각이었던 예약 번호 포맷(#RSV, RSV-, ID 단독 노출 등)을 하나의 표준 포맷인 `RESERV-{{id}}`로 통일했습니다.

## 🛠️ 변경 사항
1. **고객용 페이지**:
   - `complete.mustache`: `#RSV-{{id}}` → `RESERV-{{id}}`
2. **관리자용 페이지**:
   - `list.mustache`: `reservationIdDisplay` 등 서버 생성 문자열 → `RESERV-{{id}}` (강조 스타일 적용)
   - `detail.mustache`: `RSV-{{id}}` → `RESERV-{{id}}`
   - `change-detail.mustache`: `RSV-{{id}}` → `RESERV-{{id}}`
   - `cancel-detail.mustache`: `RSV-{{id}}` → `RESERV-{{id}}`

## ✅ 검증 결과
- 모든 대상 페이지에서 예약 번호가 `RESERV-` 접두사와 함께 일관되게 출력됨을 확인했습니다.
- 관리자 목록 페이지에서는 예약 번호에 `text-primary fw-bold` 스타일을 추가하여 가시성을 확보했습니다.
- 각 도메인(일반 예약, 변경 요청, 취소 요청)에 상관없이 동일한 식별 포맷을 사용하여 사용자 혼선을 방지했습니다.
