# 🔑 User 도메인 완성 및 검증 강화 (Phase 2)

## 1. 서버 사이드 검증 로직 추가 (JoinDTO) [x]
- [x] UserRequest.JoinDTO에 passwordConfirm 필드 추가
- [x] UserService.join()에서 password와 passwordConfirm 일치 여부 검증 로직 추가
- [x] (Optional) JoinDTO에 유효성 검사 어노테이션 추가 (email, phone 형식 등)

## 2. 예외 처리 및 사용자 알림 강화 [x]
- [x] GlobalExceptionHandler에서 RuntimeException 발생 시 Alert 창 띄우고 history.back() 응답 확인
- [x] UserService에서 Exception400을 사용하여 구체적인 에러 메시지 전달

## 3. 관리자 회원 관리 기능 최종 점검 [x]
- [x] AdminUserController 목록 조회 페이징 동작 확인
- [x] AdminInterceptor를 통한 권한 접근 제어 테스트
- [x] UserStatus (ACTIVE/ANONYMOUS) 전환 로직 추가 (회원 상태 변경 기능)

## 4. 최종 통합 테스트 [x]
- [x] 회원가입(중복체크, 비밀번호불일치, 성공) 테스트
- [x] 로그인/로그아웃/마이페이지 접근 테스트
- [x] 관리자 페이지 회원 목록 및 권한 변경 테스트

