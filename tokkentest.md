# 🏛️ [CORE] 디자인 토큰 표준 규칙 (Design Tokens)

본 규칙은 캠핑 ERP 프로젝트의 시각적 정체성을 유지하기 위한 절대적인 기준입니다. 모든 개발자는 UI 구현 시 이 규칙을 준수해야 합니다.

---

## 1. 변수 사용 대원칙 (The Law of Variables)
- **하드코딩 금지**: 색상 코드(#000 등)나 절대 수치(px)를 직접 입력하지 않는다.
- **변수 우선**: 항상 `src/main/resources/static/css/common.css`에 정의된 CSS 변수(`var(--fh-...)`)를 사용한다.
- **이름 기반 설계**: 색상 이름(Green)이 아닌 역할 이름(Primary)으로 사고한다.

---

## 2. 디자인 토큰 명세 (Token Specifications)

### 🎨 Color System (색상)
- **Primary**: `var(--fh-primary)` (#2c5926) - 브랜드 시그니처 컬러. 버튼, 링크, 로고 강조에 사용.
- **Secondary/Hover**: `var(--fh-primary-light)` - 강조 요소의 배경색, 호버(Hover) 시 배경색으로 사용.
- **Background**: `var(--fh-bg-light)` (#f6f7f6) - 전체 페이지의 기본 배경색.
- **Text Main**: `var(--fh-text-main)` (#0f172a) - 가독성이 중요한 제목 및 본문에 사용.
- **Text Muted**: `var(--fh-text-muted)` (#64748b) - 날짜, 카테고리, 부연 설명 등 보조 텍스트에 사용.
- **Border**: `var(--fh-border)` - 요소 간의 부드러운 구분을 위한 선 색상.

### 📐 Geometry & Depth (형태 및 깊이)
- **Corner Radius**: `var(--fh-radius)` (1rem / 16px) - 모든 카드, 버튼, 입력창에 공통 적용하여 부드러운 인상을 유지.
- **Shadows**: Bootstrap의 `.shadow-sm` (일반 목록), `.shadow` (카드), `.shadow-lg` (모달/배너)를 상황에 맞게 활용.

---

## 3. 타이포그래피 및 유틸리티 (Typography & Utilities)
- **Font**: `Plus Jakarta Sans`를 기본으로 하며, 제목에는 `.fw-black`(900)을 적극 활용한다.
- **Icons**: `Material Symbols Outlined`를 표준으로 한다.
- **Spacing**: Bootstrap의 4(1.5rem), 5(3rem) 단위를 사용하여 시원한 여백(Whitespace)을 확보한다.

---

## 🔍 구현 시 체크리스트
1. [ ] `#`으로 시작하는 색상 코드가 CSS 파일에 직접 들어가 있지는 않은가?
2. [ ] 모든 둥근 모서리에 `var(--fh-radius)`가 적용되었는가?
3. [ ] 텍스트의 중요도에 따라 `main`과 `muted` 토큰을 올바르게 구분했는가?
