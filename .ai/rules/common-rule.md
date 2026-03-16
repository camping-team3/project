# Camping ERP Project Common Rules

## 1. Context & Metadata Sync (CRITICAL)
- 모든 작업의 시작 시 `@ai-context.md`를 읽고 현재 진행 단계(Phase)와 파일 구조를 파악한다.
- 파일 생성/수정 시 `ai-context.md`의 **2. 프로젝트 맵**과 **3. 비즈니스 로직 매핑** 섹션을 즉시 업데이트한다.
- 작업 완료 시 `ai-context.md`의 **1. 프로젝트 현재 상태**와 **5. 작업 히스토리**에 진척도와 내용을 기록한다.

## 2. Package & Directory Standards
- **Root Package**: `com.camping.erp` (오타 주의: `caping` 아님)
- **Domain-Driven Directory**: `src/main/java/com/camping/erp/domain/{domain_name}/` 폴더 안에 해당 도메인의 Controller, Service, Repository, Entity, DTO를 모아서 관리한다.
- **Global Layer**: 공통 설정은 `global/config/`, 인프라 및 기술 기초는 `global/_core/` 하위에 위치시킨다.
- **View Templates**: `src/main/resources/templates/` 하위에 도메인별 폴더로 관리하며, `layout/header.mustache` 등 공통 레이아웃을 적극 활용한다.

## 3. Web & Response Strategy
- **Form-Based Development**: AJAX 사용을 최소화하고, 주요 데이터 전송은 HTML `<form>` 태그를 통한 동기식 요청으로 처리한다.
- **Error Handling (UI)**: 비즈니스 예외 발생 시 `GlobalExceptionHandler`를 통해 사용자에게 `alert` 창을 띄우고 `history.back()`을 수행하는 **Script 응답**을 반환한다.
- **AJAX & JSON**: 중복 체크 등 꼭 필요한 경우에만 제한적으로 AJAX를 사용하며, 이때는 `global/_core/util/Resp.java`를 활용하여 JSON 응답을 제공한다.
- **PRG Pattern**: 모든 상태 변경(`POST`) 성공 시 반드시 **Redirect**를 수행하여 브라우저 새로고침 시의 중복 제출을 방지한다.

## 4. Coding & Persistence Standards
- **Java Syntax**: Java 17 이상(현재 프로젝트 설정 준수), Spring Boot 3.x를 기반으로 한다.
- **JPA Strategy**: 모든 연관관계는 `FetchType.LAZY`를 기본으로 하며, N+1 문제 방지를 위해 필요시 `fetch join`을 명시적으로 사용한다.
- **Security**: `/admin/**` 경로 접근 시 `AdminInterceptor`를 통한 권한 검증이 필수적으로 이루어져야 한다.
- **Lombok**: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder` 등을 활용하여 보일러플레이트 코드를 최소화한다.

## 5. Implementation Workflow
1. **Research**: 기존 도메인 코드 및 `AI-CONTEXT.md`를 통해 요구사항과 제약사항을 확인한다.
2. **Plan**: 구현할 클래스 구조와 로직, 특히 Form 전송 여부와 Redirect 경로를 먼저 설계한다.
3. **Act**: 수술적(Surgical) 변경을 통해 기능을 구현하고, 명명 규칙과 패키지 구조를 엄수한다.
4. **Validate**: 단위 테스트 또는 `testData.sql`을 활용한 통합 테스트로 기능의 정상 작동 여부를 확인한다.
