# Forest Haven Project Rules

## 1. Context Syncing (CRITICAL)
- 모든 대화의 시작 전에 반드시 `@ai-context.md`를 읽고 현재 작업 단계(Milestone)와 파일 구조를 파악하라.
- 새로운 파일을 생성하거나 기존 비즈니스 로직(Service, Controller 등)을 수정할 경우, 반드시 `@ai-context.md`의 '3. Directory & File Manifest'와 '4. Business Logic' 섹션을 최신화하라.

## 2. Auto-Logging Protocol
- 작업이 성공적으로 완료될 때마다(예: Entity 생성 완료, 결제 로직 구현 완료), `@ai-context.md`의 '6. Work Log' 섹션에 날짜와 작업 내용을 한 줄로 자동 기록하라.
- 사용자가 "수고했어" 또는 "오늘 작업 끝"이라고 말하면, 오늘 진행한 전체 내역을 요약하여 `ai-context.md`의 '1. Project Context' 진척도를 업데이트하고 '6. Work Log'에 상세 기록하라.

## 3. Coding Standards
- Java 21 / Spring Boot 3.x / JPA (LAZY) / Redis 규칙을 엄수하라.
- 모든 상태 변경은 POST + PRG 패턴을 적용하라.
- 기존 01~37.html 파일의 매핑 테이블을 준수하여 템플릿을 생성하라.