<!-- Parent: ../AI-CONTEXT.md -->

# domain/user/

## 목적
회원 관리 및 인증 시스템. 가입, 로그인, 권한 관리를 담당한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `User.java` | 회원 엔티티 (username, password, role, status 등) |
| `UserRepository.java` | JPA 기반 DB 접근 (findByUsername 지원) |
| `UserService.java` | 회원 가입 및 로그인 비즈니스 로직 (뼈대 생성됨) |
| `UserRequest.java` | JoinDTO, LoginDTO 등 요청 데이터 구조 |
| `UserResponse.java` | LoginDTO, DetailDTO 등 응답 데이터 구조 |

## AI 작업 지침
- **비밀번호**: 서비스 구현 시 반드시 암호화 처리를 고려해야 한다.
- **상태 관리**: `UserStatus.ANONYMOUS`를 통한 소프트 딜리트(익명화) 정책을 준수한다.

## 의존성
- 내부: `global/_core/BaseTimeEntity`
- 외부: Spring Data JPA, Lombok
