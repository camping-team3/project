<!-- Parent: ../../../../../../../AI-CONTEXT.md -->

# resources/

## 목적
애플리케이션 설정 파일, Mustache 뷰 템플릿, 정적 자원, 테스트 데이터를 포함하는 리소스 루트.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `application.properties` | 서버 포트, H2 DB 연결, JPA/Hibernate 설정, Mustache 설정 |

## 하위 디렉토리
| 디렉토리 | 설명 |
|----------|------|
| `templates/` | Mustache SSR 뷰 템플릿 (layout/, 도메인별 폴더) |
| `static/` | 정적 자원 — CSS, JS, 이미지, 업로드 파일 |
| `db/` | 테스트 데이터 SQL (`testData.sql`) |

## application.properties 주요 설정
| 설정 | 값 | 비고 |
|------|-----|------|
| `server.port` | 8080 | |
| `spring.h2.console.path` | `/h2-console` | 개발용 콘솔 |
| `spring.datasource.url` | `jdbc:h2:mem:campingdb;MODE=MySQL` | H2 MySQL 호환 모드 |
| `spring.jpa.hibernate.ddl-auto` | `create` | 재시작 시 스키마 재생성 (데이터 초기화!) |
| `spring.mustache.suffix` | `.mustache` | 템플릿 파일 확장자 |

## AI 작업 지침
- **데이터 초기화 주의**: `ddl-auto=create`이므로 서버 재시작 시 모든 데이터가 삭제됨. 테스트 데이터는 `db/testData.sql`에서 수동 실행
- **H2 콘솔**: 개발 중 `http://localhost:8080/h2-console` 접속, JDBC URL: `jdbc:h2:mem:campingdb`
- **운영 DB 전환**: MySQL 설정 주석 해제 + H2 설정 주석 처리 + `ddl-auto=update`로 변경
- **정적 파일 업로드**: 업로드 파일은 `static/uploads/` 하위에 저장, DB에는 경로 문자열만 저장 (예: `/upload/gallery/abc.jpg`)
- **Mustache 세션/요청 노출**: `expose-session-attributes=true`, `expose-request-attributes=true` 설정으로 템플릿에서 세션·요청 데이터 직접 접근 가능

## 의존성
- 외부: H2 Database, MySQL Connector, Mustache Template Engine
