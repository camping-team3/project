<!-- Parent: ../AI-CONTEXT.md -->

# domain/payment/

## 목적
결제 승인 및 환불 이력 관리. 포트원 연동 정보와 예약 상태를 연계한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Payment.java` | 결제 성공 이력 (impUid, amount, payDate 등) |
| `Refund.java` | 환불 승인 이력 (reason, refundAmount 등) |
| `PaymentRepository.java` | 결제 데이터 접근 |
| `PaymentService.java` | 결제 검증 및 확정 로직 (뼈대 생성됨) |

## AI 작업 지침
- **원자성**: 결제 성공 시 `Reservation`의 상태 변경과 `Payment` 저장이 하나의 트랜잭션으로 묶여야 한다.
- **환불**: 관리자 승인 후 `Refund` 엔티티가 생성되어야 한다.
