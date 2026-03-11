# 워크플로우 강화 보고서 (Workflow Enforcement Report)

## 1. 개요
- **작업 일시**: 2026-03-11
- **목적**: `plan` 스킬 수행 후 사후 보고서 작성을 절대적인 의무(MANDATORY)로 정의하여 워크플로우의 완성도 보장.
- **대상 파일**: `AI-GUIDE.md`, `.ai/skills/plan/SKILL.md`

## 2. 주요 변경 사항
### AI-GUIDE.md
- **3. 사후 보고서 작성 (MANDATORY)** 섹션 신설.
- 사용자 요청 여부와 무관하게 모든 유의미한 작업 완료 후 `.person/reports/` 폴더에 보고서 작성을 강제함.

### .ai/skills/plan/SKILL.md
- **Instructions**: 4번 항목을 수정하여 보고서 작성을 '무조건' 실행하도록 명시.
- **Core Workflow**: 'report.md 자동 생성' 단계를 명문화하고, 보고서 생성을 최종 완료의 조건으로 정의.

## 3. 적용 효과
- 모든 AI 에이전트는 코드 수정 후 즉각적으로 작업 결과를 문서화함.
- 사용자는 별도의 독촉 없이도 작업의 상세 내용과 검증 결과를 확인할 수 있음.
- 프로젝트 히스토리 관리가 자동화되어 추후 유지보수 시 유리함.

---
**보고자**: Gemini CLI (plan skill enforced)
