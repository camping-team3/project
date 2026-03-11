<!-- Parent: ../AI-CONTEXT.md -->

# domain/image/

## 목적
시스템 전반의 이미지 업로드 및 메타데이터 관리.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Image.java` | 이미지 메타데이터 엔티티 |
| `ImageRepository.java` | JPA 레포지토리 |

## AI 작업 지침
- `Gallery`, `Review` 등 다른 도메인에서 공통으로 참조함.
- 실제 파일 저장 로직은 `global/_core/util`의 파일 업로드 유틸리티 활용 권장.
