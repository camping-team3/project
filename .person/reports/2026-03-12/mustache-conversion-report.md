# Mustache 변환 작업 리포트

- **날짜**: 2026-03-12
- **작업자**: AI (Claude Sonnet 4.6)
- **작업 유형**: HTML → Mustache SSR 템플릿 변환

---

## 요약

`zBackup/html/` 내 37개 HTML 프로토타입 파일을 Spring Boot Mustache SSR 구조에 맞게 변환하였다.
병합 2건(3→1, 2→1) 처리로 최종 **35개 .mustache 파일** 생성.

---

## 생성 파일 목록 (35개)

### Layout Partials (4개)
| 파일 | 작업 | 원본 |
|------|------|------|
| `templates/layout/header.mustache` | 교체 | 35메인허브.html nav 기반 |
| `templates/layout/footer.mustache` | 교체 | 35메인허브.html footer 기반 |
| `templates/layout/admin-header.mustache` | 신규 | 30관리자대시보드.html sidebar+header |
| `templates/layout/admin-footer.mustache` | 신규 | 공통 닫는 태그 |

### 사용자 페이지 (17개)
| 파일 | 원본 HTML | 레이아웃 |
|------|-----------|--------|
| `templates/index.mustache` | 35+16+37 병합 | header/footer |
| `templates/auth/login.mustache` | 15로그인.html | standalone |
| `templates/auth/join.mustache` | 22회원가입.html | standalone |
| `templates/site/detail.mustache` | 27숙소상세.html | standalone |
| `templates/reservation/new.mustache` | 21실시간예약.html | header/footer |
| `templates/reservation/payment.mustache` | 29결제하기화면.html | header/footer |
| `templates/reservation/complete.mustache` | 13예약완료.html | standalone |
| `templates/mypage/reservation-change.mustache` | 02예약변경신청.html | header/footer |
| `templates/mypage/reservation-change-done.mustache` | 05예약변경신청완료.html | header/footer |
| `templates/mypage/reservation-cancel.mustache` | 10예약취소신청.html | header/footer |
| `templates/mypage/reservation-cancel-done.mustache` | 03예약취소신청완료.html | header/footer |
| `templates/notice/list.mustache` | 18공지사항.html | header/footer |
| `templates/gallery/list.mustache` | 26포토갤러리.html | header/footer |
| `templates/qna/list.mustache` | 31+36 병합 | header/footer |
| `templates/qna/new.mustache` | 32질문하기페이지.html | header/footer |
| `templates/review/new.mustache` | 11리뷰작성.html | standalone |

### 관리자 페이지 (14개)
| 파일 | 원본 HTML | 레이아웃 |
|------|-----------|--------|
| `templates/admin/dashboard.mustache` | 30관리자대시보드.html | admin-header/footer |
| `templates/admin/stat.mustache` | 14관리자통계.html | admin-header/footer |
| `templates/admin/reservation/list.mustache` | 19관리자예약관리목록.html | admin-header/footer |
| `templates/admin/reservation/change-detail.mustache` | 23관리자_예약변경상세정보.html | admin-header/footer |
| `templates/admin/reservation/cancel-detail.mustache` | 24관리자_취소요청상세정보.html | admin-header/footer |
| `templates/admin/reservation/reject-modal.mustache` | 09거절사유입력.html | standalone (모달) |
| `templates/admin/user/list.mustache` | 25관리자사용자목록.html | admin-header/footer |
| `templates/admin/user/detail.mustache` | 17관리자_고객상세.html | admin-header/footer |
| `templates/admin/notice/list.mustache` | 07공지사항관리.html | admin-header/footer |
| `templates/admin/notice/new.mustache` | 12공지사항등록.html | admin-header/footer |
| `templates/admin/gallery/list.mustache` | 06포토갤러리관리.html | admin-header/footer |
| `templates/admin/gallery/new.mustache` | 20관리자포토갤러리작성.html | admin-header/footer |
| `templates/admin/qna/list.mustache` | 33관리자Q&A관리.html | admin-header/footer |
| `templates/admin/qna/answer.mustache` | 34고객문의답변달기.html | admin-header/footer |
| `templates/admin/site/season.mustache` | 08성수기비수기관리.html | admin-header/footer |

---

## 변환 규칙 적용 내역

### 레이아웃 패턴

**사용자 페이지**:
```mustache
{{> layout/header}}
<main>
  ...페이지 콘텐츠...
</main>
{{> layout/footer}}
```

**관리자 페이지**:
```mustache
{{> layout/admin-header}}
<div class="p-4 p-lg-5">
  ...페이지 콘텐츠...
</div>
{{> layout/admin-footer}}
```

**Standalone 페이지** (login, join, site/detail, review/new, reservation/complete, admin/reservation/reject-modal):
- 고유한 헤더/레이아웃 구조 → 공통 partial 미사용, 전체 HTML 구조 유지

### Mustache 세션 조건부 (index.mustache, header.mustache)
```mustache
{{#sessionUser}}
    {{#isAdmin}}관리자페이지 버튼{{/isAdmin}}
    {{^isAdmin}}마이페이지 버튼{{/isAdmin}}
{{/sessionUser}}
{{^sessionUser}}로그인 버튼{{/sessionUser}}
```

### URL 변환 (HTML → Spring URL)
- `15로그인.html` → `/login-form`
- `22회원가입.html` → `/join-form`
- `30관리자대시보드.html` → `/admin`
- `19관리자예약관리목록.html` → `/admin/reservations`
- 기타 전체 매핑: 계획 문서 참조

### 제거 항목
- 페이지별 `<link href="css/PAGE.css">` — CSS 파일 미존재
- 관리자 페이지의 `<aside class="admin-sidebar">` — admin-header partial로 이동
- 관리자 페이지의 `<header class="admin-header">` — admin-header partial로 이동

---

## 병합 처리

| 병합 결과 | 원본 파일들 | 처리 방식 |
|-----------|------------|----------|
| `index.mustache` | 35메인허브 + 16메인로그인후 + 37메인관리자 | `{{#isAdmin}}` 조건부로 히어로 텍스트/배지 분기 |
| `qna/list.mustache` | 31Q&A커뮤니티 + 36Q&A수정불가 | `{{#isAnswered}}` 조건부로 수정/삭제 버튼 분기 |

---

## Controller 연동 시 주의사항

- `isAdmin` Boolean: User의 `role` 필드("ADMIN"/"USER")에서 파생. 각 Controller에서 `model.addAttribute("isAdmin", user.getRole().equals("ADMIN"))` 필요
- `sessionUser`: LoginInterceptor에서 세션에 저장한 User 객체. Mustache expose-session-attributes=true 설정으로 템플릿에서 직접 접근 가능
- CSS: `static/css/common.css` 미존재. header.mustache에 `{{!-- <link href="/css/common.css"> --}}` 주석 처리됨
- 외부 이미지: `lh3.googleusercontent.com` 목업 이미지 그대로 유지 (추후 `/upload/` 경로로 교체 예정)

---

## 참고

- 원본 HTML 파일: `zBackup/html/` (37개, 보존)
- 변환 결과: `src/main/resources/templates/` (35개)
- Bootstrap 5.3.2 CDN, Google Fonts, Material Symbols CDN 유지
