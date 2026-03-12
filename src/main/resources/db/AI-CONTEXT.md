<!-- Parent: ../AI-CONTEXT.md -->

# resources/db/

## 목적
개발 및 테스트 환경에서 H2 인메모리 DB에 초기 데이터를 삽입하는 SQL 스크립트 모음.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `testData.sql` | 11개 테이블 초기 데이터 (서버 기동 후 H2 콘솔 또는 `@Sql`로 실행) |

## 테스트 계정 목록
| username | password | role | 설명 |
|----------|----------|------|------|
| `admin` | `1234` | ADMIN | 관리자 계정 |
| `ssar` | `1234` | USER | 이용완료 예약 보유 → 리뷰 작성 가능 |
| `cos` | `1234` | USER | 현재 이용 중인 예약 보유 |
| `love` | `1234` | USER | 미래 예약(CONFIRMED) 보유 |
| `guest` | `1234` | USER | 취소 요청(CANCEL_REQ) 중인 예약 보유 |
| `olduser` | `1234` | USER | ANONYMOUS(탈퇴) 상태 |

## 예약 테스트 케이스
| reservation_id | user | status | 설명 |
|----------------|------|--------|------|
| 1 | ssar | CONFIRMED | 이용 완료 (과거 날짜) → 리뷰 작성 대상 |
| 2 | cos | CONFIRMED | 현재 이용 중 |
| 3 | love | CONFIRMED | 미래 예약 |
| 4 | guest | CANCEL_REQ | 취소 요청 중 → 관리자 승인 대기 |
| 5 | ssar | CANCEL_COMP | 취소 완료된 건 |

## AI 작업 지침
- **자동 실행 아님**: `testData.sql`은 Spring Boot가 자동으로 실행하지 않음. H2 콘솔에서 수동 실행하거나 테스트에서 `@Sql("classpath:db/testData.sql")` 사용
- **데이터 추가**: 새 도메인 엔티티를 추가하면 연관 테스트 데이터를 이 파일에 함께 추가
- **비밀번호**: 현재 평문 `1234` 저장 (개발 단계). 운영 전 BCrypt 해시로 교체 필수
- **외래키 순서**: 삽입 순서 중요 (user → zone → site → reservation → payment → refund → notice → gallery → qna → comment → review → image)

## 의존성
- 내부: 모든 도메인 엔티티 테이블 (user_tb, zone_tb, site_tb, reservation_tb, payment_tb, refund_tb, notice_tb, gallery_tb, qna_tb, comment_tb, review_tb, image_tb)
