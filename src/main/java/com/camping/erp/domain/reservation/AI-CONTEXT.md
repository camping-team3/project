<!-- Parent: ../AI-CONTEXT.md -->

# domain/reservation/

## 목적
실시간 예약 및 일정 관리. 중복 예약 방지와 예약 라이프사이클을 관리한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Reservation.java` | 예약 엔티티 (user, site, checkIn, checkOut, totalPrice, status) |
| `ReservationController.java` | 예약 신청 폼, 결제 페이지, 완료 페이지 경로 매핑 |
| `ReservationService.java` | 예약 생성 및 취소 핵심 비즈니스 로직 |
| `ReservationRepository.java` | 예약 엔티티에 대한 Spring Data JPA 레포지토리 |
| `ReservationRequest.java` | 예약 관련 요청 DTO (내부 클래스 ReserveDTO 포함) |
| `ReservationResponse.java` | 예약 관련 응답 DTO (내부 클래스 ReserveDTO 포함) |

## 하위 디렉토리
- `enums/` - 예약 상태(`ReservationStatus`) 정의

## AI 작업 지침
- **상태 변화**: `PENDING` -> `CONFIRMED` -> `CANCEL_REQ` -> `CANCEL_COMP` 흐름을 따른다.
- **예약 생성**: 사이트 중복 예약 방지를 위해 기간 중복 체크 로직이 필수적이다.
- **동시성**: 고부하 상황을 대비하여 락(Lock) 메커니즘을 고려해야 한다.
- **컨벤션**: 모든 엔티티는 `BaseTimeEntity`를 상속하며, 비즈니스 로직은 Service 계층에 위치시킨다.

## 테스트
- 특정 기간 내 사이트 중복 예약 방지 로직 검증 테스트 필수.
- 상태 전이(예: 결제 완료 시 CONFIRMED 변경) 로직 테스트.

## 의존성
- 내부: `com.camping.erp.domain.user.User`, `com.camping.erp.domain.site.Site`, `com.camping.erp.global.BaseTimeEntity`
- 외부: Spring Data JPA, Lombok, Jakarta Persistence
