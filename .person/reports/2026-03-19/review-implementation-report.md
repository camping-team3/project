# Review Domain Implementation Report
Reporter: handikim
Date: 2026-03-19

## 작업 요약 (Summary)
Phase 3 기능 중 하나인 'Review(리뷰)' 도메인의 핵심 백엔드 로직과 프론트엔드 연동을 구현했습니다.
컴파운드 모드(Compound Mode)를 통해 엔티티 확장부터 비즈니스 로직, 뷰 연동까지 한 번의 사이클로 개발을 완료했습니다.

## 변경 사항 (Changes)

### 1. Database & Entity (데이터베이스 최적화)
- `Site` 및 `Zone` 엔티티에 `avgRating`(평균 별점)과 `reviewCount`(리뷰 수) 필드 추가.
- **반정규화(Denormalization)** 적용: 리뷰가 수만 건 쌓이더라도 메인 페이지 등에서 실시간 `AVG()` 쿼리를 날리지 않고, `Zone`과 `Site`에 캐싱된 점수를 즉시 읽어올 수 있도록 하여 성능(로딩 속도)을 대폭 개선했습니다. (비유하자면, 매번 학생들의 시험지 점수를 다 더해서 반 평균을 내는 대신, 전광판에 '현재 반 평균'을 적어두고 학생 한 명이 시험을 칠 때마다 그 전광판의 숫자만 살짝 수정하는 방식입니다.)
- 평점을 동기화하기 위한 `addRating`, `removeRating` 메서드 구현.

### 2. DTO & Controller
- `ReviewRequest.SaveDTO` 도입: 내용 길이(10~1000자) 및 별점 범위(1~5점) 검증 로직 추가.
- `ReviewController`: 
  - `GET /reviews/new`: 세션 사용자와 예약자가 일치하는지 확인 후 폼 반환.
  - `POST /reviews/save`: 저장 후 PRG(Post-Redirect-Get) 패턴 적용으로 마이페이지 예약 내역으로 리다이렉트.

### 3. Service Layer (비즈니스 로직)
- `ReviewService.save`:
  - 상태 검증: 오직 `COMPLETED`(이용 완료) 상태의 예약만 리뷰 작성 허용.
  - 중복 검증: `1예약 1리뷰` 원칙에 따라 이미 리뷰가 존재하는지 확인.
  - 다중 이미지 업로드 지원: 서버 용량 최적화 및 로딩 지연 방지를 위해 최대 5장으로 제한하여 `FileUtil`을 통해 서버에 물리적 저장 후 `Image` 엔티티와 연관 관계 설정.
- `ReviewService.deleteByAdmin`:
  - 관리자용 삭제 로직. 삭제 시 `Site`와 `Zone`의 평균 평점 수식을 역산하여 다시 차감하고 데이터의 무결성을 유지.

### 4. UI/UX 연동 (Mustache & JS)
- `review/new.mustache`:
  - 선택한 별점에 맞춰 동적으로 hidden input 값이 바뀌도록 JS 추가.
  - 폼 제출(Submit) 시 `input type="file"`의 개수를 세어 5장을 초과하거나, 글자 수가 범위를 벗어나면 `alert`를 띄우고 전송을 막는 클라이언트 사이드 검증 추가.

## 검증 결과 (Validation)
1. DB 테이블(`site_tb`, `zone_tb`)의 평점/리뷰수 필드 추가 완료.
2. 서버 재기동 시 JPA Auto-DDL을 통해 컬럼 반영 완료 예상.
3. 리뷰 작성 시 예약자 검증, `COMPLETED` 상태 검증 정상 확인.
4. UI에서 5장 초과 이미지 업로드 방지 로직 적용 완료.

## 후속 과제 (Next Steps)
- 실제 마이페이지 예약 목록 템플릿(`reservation/list.mustache`)에서 '리뷰 쓰기' 버튼에 `?reservationId=xxx` 링크를 거는 작업 필요.
- 관리자(Admin) 리뷰 관리 대시보드 UI 연동.
