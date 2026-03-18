- [x] **1단계: 도메인 모델링 및 기본 구현**
  - [x] Notice 엔티티 수정: isTop 필드 추가, Image 연관관계 설정
  - [x] Image 엔티티 수정: Notice 연관관계 추가
  - [x] NoticeRepository 구현: 최신/상단고정 정렬, 검색 쿼리 추가
- [x] **2단계: 비즈니스 로직 및 DTO 구성**
  - [x] NoticeRequest / NoticeResponse DTO 생성 및 필드 반영
  - [x] NoticeService 구현: CRUD, 검색/페이징, 파일 업로드 로직 통합
- [x] **3단계: 관리자 기능 개발 (Admin CRUD)**
  - [x] AdminNoticeController 생성 및 권한 인터셉터 적용
  - [x] 관리자용 공지사항 목록 화면 (admin/notice/list.mustache)
  - [x] 공지사항 등록/수정 폼 (admin/notice/save-form.mustache) - Summernote 에디터 및 이미지 업로드 통합
- [x] **4단계: 사용자 뷰(Public View)**
  - [x] 공지사항 목록 화면 (notice/list.mustache) - 검색 기능 및 상단고정 디자인 적용
  - [x] 공지사항 상세 뷰 (notice/detail.mustache) - 에디터 내용 렌더링 및 이미지 노출
- [x] **5단계: 안정화**
  - [x] 비로그인 사용자의 접근 권한 인터셉터 적용
  - [x] 통합 테스트 및 예외 처리 로직 (GlobalExceptionHandler) 검증
  - [x] 최종 작업 보고서 (notice-report.md) 작성 및 TODO 체크
- [x] **6단계: 검증 및 데이터 정합성 강화**
  - [x] `data.sql` 초기 데이터 정합성 수정 (`is_top` 컬럼 및 값 추가)
  - [x] 서버 기동 및 런타임 오류 검증 (H2 IntegrityConstraintViolation 해결)
  - [x] 갤러리/공지사항 이미지 업로드(클릭/드래그) 및 프리뷰 기능 구현
- [x] **8단계: 공지사항 페이징 UI 구현**
  - [x] 관리자 공지사항 목록(`admin/notice/list.mustache`) 페이징 연동
  - [x] 사용자 공지사항 목록(`notice/list.mustache`) 페이징 연동
  - [x] 이전/다음 버튼 및 페이지 번호(1부터 시작) 노출 로직 구현
- [x] **11단계: 포토갤러리 저장 및 목록 노출 오류 수정**
  - [x] 갤러리 저장(`AdminGalleryController.save`) 로직 및 데이터 저장 여부 확인
  - [x] 갤러리 목록(`AdminGalleryController.list`) 조회 쿼리 및 페이징 확인
  - [x] 갤러리 엔티티 및 이미지 연관관계 저장 정합성 검토
- [x] **12단계: 포토갤러리 수정 기능 구현**
  - [x] 수정 폼(`admin/gallery/update-form.mustache`) UI 및 JS 구현
  - [x] 수정 요청 처리(`AdminGalleryController.update`) 및 이미지 추가/삭제 로직
  - [x] 목록 화면에서 수정 버튼 링크 연결
- [x] **13단계: 갤러리 썸네일 즉시 반영 UI 개선**
  - [x] 갤러리 작성/수정 후 목록/상세 화면 이동 시 캐시 또는 리로딩 문제 해결 (외부 경로 매핑 방식 도입)
  - [x] 썸네일 이미지 URL에 타임스탬프 또는 캐시 방지 파라미터 적용 검토 (UUID 사용으로 중복 방지 확인)
  - [x] 작업 보고서 작성 및 TODO 체크
- [x] **14단계: 갤러리 카테고리 통일**
  - [x] 등록/수정 폼 카테고리 옵션 일치 [특별 이벤트, 캠핑장 전경, 기타]
  - [x] 수정 폼 기존 카테고리 자동 선택 로직 추가
  - [x] 초기 데이터(data.sql) 카테고리 정합성 수정
  - [x] 작업 보고서 작성 및 TODO 체크
- [x] **15단계: 갤러리 작성 URL 및 템플릿 명칭 컨벤션 준수**
  - [x] AdminGalleryController: /new -> /save-form URL 변경
  - [x] Mustache: new.mustache -> save-form.mustache 파일명 변경
  - [x] 갤러리 목록 버튼 링크 수정
  - [x] 작업 보고서 작성 및 TODO 체크
