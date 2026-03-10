<!-- Parent: ../../../../../.ai/AI-CONTEXT.md -->

# templates/

## 목적
Mustache 기반의 서버 사이드 렌더링(SSR) 뷰 템플릿.

## 주요 디렉토리
| 디렉토리 | 설명 |
|--------|------|
| `layout/` | `header.mustache`, `footer.mustache` 등 공통 레이아웃 |
| `main/` | 메인 페이지 및 캠핑 사이트 소개 |
| `res/` | 예약 진행 페이지 (날짜 선택, 결제) |
| `auth/` | 로그인, 회원가입 폼 |
| `mypage/` | 내 예약 내역, 리뷰 작성 폼 |
| `admin/` | 관리자 대시보드 및 운영 관리 페이지 |

## AI 작업 지침
- **Partial 활용**: 모든 페이지는 `{{> layout/header}}`와 `{{> layout/footer}}`를 포함하여 일관된 디자인을 유지한다.
- **데이터 바인딩**: Mustache는 Logic-less 템플릿이므로, 컨트롤러에서 필요한 모든 데이터를 가공하여 `Model`에 담아 전달한다.
- **스타일**: Bootstrap 5 클래스를 기본으로 사용하며, 필요한 커스텀 스타일은 `static/css/common.css`에 정의한다.

## 의존성
- 외부: Mustache Template Engine, Bootstrap 5
