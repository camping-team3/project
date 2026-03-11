<!-- Parent: ../AI-CONTEXT.md -->

# domain/board/

## 목적
커뮤니티 및 콘텐츠 관리 (공지, 갤러리, Q&A, 리뷰, 이미지).

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Notice.java` | 공지사항 엔티티 |
| `Gallery.java` | 포토 갤러리 엔티티 (Image와 1:N) |
| `Qna.java` | 질문 엔티티 (Comment와 1:N) |
| `Comment.java` | 관리자 답변 엔티티 |
| `Review.java` | 예약 기반 리뷰 엔티티 (Image와 1:N) |
| `Image.java` | 공통 이미지 관리 엔티티 (Gallery 또는 Review 참조) |
| `BoardService.java` | 게시판 통합 관리 서비스 (뼈대 생성됨) |

## AI 작업 지침
- **다중 이미지**: 갤러리와 리뷰는 `Image` 엔티티를 통해 여러 장의 사진을 관리한다.
- **리뷰 권한**: `Reservation`의 상태가 이용 완료인 경우에만 작성이 가능하도록 검증 로직이 필요하다.
