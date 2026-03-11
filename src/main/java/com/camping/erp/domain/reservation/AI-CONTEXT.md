<!-- Parent: ../AI-CONTEXT.md -->

# domain/reservation/

## 목적
실시간 예약 및 일정 관리. 중복 예약 방지와 예약 라이프사이클을 관리한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Reservation.java` | 예약 엔티티 (checkIn, checkOut, totalPrice, status) |
| `ReservationRepository.java` | 예약 내역 조회 및 상태 관리 |
| `BookingService.java` | 예약 생성, 취소 요청 등 핵심 비즈니스 로직 (뼈대 생성됨) |
| `ReservationRequest.java` / `ReservationResponse.java` | 데이터 교환용 DTO |

## AI 작업 지침
- **상태 변화**: `PENDING` -> `CONFIRMED` -> `CANCEL_REQ` -> `CANCEL_COMP` 흐름을 따른다.
- **동시성**: 추후 Redis 등을 이용한 선점 락(Lock) 로직 구현이 필요하다.
