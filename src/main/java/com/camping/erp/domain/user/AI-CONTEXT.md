<!-- Parent: ../AI-CONTEXT.md -->

# domain/user/

## 목적
회원 관리 및 인증 시스템. 가입, 로그인, 세션 관리, 회원 등급 처리를 담당한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `User.java` | 회원 엔티티 (username, password, name, role, status) |
| `UserController.java` | 가입/로그인 웹 요청 처리 (SSR) |
| `UserService.java` | 회원 가입 및 비밀번호 암호화 로직 |
| `UserRequest.java` | 폼 전송용 DTO (가입, 로그인) |
| `UserResponse.java` | 뷰에 전달할 데이터용 DTO |
| `UserRepository.java` | JPA 기반 DB 접근 |

## AI 작업 지침
- **비밀번호**: 반드시 `BCryptPasswordEncoder`를 통해 암호화하여 저장한다.
- **인증 상태**: `HttpSession`을 사용하여 사용자 객체를 저장한다.
- **인가**: `/mypage/**` 등은 `LoginInterceptor`에 의해 보호되어야 한다.

## 테스트
- `UserRepositoryTest`를 통해 회원 중복 검증 로직 등을 확인한다.

## 의존성
- 내부: `global/_core/auth` (Interceptor), `global/_core/util/Resp`
- 외부: Spring Security (Encryption), Spring Data JPA
