# 📄 Ambiguous Mapping 오류 해결 보고서

Reporter: park (Git user.name)
Date: 2026-03-23
Task: UserController.java 중복 매핑 및 미사용 서비스 제거

## 🔍 작업 요약
- `UserController`와 `ReservationController` 간의 URL 매핑 중복으로 인한 스프링 부트 기동 실패 문제를 해결했습니다.
- `UserController`에 잔류하던 예약 관련 매핑들을 모두 제거하고, 예약 도메인 로직은 `ReservationController`로 단일화했습니다.

## 🛠️ 변경 사항
- **`UserController.java`**:
    - 중복된 매핑 메서드 삭제: `reservationChange`, `reservationChangeDone`, `reservationCancel`, `reservationCancelDone`, `reservationDetail`
    - 미사용 필드 및 의존성 주입 제거: `ReservationService`
    - 미사용 임포트 정리: `ReservationResponse`, `ReservationService`

## ✅ 검증 결과
- `UserController`에는 이제 회원가입, 로그인, 로그아웃, 마이페이지 홈 및 리뷰 목록과 같은 사용자 중심의 매핑만 남았습니다.
- `/mypage/reservations/**` 관련 모든 비즈니스 로직은 `ReservationController`에서 정상적으로 처리됨을 코드를 통해 확인했습니다.

## 💡 비유로 설명하기
한 집에 두 명의 요리사가 똑같은 메뉴를 주문받으려다 보니 주방에 혼란이 생겨 영업을 시작하지 못한 상황이었습니다. 보조 요리사(`UserController`)가 담당하던 메뉴를 메인 요리사(`ReservationController`)에게 완전히 넘겨줌으로써, 이제는 누가 어떤 요리를 할지 명확해져서 주방이 다시 돌아가게 된 것과 같습니다.
