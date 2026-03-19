# Camping ERP Project TODO List

## 🏗️ Phase 2: MVP 핵심 도메인 고도화 (완료)
- [x] 공지사항/갤러리 이미지 업로드 및 관리자 CRUD 완료
- [x] 이미지 데이터 통합 및 DB 무결성 확보
- [x] 업로드 용량 증설 및 예외 처리

## 💬 Phase 3: 확장 도메인 및 상세보기 구현 (진행 중)
### 1. 포토 갤러리 사용자 기능 강화
- [x] **21단계: 사용자용 포토 갤러리 상세보기 구현**
  - [x] `GalleryController`: `/galleries/{id}` 상세 조회 매핑 추가
  - [x] `GalleryService`: ID 기반 상세 데이터(이미지 포함) 조회 로직 구현
  - [x] `GalleryResponse.DetailDTO` 구성
  - [x] `gallery/detail.mustache` 템플릿 생성 (**design-rule 준수**)
  - [x] `gallery/list.mustache` 하드코딩 제거 및 상세 페이지 링크 연결

### 2. 관리자용 상세보기 기능 구현 (New)
- [ ] **22단계: 관리자 공지사항 상세보기 구현**
  - [ ] `AdminController`: `/admin/notices/{id}` 매핑 추가
  - [ ] `admin/notice/detail.mustache` 생성 (**design-rule 준수**)
  - [ ] `admin/notice/list.mustache` 링크 연결 수정
- [ ] **23단계: 관리자 포토 갤러리 상세보기 구현**
  - [ ] `AdminController`: `/admin/galleries/{id}` 매핑 추가
  - [ ] `admin/gallery/detail.mustache` 생성 (**design-rule 준수**)
  - [ ] `admin/gallery/list.mustache` 링크 연결 수정

### 3. 리뷰 도메인 구현 (Next)
... (기존 내용 동일)
- [ ] 리뷰 엔티티 및 Repository 생성
- [ ] 이용 완료 예약 건 대상 리뷰 작성 로직
- [ ] 마이페이지 리뷰 목록 및 사이트 상세 연동

---

## 🛠️ 공통 작업 및 안정화
- [x] 디자인 시스템 표준 수립 (`design-rule.md`)
- [x] UI 레퍼런스 체계 구축 (`.ai/references/`)
- [ ] 전반적인 UI 상세보기 기능 점검 (공지사항/갤러리 관리자 포함)
