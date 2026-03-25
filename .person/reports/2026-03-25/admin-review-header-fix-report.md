# 관리자 리뷰 관리 페이지 헤더 홈 버튼 복구 리포트

**Reporter:** Gemini-CLI
**Date:** 2026-03-25
**Task:** 관리자 리뷰 관리 페이지(`admin/review/list.mustache`) 헤더 가시성 확보

## 1. 문제 분석
- **현상**: 관리자 리뷰 관리 페이지에서 다른 관리자 페이지와 달리 상단 헤더(홈으로 돌아가기 버튼 포함)가 보이지 않음.
- **원인**: 해당 템플릿 파일 내 `<style>` 태그에 `.admin-header`를 `display: none !important;` 처리하고 `.admin-content`의 패딩을 제거하는 코드가 포함되어 있었음.

## 2. 변경 사항
- **`src/main/resources/templates/admin/review/list.mustache`**:
  - 공통 헤더를 숨기던 CSS 규칙 제거.
  - 헤더 영역 확보를 위해 강제로 설정된 `padding-top: 0` 규칙 제거.

## 3. 검증 결과
- `layout/admin-header.mustache`에 정의된 "홈으로 돌아가기" 버튼이 이제 리뷰 관리 페이지에서도 정상적으로 노출됨.
- 다른 관리자 메뉴와 일관된 UI/UX 제공.

## 4. 비유로 설명하기
안내 데스크로 가는 이정표를 실수로 가려두었던 가림막을 치운 것과 같습니다. 이제 관리자분들이 리뷰를 검토하다가도 언제든지 상단 버튼을 통해 메인 화면으로 돌아가실 수 있게 되었습니다.
