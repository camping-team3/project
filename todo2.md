# 🎨 디자인 시스템 가이드: 토큰 및 컴포넌트 룰

이 문서는 캠핑 ERP 프로젝트의 UI 일관성을 유지하고 팀원 간 효율적인 협업을 위해 작성되었습니다. 모든 UI 작업 시 아래 정의된 규칙을 우선적으로 준수합니다.

---

## 1. 디자인 토큰 (Design Tokens)
디자인 토큰은 UI 스타일의 가장 작은 단위(색상, 간격, 폰트 등)를 변수화한 것입니다. 우리 프로젝트는 **Vanilla CSS 변수(Custom Properties)**를 사용하여 관리합니다.

### 🎨 Color (색상)
- `--fh-primary`: 서비스의 메인 테마 색상 (숲의 느낌을 주는 진한 초록/청록 계열)
- `--fh-secondary`: 보조 색상 (회색조, 구분선 등)
- `--fh-danger`: 경고, 삭제, 에러 시 사용
- `--fh-bg-light`: 배경용 연한 회색/화이트 계열
- `--fh-white`: 순수 화이트 (#FFFFFF)

### 📏 Spacing & Radius (간격 및 곡률)
- `--spacing-xs`: 4px (아주 작은 요소 간격)
- `--spacing-sm`: 8px (내부 여백, 좁은 간격)
- `--spacing-md`: 16px (일반적인 여백, 컴포넌트 간격)
- `--spacing-lg`: 24px (섹션 간 여백)
- `--radius-sm`: 4px (체크박스, 작은 버튼)
- `--radius-md`: 8px (일반적인 카드, 버튼)
- `--radius-lg`: 12px~ (모달, 큰 카드 섹션)

### 🔠 Typography (글꼴)
- `--font-size-base`: 16px (기본 본문)
- `--font-size-sm`: 14px (부가 설명, 작은 텍스트)
- `--font-size-lg`: 20px (소제목)
- `--font-size-xl`: 24px+ (대제목)
- `--font-weight-bold`: 700
- `--font-weight-medium`: 500

---

## 2. 컴포넌트 룰 (Component Rules)
컴포넌트는 토큰들을 조합하여 만든 재사용 가능한 UI 단위입니다.

### 🔳 Card (카드 컴포넌트)
- **클래스명**: `.fh-card`
- **규칙**: 
  - 배경은 항상 `--fh-white`이며, 테두리는 아주 연한 `--fh-secondary` 또는 `--spacing-md` 수준의 그림자를 가짐.
  - 내부 여백(Padding)은 기본적으로 `--spacing-md` 이상을 유지함.
  - 모서리 곡률은 `--radius-md`를 적용함.

### 🔘 Button (버튼 컴포넌트)
- **클래스명**: `.btn-primary`, `.btn-outline`, `.btn-icon`
- **규칙**:
  - `btn-primary`: 배경색 `--fh-primary`, 글자색 `--fh-white`.
  - 호버(Hover) 시 색상이 10% 정도 어두워지거나 투명도가 조절되어야 함.
  - 아이콘과 텍스트가 함께 쓰일 경우 간격은 `--spacing-sm`을 유지함.

### 📝 Table (표 컴포넌트)
- **클래스명**: `.fh-table`
- **규칙**:
  - 테이블 헤더(`thead`)는 연한 회색 배경(`--fh-bg-light`)을 사용하며 텍스트는 `--font-size-sm`의 볼드체 적용.
  - 각 행(`tr`) 사이에는 1px의 실선 구분선이 들어감.

---

## 3. 협업 지침 (How to Use)
1. **변수 사용 우선**: 하드코딩된 값(예: `#123456`) 대신 반드시 CSS 변수(`var(--fh-primary)`)를 사용합니다.
2. **공통 CSS 위치**: 모든 토큰은 `static/css/common/variables.css`에 정의되어 있어야 합니다.
3. **새 컴포넌트 추가 시**: 기존에 정의된 컴포넌트로 구현이 불가능할 경우에만 새로운 컴포넌트를 정의하고 이 문서에 업데이트합니다.
