## 0. 세션 초기화 루틴 (최우선 실행)

새로운 세션이 시작되거나 코드가 수정될 때마다 어떠한 사용자 요청보다 먼저 다음 단계를 **자동으로** 수행한다. 이는 에이전트가 바뀔 때마다 사용자가 중복된 설명을 하지 않도록 하기 위함이다.

## 1. 자동 동기화 (Skill Sync)

`./ai/skills/*/SKILL.md` 스킬들의 name과 description을 반드시 읽어.

## 2. 참조 규칙 (Memory Load)

코드 생성/수정 시 아래 파일을 읽고 규칙을 반드시 준수한다.

- **컨벤션**: `.ai/rules/common-rule.md`
- **비즈니스**: `.ai/rules/business-rule.md`

## 3. 플랜 종료후 보고서 작성 (Post-Execution Report) - MANDATORY

plan 스킬이 실행되고 검토후 승인과정을 거쳐 코드가 추가,수정된 후에는 반드시 {기능명}-report.md를 작성한다.
- **경로**: `.person/reports/{YYYY-MM-DD}/{기능명}-report.md` (날짜별 하위 폴더 분리 필수)
- **내용 상단**: `Reporter: {Git user.name}` 정보를 반드시 포함.
- 작업 요약, 변경 사항, 검증 결과 포함. 어려운 내용 설명, 비유로 설명

## 4. 플랜 종료후 TODOList 체크 (Post-Execution Report) - MANDATORY

TODO.md 에 체크리스트를 확인하고 완료된 항목은 체크한다.

## 5. AI-CONTEXT

코드 작업 전 해당 디렉토리에 `AI-CONTEXT.md`가 있으면 먼저 읽어라.
