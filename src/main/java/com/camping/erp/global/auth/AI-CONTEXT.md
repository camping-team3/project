<!-- Parent: ../AI-CONTEXT.md -->

# global/_core/auth/

## 목적
세션 기반 인증(LoginInterceptor) 및 관리자 권한 검증(AdminInterceptor)을 담당하는
HandlerInterceptor 구현체.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `LoginInterceptor.java` | `sessionUser`가 null이면 `Exception401` 발생. 비로그인 사용자 차단. |
| `AdminInterceptor.java` | `sessionUser.role`이 `"ADMIN"`이 아니면 `Exception403` 발생. 관리자 전용 경로 보호. |

## 세션 구조
- **세션 키**: `"sessionUser"`
- **세션 값 타입**: `com.camping.erp.domain.user.UserResponse.LoginDTO` DTO
- **로그인 처리**: 인증 성공 시 `session.setAttribute("sessionUser", loginDto)` 저장
- **로그아웃 처리**: `session.invalidate()` 호출

## 예외 흐름
```
인터셉터 예외 발생
  → GlobalExceptionHandler 포착
  → AJAX 요청: Resp.fail(status, msg) JSON 반환
  → 일반 폼: <script>alert('msg'); 처리;</script> 반환
    - 401: location.href = '/login-form'
    - 403/400/404/500: history.back()
```

## AI 작업 지침
- **인터셉터 등록 필수**: 구현만으로는 동작 안 함. `global/config/WebMvcConfig.addInterceptors()`에 경로 등록 필요
- **세션 키 통일**: 항상 `"sessionUser"` 문자열 사용 (오타 방지)
- **AdminInterceptor 주의**: `LoginInterceptor`가 먼저 실행되므로 `AdminInterceptor`에서 null 체크는 중복이나 안전을 위해 유지

## 의존성
- 내부: `domain/user/User`, `global/_core/handler/ex/Exception401`, `global/_core/handler/ex/Exception403`
- 외부: Jakarta Servlet API, Spring WebMvc (`HandlerInterceptor`)
