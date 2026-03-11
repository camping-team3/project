<!-- Parent: ../AI-CONTEXT.md -->

# domain/qna/

## 목적
사용자 1:1 문의 및 관리자 답변 관리.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Qna.java` | 질문 엔티티 |
| `Comment.java` | 답변(댓글) 엔티티 |
| `QnaRepository.java` | 질문 레포지토리 |
| `CommentRepository.java` | 답변 레포지토리 |
| `QnaService.java` | Q&A 통합 서비스 |

## AI 작업 지침
- 관리자가 답변을 등록하면 `Qna`의 `isAnswered` 상태를 `true`로 변경함.
- 본인의 글만 조회 가능하도록 필터링 필요 (관리자 제외).