- [x] **16단계: 갤러리 게시글 최신순 정렬**
  - [x] GalleryRepository: ID 내림차순 쿼리 메서드 추가
  - [x] GalleryService: 최신순 정렬 메서드 호출 적용
  - [x] 작업 보고서 작성 및 TODO 체크
- [x] **17단계: 데이터 정합성 오류 수정**
  - [x] `data.sql` 내 `gallery_tb` 데이터 삽입 구문 누락 또는 순서 오류 확인
  - [x] `image_tb` 삽입 시점 조정 (참조하는 테이블 데이터 삽입 이후로 이동)
  - [x] 서버 재기동 및 H2 데이터 초기화 정상 작동 검증
- [x] **18단계: gallery_tb 컬럼 누락 수정**
  - [x] `data.sql`의 `gallery_tb` 삽입문에 `shooting_date`, `view_count` 컬럼 추가
  - [x] 서버 재기동 및 데이터 초기화 성공 여부 최종 확인
- [x] **19단계: ERD.md 문서 최신화**
  - [x] `gallery_tb`에 `shooting_date`, `view_count` 컬럼 추가 반영
  - [x] `image_tb`에 `notice_id` (FK) 추가 및 통합 관계 명시
  - [x] 엔티티 관계도(Notice : Image) 최신화
- [x] **20단계: 도메인 공통 안정화 및 리팩토링**
  - [x] `FileUtil` 공통 유틸리티 생성 및 물리적 파일 삭제 기능 구현
  - [x] `GalleryService`, `NoticeService` 내 파일 삭제 로직 통합 적용
  - [x] DTO(Validation) 및 프론트엔드(JS) 이미지 필수 검증 로직 강화
  - [x] `Image` 엔티티 Setter 제거 및 캡슐화 리팩토링

# 💬 Phase 3: Review 도메인 구현 (커뮤니티 및 평점 시스템)

## 1. 도메인 기초 설계 및 엔티티 구현 [ ]
- [ ] `com.camping.erp.domain.review` 패키지 생성
- [ ] `Review` 엔티티 구현 (JPA 매핑, `BaseTimeEntity` 상속, `Reservation`과 1:1 관계)
- [ ] `ReviewRepository` 인터페이스 생성 (기본 CRUD 및 평균 평점 계산 쿼리 포함)

## 2. 리뷰 작성 기능 (고객용) [ ]
- [ ] `ReviewRequest.SaveDTO` 구현 (별점, 내용, 이미지 파일 리스트 포함)
- [ ] `ReviewService.saveReview()` 구현:
    - [ ] 예약 상태(`CONFIRMED`) 및 체크아웃 날짜 검증 로직 (체크아웃 당일 가능)
    - [ ] 중복 리뷰 작성 여부 체크 (이미 해당 예약에 리뷰가 있는지 확인)
    - [ ] 이미지 업로드 처리 (최대 5장 제한, UUID 파일명 생성, `Image` 엔티티 연동)
- [ ] `ReviewController.save()` 구현:
    - [ ] `/review/save` (POST) 매핑 및 유효성 검사 (10~1000자)
    - [ ] 작성 완료 후 마이페이지로 리다이렉트 (PRG 패턴)

## 3. 리뷰 관리 및 조회 기능 (고객/관리자) [ ]
- [ ] `ReviewService.updateReview()` 구현 (작성자 본인 권한 검증 포함)
- [ ] `ReviewService.deleteReview()` 구현 (작성자 본인 및 관리자 권한 허용)
- [ ] `ReviewController` 내 수정/삭제 API 매핑 (`/review/update`, `/review/delete`)
- [ ] 마이페이지 연동: '작성 가능한 예약 건' 및 '내가 작성한 리뷰' 목록 조회 로직
- [ ] 사이트 상세 페이지(`site/detail`) 연동: 해당 구역/사이트의 평균 평점 및 리뷰 목록 동적 렌더링

## 4. 최종 검증 및 보고서 작성 [ ]
- [ ] 단위 테스트 및 통합 테스트 수행 (권한, 유효성, 이미지 제한 등)
- [ ] `.person/reports/2026-03-18/review-domain-report.md` 작성
- [ ] `TODO.md` 및 `phases.md` 상태 동기화 및 자동 커밋
