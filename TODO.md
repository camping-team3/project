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
