# 📋 Forest Haven ERP Project TODO

## 🎨 디자인 보존 원칙 (Mandatory)

- 모든 Mustache 파일 수정 시 **기존 HTML 구조, CSS 클래스명, 인라인 스타일을 100% 보존**한다.
- 더미 데이터(예: "A-1 데크", "₩65,000")가 있던 자리에만 Mustache 문법(`{{variable}}`)을 적용한다.
- 신규 로직 추가 시 기존 레이아웃이 깨지지 않도록 `{{#sites}}...{{/sites}}` 등의 섹션을 기존 카드 디자인 블록에 정확히 입힌다.

## 🎯 예약 도메인 UI/UX 고도화

- [x] **Task 1: 예약 변경 완료 페이지 UI 통일**
  - [x] `reservation-change-done.mustache` 디자인을 `reservation-cancel-done.mustache` 스타일로 변경
  - [x] 정보 그리드(4:8 비율) 및 카드 레이아웃 적용
  - [x] 안내 배너 및 버튼 그룹 스타일 일관성 확보

- [x] **Task 2: 예약 목록 상세 보기 버튼 제거**
  - [x] `reservations.mustache` 내 중복 기능을 수행하는 '상세 보기' 버튼 삭제
  - [x] 카드 클릭(`onclick`) 기능으로의 일원화 확인
  - [x] 디자인 보존 원칙 준수 (기존 레이아웃 유지)
