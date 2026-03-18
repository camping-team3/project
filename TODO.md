# 🛠️ Emergency: Merge Conflict Cleanup & Structure Normalization

## Phase 0: 컨플릭트 잔재 제거 및 구조 정상화

### 1. 템플릿 파일 컨플릭트 마커 제거 (HEAD 기준 통합) [x]
- [x] `src/main/resources/templates/reservation/new.mustache` 수정
- [x] `src/main/resources/templates/reservation/payment.mustache` 수정
- [x] `src/main/resources/templates/admin/gallery/update-form.mustache` 수정

### 2. 중복 프로젝트 구조(`project/` 폴더) 조사 및 정리 [x]
- [x] 루트의 `project/` 디렉토리 내용 상세 조사 및 삭제 완료

### 4. 결제 시 세션 불일치 버그 수정 [x]
- [x] `ReservationController.reserve()` 세션 유저 타입 수정 (`User` -> `UserResponse.LoginDTO`)
- [x] `ReservationService.reserve()` 파라미터 타입 수정 및 유저 엔티티 조회 로직 추가
- [x] 결제 완료 리다이렉트 정상 작동 확인

---

# 📅 Phase 3: Review 도메인 구현 (커뮤니티 및 로열티)

## 1. 리뷰 도메인 설계 및 엔티티 구현 [ ]
- [ ] `com.camping.erp.domain.review` 패키지 생성
- [ ] `Review` 엔티티 구현 (JPA 매핑, `BaseTimeEntity` 상속, `Reservation`과 1:1 관계)
- [ ] `ReviewRepository` 인터페이스 및 기본 쿼리 메서드 생성

## 2. 리뷰 등록 기능 (사용자) [ ]
- [ ] `ReviewRequest.SaveDTO` 구현 (별점, 내용, 이미지 파일 포함)
- [ ] `ReviewService.saveReview()` 구현:
    - 예약 상태(`CONFIRMED`) 및 이용 종료일 검증
    - 중복 리뷰 작성 여부 체크
    - 이미지 업로드 처리 (최대 5장, UUID 파일명) 및 `Image` 엔티티 연관관계 설정
- [ ] `ReviewController.save()` 구현:
    - `/review/save` (POST) 매핑 및 유효성 검사
    - 등록 완료 시 알림 및 리다이렉트 (PRG 패턴)

## 3. 리뷰 관리 및 조회 기능 (사용자/관리자) [ ]
- [ ] `ReviewService.updateReview()` 구현 (작성자 본인 검증)
- [ ] `ReviewService.deleteReview()` 구현 (작성자 또는 관리자 권한)
- [ ] `ReviewController` 수정/삭제 API 매핑 (`/review/update`, `/review/delete`)
- [ ] 마이페이지 연동: '리뷰 작성 가능 예약 건' 및 '내가 작성한 리뷰' 목록 로직
- [ ] 사이트 상세 페이지(`site/detail`) 연동: 해당 구역/사이트에 대한 리뷰 목록 렌더링

## 4. 최종 검증 및 보고서 [ ]
- [ ] 단위 테스트 및 통합 테스트 수행 (권한, 유효성 검사, 예외 처리)
- [ ] `.person/reports/2026-03-18/review-domain-report.md` 작성
- [ ] `TODO.md` 및 `phases.md` 상태 동기화 및 최종 커밋
