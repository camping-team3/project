<!-- Parent: ../../../../../../../AI-CONTEXT.md -->

# com.camping.erp (Root Package)

## 목적
Spring Boot 애플리케이션의 루트 패키지. `@EnableJpaAuditing`을 활성화하여
모든 엔티티의 생성·수정 시각 자동 기록을 활성화한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `ErpApplication.java` | 애플리케이션 진입점. `@SpringBootApplication`, `@EnableJpaAuditing` 선언 |

## 하위 패키지
- `domain/` - 비즈니스 도메인 및 엔티티 (9개 Bounded Context: user, site, reservation 등)
- `global/` - 공통 설정(`config/`), 인터셉터·예외처리·유틸(`_core/`)

## AI 작업 지침
- **패키지명 필수 준수**: `com.camping.erp` (오타 주의: `caping` 아님)
- **JPA Auditing**: `@EnableJpaAuditing`은 `ErpApplication`에 이미 선언됨 — 다른 Config 클래스에 재선언 금지
- 신규 도메인 → `domain/{domain_name}/` 하위 생성
- 신규 공통 기능 → `global/` 하위 적절한 패키지에 생성

## 의존성
- 외부: Spring Boot 4.0.3, Spring Data JPA, Mustache, H2, MySQL Connector
