# 🌲 Forest Haven ERP 개발 체크리스트

## ✅ Phase 2: MVP 핵심 도메인 로직 완료
- [x] 사용자/관리자 상세보기 및 포토 갤러리 조회수 구현 완료

## 🚀 Phase 3: 확장 도메인 - 예약 상세 보기 (MyPage & Admin) [완료]

### 1. MyPage 예약 상세 보기 [✅ 완료]
- [x] **UI 구현**: `reservation-detail.mustache` 생성 및 `reservation-detail.css` 작성
- [x] **링크 연결**: `home.mustache`, `reservations.mustache` 버튼에 상세 보기 링크 연결
- [x] **매핑 추가**: `UserController`에 상세 보기 엔드포인트 추가

### 2. Admin 예약 상세 보기 [✅ 완료]
- [x] **UI 구현**: `admin/reservation/detail.mustache` 생성 및 `detail.css` 작성
- [x] **링크 연결**: `admin/reservation/list.mustache`에 상세 보기(눈 아이콘) 버튼 추가
- [x] **매핑 추가**: `AdminController`에 상세 보기 엔드포인트 추가

---
*(이후 작업은 Payment 및 예약 선점 로직으로 이어집니다.)*
