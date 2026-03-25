# Reporter: Gemini CLI

## 작업 요약
QnA 화면 접속 시 로그인 상태임에도 불구하고 로그인 폼으로 리다이렉트되는 버그를 수정하였습니다.

## 변경 사항
- **QnaController & AdminController**: 세션의 `sessionUser`를 `User` 엔티티가 아닌 `UserResponse.LoginDTO` 타입으로 캐스팅하도록 수정.
- **QnaService**: 인자 타입을 `LoginDTO`로 변경하고, 엔티티 저장 시 ID를 이용한 프록시 객체 생성 로직 적용.
- **QnaResponse**: DTO 생성자에서 `LoginDTO`를 사용하여 소유권 확인 로직 수행.

## 검증 결과
- `ClassCastException` 발생 원인 제거 확인.
- `GlobalExceptionHandler`의 예외 캐치 및 리다이렉트 루프 해결.
- 로그인한 사용자가 자신의 QnA 목록 및 상세 내용을 정상적으로 조회할 수 있음을 구조적으로 확인.

## 비유를 통한 설명
마치 **'학생증(LoginDTO)'**을 보여줘야 하는 도서관 입구에서, 관리자가 **'학생 본인(User Entity)'**을 통째로 데려오라고 요구한 것과 같습니다. 학생은 이미 학생증을 가지고 있지만, 관리자가 요구하는 형식이 달라 입구에서 쫓겨나 다시 **'안내 데스크(Login Form)'**로 보내졌던 상황입니다. 이제 관리자가 학생증만 확인하고도 입장을 허가하도록 수정했습니다.
