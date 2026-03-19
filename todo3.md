# 📋 프로젝트 UI/UX 자산 분석 (디자인 토큰 & 컴포넌트)

본 문서는 현재 캠핑 ERP 프로젝트(`common.css` 기반)에 적용된 디자인 시스템을 분석한 결과입니다. 향후 `design-rule.md` 작성 및 UI 구현의 기초 자료로 활용됩니다.

---

## 1. 디자인 토큰 (Design Tokens)

### 🎨 Color System (색상)
- **Brand Primary**
  - `--fh-primary`: `#2c5926` (숲의 느낌을 주는 메인 그린)
  - `--fh-primary-light`: `rgba(44, 89, 38, 0.1)` (연한 배경, 호버 효과)
  - `--fh-primary-dark`: `#1e3d1a` (버튼 호버 등 강조색)
- **Neutral & Background**
  - `--fh-bg-light`: `#f6f7f6` (전체 페이지 배경색)
  - `--fh-white`: `#FFFFFF`
  - `--fh-border`: `rgba(44, 89, 38, 0.08)` (부드러운 구분선)
- **Typography Color**
  - `--fh-text-main`: `#0f172a` (본문 및 제목)
  - `--fh-text-muted`: `#64748b` (설명, 날짜 등 보조 텍스트)

### 📐 Shape & Depth (형태 및 깊이)
- **Radius (곡률)**: `--fh-radius: 1rem` (16px) - 전체적으로 둥글고 부드러운 느낌 강조
- **Shadow (그림자)**: `--fh-shadow` 및 Bootstrap 오버라이드(`.shadow-sm`, `.shadow`) 사용
- **Typography**: `Plus Jakarta Sans`, `sans-serif` 사용

---

## 2. 공통 컴포넌트 룰 (Component Rules)

### 🔳 Card (.fh-card)
- **특징**: 배경 `--fh-white`, 테두리 `--fh-border`, 곡률 `--fh-radius`.
- **활용**: 대시보드 아이템, 공지사항/갤러리 상세 내용 박스.

### 🔘 Buttons
- **.btn-primary**: 배경 `--fh-primary`, 글자 흰색, 굵은 폰트(700).
- **.btn-outline**: 흰색 배경, 초록색 테두리, 초록색 글자.
- **.btn-icon-square**: 회색 배경의 정사각형 버튼, 호버 시 `--fh-primary-light`로 변경.

### 📝 Tables (.fh-table)
- **헤더**: 연한 배경(`--fh-bg-light`), 작은 글씨(0.75rem), 대문자, 볼드.
- **데이터**: 행(Row) 사이 구분선 존재, 호버 시 배경색 변화 효과.

### 🧭 Navigation & Layout
- **.fh-navbar**: 상단 고정, 백드롭 블러(10px) 효과, 좌우 여백(5rem).
- **.admin-sidebar**: 좌측 고정(280px), 흰색 배경, 우측 구분선.
- **.fh-sidebar-nav**: 메뉴 아이템 간격(0.25rem), 호버 및 활성 상태(`active`) 처리.

---

## 3. UI 구현 시 주의사항
1. **아이콘**: Google Material Symbols Outlined 사용 (`.material-symbols-outlined`).
2. **반응형**: `@media (max-width: 991px)` 등을 통한 모바일 최적화 고려.
3. **유틸리티**: `.fw-black`(900), `.text-xs`(0.75rem), `.tracking-wider` 등의 커스텀 유틸리티 클래스 활용.
