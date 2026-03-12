# Camping ERP Project Common Rules

## 1. Context & Metadata Sync (CRITICAL)
- 모든 작업의 시작 시 `AI-CONTEXT.md`를 읽고 현재 진행 단계(Phase)와 파일 구조를 파악한다.
- 파일 생성/수정 시 `AI-CONTEXT.md`의 **2. 프로젝트 맵**과 **3. 비즈니스 로직 매핑** 섹션을 즉시 업데이트한다.
- 작업 완료 시 `AI-CONTEXT.md`의 **1. 프로젝트 현재 상태**와 **5. 작업 히스토리**에 진척도와 내용을 기록한다.

## 2. Package & Directory Standards
- **Root Package**: `com.camping.erp` (오타 주의: `caping` 아님)
- **Domain-Driven Directory**: `src/main/java/com/camping/erp/domain/{domain_name}/` 폴더 안에 해당 도메인의 Controller, Service, Repository, Entity, DTO를 모아서 관리한다.
- **Enum 분리**: 도메인 엔티티 내부에 enum을 선언하지 않는다. `domain/{domain_name}/enums/` 하위 패키지에 별도 파일로 분리한다.
  - 예: `UserRole` → `domain/user/enums/UserRole.java`
  - 예: `ReservationStatus` → `domain/reservation/enums/ReservationStatus.java`
- **BaseTimeEntity 사용**: 모든 엔티티는 `global/BaseTimeEntity`를 상속하여 `createdAt`, `updatedAt` 필드를 공통으로 관리한다. 엔티티에 `@EntityListeners`나 시간 필드를 직접 선언하지 않는다.
- **Global Layer**: 공통 설정은 `global/config/`, 인증/인가는 `global/auth/`, 예외 처리는 `global/handler/`, 유틸리티는 `global/util/`, 필터는 `global/filter/` 하위에 위치시킨다.
- **View Templates**: `src/main/resources/templates/` 하위에 도메인별 폴더로 관리하며, `layout/header.mustache` 등 공통 레이아웃을 적극 활용한다.

## 3. Naming Conventions
- **URL-Template 일치**: `@GetMapping` URL 끝에 `-form`이 붙는 경우 mustache 파일명도 동일하게 `-form`을 붙인다.
  - 예: `@GetMapping("/login-form")` → `return "auth/login-form"` → `templates/auth/login-form.mustache`

## 4. Web & Response Strategy
- **Form-Based Development**: AJAX 사용을 최소화하고, 주요 데이터 전송은 HTML `<form>` 태그를 통한 동기식 요청으로 처리한다.
- **Error Handling (UI)**: 비즈니스 예외 발생 시 `GlobalExceptionHandler`를 통해 사용자에게 `alert` 창을 띄우고 `history.back()`을 수행하는 **Script 응답**을 반환한다.
- **AJAX & JSON**: 중복 체크 등 꼭 필요한 경우에만 제한적으로 AJAX를 사용하며, 이때는 `global/util/Resp.java`를 활용하여 JSON 응답을 제공한다.
- **PRG Pattern**: 모든 상태 변경(`POST`) 성공 시 반드시 **Redirect**를 수행하여 브라우저 새로고침 시의 중복 제출을 방지한다.

## 5. Coding & Persistence Standards
- **Java Syntax**: Java 17 이상(현재 프로젝트 설정 준수), Spring Boot 3.x를 기반으로 한다.
    - 컨트롤러에서 파라미터 바인딩 시 반드시 `@PathVariable("id")` 처럼 **괄호 안에 명시적으로 파라미터 이름을 지정**해야 한다. (Spring Boot 3.2+ 바이트코드 파라미터 이름 인식 이슈 방지 목적)
- **JPA Strategy**: 모든 연관관계는 `FetchType.LAZY`를 기본으로 하며, N+1 문제 방지를 위해 필요시 `fetch join`을 명시적으로 사용한다.
- **Security**: `/admin/**` 경로 접근 시 `AdminInterceptor`를 통한 권한 검증이 필수적으로 이루어져야 한다.
- **Lombok**: `@Getter`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)`, `@Builder`를 활용하여 보일러플레이트 코드를 최소화한다. `@Data`는 사용하지 않는다.

## 6. Implementation Workflow
1. **Research**: 기존 도메인 코드 및 `AI-CONTEXT.md`를 통해 요구사항과 제약사항을 확인한다.
2. **Plan**: 구현할 클래스 구조와 로직, 특히 Form 전송 여부와 Redirect 경로를 먼저 설계한다.
3. **Act**: 수술적(Surgical) 변경을 통해 기능을 구현하고, 명명 규칙과 패키지 구조를 엄수한다.
4. **Validate**: 단위 테스트 또는 `data.sql`을 활용한 통합 테스트로 기능의 정상 작동 여부를 확인한다.
