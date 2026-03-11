<!-- Parent: ../AI-CONTEXT.md -->

# domain/gallery/

## 목적
캠핑장 포토 갤러리 관리.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Gallery.java` | 갤러리 게시글 엔티티 |
| `GalleryRepository.java` | JPA 레포지토리 |
| `GalleryService.java` | 갤러리 비즈니스 로직 |

## AI 작업 지침
- 한 게시글에 여러 이미지를 포함할 수 있음 (`Image` 엔티티와 1:N).
- 이미지 업로드 시 `ImageService`(예정)와 연동하여 처리.
