<!-- Parent: ../AI-CONTEXT.md -->

# domain/notice/

## 목적
시스템 및 서비스 공지사항 관리.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Notice.java` | 공지사항 엔티티 |
| `NoticeRepository.java` | JPA 레포지토리 |
| `NoticeService.java` | 공지사항 비즈니스 로직 |
| `NoticeController.java` | 공지사항 웹 컨트롤러 |

## AI 작업 지침
- 공지사항은 관리자만 작성/수정/삭제 가능하도록 권한 검증이 필요함.
- `BaseTimeEntity`를 상속받아 생성/수정 시간을 관리함.
