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

<<<<<<< HEAD
## 4. Coding & Persistence Standards
- **Java Syntax**: Java 17 이상(현재 프로젝트 설정 준수), Spring Boot 3.x를 기반으로 한다.
- **JPA Strategy**: 모든 연관관계는 `FetchType.LAZY`를 기본으로 하며, N+1 문제 방지를 위해 필요시 `fetch join`을 명시적으로 사용한다.
- **Security**: `/admin/**` 경로 접근 시 `AdminInterceptor`를 통한 권한 검증이 필수적으로 이루어져야 한다.
- **Lombok**: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder` 등을 활용하여 보일러플레이트 코드를 최소화한다.
- **DTO Construction Pattern (CRITICAL)**:
    - Service 레이어에서 DTO 생성 시 `new` 이후 `set` 메서드를 수십 번 호출하는 방식을 **엄격히 금지**한다.
    - 대신, DTO 내부에서 엔티티(Entity) 객체 자체를 매개변수로 받는 **생성자(Constructor)**를 구현한다.
    - DTO는 생성자 내부에서 엔티티로부터 필요한 값만 추출하여 자신의 필드에 할당하며, 가능한 모든 필드는 `final`로 선언하여 데이터의 불변성을 확보한다.
    - 예시: `public ZoneDTO(Zone zone) { this.id = zone.getId(); ... }`

## 5. Implementation Workflow
1. **Research**: 기존 도메인 코드 및 `AI-CONTEXT.md`를 통해 요구사항과 제약사항을 확인한다.
2. **Plan**: 구현할 클래스 구조와 로직, 특히 Form 전송 여부와 Redirect 경로를 먼저 설계한다.
3. **Act**: 수술적(Surgical) 변경을 통해 기능을 구현하고, 명명 규칙과 패키지 구조를 엄수한다.
4. **Validate**: 단위 테스트 또는 `testData.sql`을 활용한 통합 테스트로 기능의 정상 작동 여부를 확인한다.
=======
## Coding Standards
- Java 17+, Spring Boot 3.x
- `@PathVariable("id")` 등 파라미터명 명시 필수
- 모든 엔티티는 `global/BaseTimeEntity` 상속 (시간 필드 직접 선언 금지)
- 연관관계 `FetchType.LAZY` 기본, 필요 시 fetch join
- `/admin/**` → `AdminInterceptor` 권한 검증
- Lombok: `@Getter`, `@NoArgsConstructor(access = PROTECTED)`, `@Builder`
>>>>>>> 99492c1d3d3dca07c67d1b454c7e8e3371a97993
