# Common Rules

## Package Structure
- **Root**: `com.camping.erp`
- **Domain-Driven**: `domain/{name}/` 하위에 Controller, Service, Repository, Entity, DTO 배치
- **Enum 분리**: 엔티티 내부 선언 금지 → `domain/{name}/enums/`에 별도 파일
- **Global Layer**: `global/config/`, `global/auth/`, `global/handler/`, `global/util/`, `global/filter/`
- **Templates**: `templates/` 하위 도메인별 폴더, `layout/` 공통 레이아웃 활용

## Naming
- URL `-form` → mustache 파일명도 `-form` 일치 (예: `/login-form` → `auth/login-form.mustache`)

## Web Strategy
- **Form 기반 동기식 처리** 우선, AJAX는 중복 체크 등 최소 사용 (`Resp.java`로 JSON 응답)
- **예외 처리**: `GlobalExceptionHandler` → alert + history.back() Script 응답
- **PRG 패턴**: POST 성공 시 반드시 Redirect

## Coding Standards
- Java 17+, Spring Boot 3.x
- `@PathVariable("id")` 등 파라미터명 명시 필수
- 모든 엔티티는 `global/BaseTimeEntity` 상속 (시간 필드 직접 선언 금지)
- 연관관계 `FetchType.LAZY` 기본, 필요 시 fetch join
- `/admin/**` → `AdminInterceptor` 권한 검증
- Lombok: `@Getter`, `@NoArgsConstructor(access = PROTECTED)`, `@Builder`
