# Design Rules

> **[CAUTION]** 본 문서는 캠핑 ERP 프로젝트의 시각적 일관성과 UI 개발 표준을 정의합니다. (최종 업데이트: 2026-03-19)

---

## 🚨 UI 작업 시작 전 필수 절차 (Mandatory Procedure)

모든 UI(Mustache, HTML, CSS) 구현 또는 수정 작업에 들어가기 전, AI는 반드시 다음 과정을 거쳐야 한다.

1. **레퍼런스 확인**: `.ai/references/` 폴더 내에 해당 작업과 연관된 가이드가 있는지 먼저 확인한다.
2. **사용자 확인 요청**: 작업을 시작하기 전 사용자에게 다음과 같이 질문한다.
   > "현재 UI 구현을 위해 어떤 레퍼런스(`.ai/references/` 내 파일)를 참고하면 좋을까요? 아니면 현재 페이지의 스타일을 분석해서 작업할까요?"
3. **목업 확인 (Mockup Preview)**: 실제 파일에 코드를 적용하기 전, 다음 두 가지 형식을 채팅창에 반드시 제시한다.
   - **레이아웃 구조 (Box Diagram)**: 구현하려는 UI의 구조에 맞게 텍스트 박스를 활용하여 시각화한다. **(아래 예시는 '보고 형식'에 대한 가이드일 뿐이며, 실제 상자 구조는 구현할 페이지의 디자인에 따라 자유롭게 구성한다.)**
     ```text
     +---------------------------------------------------------+
     | [Header Navbar] (기존 레이아웃)                            |
     +---------------------------------------------------------+
     |                                                         |
     |  +---------------------------------------------------+  |
     |  | [컴포넌트 A]                                       |  |
     |  |---------------------------------------------------|  |
     |  | [컴포넌트 B]                                       |  |
     |  +---------------------------------------------------+  |
     |                                                         |
     +---------------------------------------------------------+
     | [Footer] (기존 레이아웃)                                  |
     +---------------------------------------------------------+
     ```
   - **전체 소스 코드 (Full Code)**: Mustache 템플릿의 전체 소스 코드를 마크다운 코드 블록으로 제시한다.

4. **최종 승인 후 진행**: 사용자가 목업을 확인하고 "좋아", "진행해" 등 최종 승인을 한 경우에만 실제 파일(`replace`, `write_file`)을 수정한다.

---

## 1. 디자인 토큰 전략 (Design Token Strategy)
... (기존 내용 동일)
모든 UI 작업 시 하드코딩된 값 대신 `static/css/common.css`에 정의된 CSS 변수(`var(--fh-...)`)를 반드시 사용한다.

### 1.1 색상 시스템 (Color System)
- **Primary**: `var(--fh-primary)` (#2c5926) - 브랜드 시그니처, 메인 버튼, 핵심 강조.
- **Secondary**: `var(--fh-primary-light)` - 강조 요소 배경, 호버 배경.
- **Background**: `var(--fh-bg-light)` (#f6f7f6) - 전체 페이지 기본 배경색.
- **Text Main**: `var(--fh-text-main)` (#0f172a) - 메인 타이틀 및 본문 텍스트.
- **Text Muted**: `var(--fh-text-muted)` (#64748b) - 보조 설명, 날짜, 힌트성 텍스트.
- **Border**: `var(--fh-border)` - 컴포넌트 간 부드러운 구분선.

### 1.2 레이아웃 및 형태 (Geometry)
- **Radius**: `var(--fh-radius)` (1rem/16px) - 모든 카드, 버튼, 입력 필드에 공통 적용.
- **Shadow**: Bootstrap `.shadow-sm` (일반), `.shadow` (카드), `.shadow-lg` (강조/배너) 표준 준수.

---

## 2. 공통 컴포넌트 표준 (Component Standards)

### 2.1 카드 컴포넌트 (.fh-card)
- **구조**: 정보 단위를 담는 기본 컨테이너로 활용.
- **스타일**: `background: white; border-radius: var(--fh-radius); border: 1px solid var(--fh-border);` 기본 적용.

### 2.2 버튼 컴포넌트 (.btn)
- **Primary**: `.btn-primary` - 핵심 행동(등록, 예약, 저장).
- **Secondary**: `.btn-outline` - 취소, 목록 이동 등 보조 행동.
- **Icon Button**: `.btn-icon` 또는 `.btn-icon-square` - 호버 시 배경색 변화 효과 포함.

### 2.3 테이블 컴포넌트 (.fh-table)
- **구조**: 관리자 및 사용자 목록형 데이터 표시용.
- **헤더**: `.bg-light` 배경, `.text-xs fw-bold` 폰트 스타일 적용 필수.
- **행**: `tr:hover` 시 `var(--fh-primary-light)` 배경색 적용으로 시각적 피드백 제공.

---

## 3. 타이포그래피 및 유틸리티 (Typography & Utilities)

- **Font Family**: `Plus Jakarta Sans`를 기본 서체로 사용한다.
- **Font Weight**: 대제목 및 강조 시 `.fw-black` (900)을 적극 권장한다.
- **Icons**: Google `Material Symbols Outlined`를 표준 아이콘 시스템으로 사용한다.
- **Spacing**: Bootstrap Utility(`p-4`, `mb-5` 등)를 사용하되, 섹션 간 여백은 가급적 `py-5` 수준의 여유를 둔다.

---

## 4. UI 개발 가이드라인

- **일관성**: 새로운 페이지 제작 시 기존의 `Index`, `Notice/Gallery List`, `Admin List` 페이지의 스타일을 계승한다.
- **아이콘 정렬**: 아이콘과 텍스트가 함께 쓰일 경우 간격을 적절히(예: `.gap-2`) 유지하고 수직 정렬(`align-middle`)에 유의한다.
- **반응형**: Bootstrap 그리드 시스템과 컨테이너(`container`, `container-fluid`)를 활용하여 다양한 해상도에 대응한다.
