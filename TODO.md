# 📋 작업 현황 (2026-03-23)

## 🛠️ 기능 구현: 마이페이지 회원 정보 조회

- [x] **UserController.java 회원 정보 매핑 추가**
    - [x] `/mypage/info` GET 메서드 구현 (`info` 메서드)
    - [x] `userService.findUser(id)` 호출 및 모델 바인딩
- [x] **mypage/info.mustache 템플릿 생성**
    - [x] 승인된 UI 구조 반영 및 디자인 토큰 준수
- [x] **마이페이지 사이드바 링크 일괄 업데이트**
    - [x] `home`, `reservations`, `reviews`, `reservation-detail`, `reservation-change`, `reservation-cancel`
    - [x] '회원 정보' 링크 (`#` -> `/mypage/info`) 수정

---
## 🏁 사후 처리
- [x] `.person/reports/2026-03-23/mypage-info-view-report.md` 작성
- [ ] `phases.md` 업데이트 및 한글 커밋 메시지 제안/커밋
