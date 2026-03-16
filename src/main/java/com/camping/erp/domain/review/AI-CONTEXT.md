<!-- Parent: ../AI-CONTEXT.md -->

# domain/review/

## 목적
이용 완료 고객의 후기 관리.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Review.java` | 리뷰 엔티티 |
| `ReviewRepository.java` | JPA 레포지토리 |
| `ReviewService.java` | 리뷰 비즈니스 로직 |

## AI 작업 지침
- `Reservation` 상태가 '이용완료'인 경우에만 작성 가능하도록 검증.
- 리뷰 별점(rating)은 1~5점 사이로 제한.
