<!-- Parent: ../AI-CONTEXT.md -->

# domain/payment/

## 목적
외부 결제 연동(PortOne) 및 결제 이력 관리. 예약 확정과 연계되어 트랜잭션을 처리한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Payment.java` | 결제 엔티티 (imp_uid, amount, status) |

## AI 작업 지침
- **트랜잭션**: 결제 성공 정보가 수신되면 `Reservation`의 상태를 `CONFIRMED`로 변경하는 작업이 원자적으로 수행되어야 한다.
- **검증**: 클라이언트에서 넘어온 결제 금액과 실제 예약 금액이 일치하는지 서버 측 검증이 필수적이다.

## 의존성
- 내부: `domain/reservation` (참조)
- 외부: PortOne V2 API
