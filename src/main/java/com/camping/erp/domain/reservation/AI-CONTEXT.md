<!-- Parent: ../AI-CONTEXT.md -->

# domain/reservation/

## 목적
실시간 예약 및 일정 관리. 중복 예약 방지와 결제 선점 락(Lock) 처리를 핵심으로 한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Reservation.java` | 예약 엔티티 (site_id, check_in, check_out, status) |
| `BookingService.java` | 예약 생성 로직 및 Redis 분산 락 관리 |
| `ReservationRepository.java` | 예약 내역 조회 및 상태 업데이트 |

## AI 작업 지침
- **동시성 제어**: [결제하기] 진입 시 Redis에 5분 TTL의 Lock 키를 생성하여 오버부킹을 방지한다.
- **예약 라이프사이클**: `PENDING` -> `CONFIRMED` 흐름을 따르며, 취소 시 관리자 승인(`CANCEL_REQ`) 단계를 거친다.
- **쿼리 최적화**: 예약 내역 조회 시 `Site` 엔티티와 `fetch join`을 권장한다.

## 테스트
- 동시성 테스트를 통해 Redis Lock이 정상 작동하는지 검증한다.

## 의존성
- 내부: `domain/site` (참조), `domain/payment` (연동)
- 외부: Redis (Spring Data Redis), Spring Data JPA
