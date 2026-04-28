# Forest Haven Camping ERP

단일 캠핑장 운영을 위한 서버 사이드 렌더링 기반 예약/관리 시스템입니다. 고객은 날짜와 인원 조건으로 예약 가능한 사이트를 조회하고 예약, 변경 요청, 취소 요청을 진행할 수 있으며, 관리자는 대시보드에서 예약 현황과 변경/취소 요청을 확인하고 승인 또는 거절할 수 있습니다.

## 프로젝트 정보

| 항목 | 내용 |
| --- | --- |
| 프로젝트 유형 | 학원 팀 프로젝트 |
| 아키텍처 | Spring MVC 기반 SSR |
| 담당 범위 | 예약 도메인 전반 |
| 담당 브랜치 | `feat/reservation`, `feat/reservation-phase3` |
| 포트폴리오 브랜치 | `portfolio-reservation` |

## 기술 스택

| 구분 | 기술 |
| --- | --- |
| Backend | Java 21, Spring Boot 3.3.4, Spring MVC |
| Database | MySQL, H2 |
| ORM | Spring Data JPA |
| Template | Mustache |
| Security | HttpSession, HandlerInterceptor |
| Validation | Bean Validation, 서버 측 비즈니스 검증 |
| Build | Gradle |

## 주요 기능

- 회원 기반 캠핑장 예약
- 날짜, 구역, 인원 조건에 따른 예약 가능 사이트 조회
- 결제 화면 진입 시 예약 선점 상태 생성
- 예약 확정 직전 서버 측 중복 예약 재검증
- 마이페이지 예약 목록 및 상세 조회
- 고객 예약 변경 요청
- 고객 예약 취소 요청
- 관리자 예약 목록, 상세, 변경 요청 상세, 취소 요청 상세
- 관리자 변경/취소 요청 승인 및 거절
- 관리자 대시보드의 미처리 예약 요청 현황 표시

## 담당 역할

예약 도메인에서 고객 화면부터 관리자 처리 화면까지 이어지는 전체 흐름을 담당했습니다.

- 고객 신규 예약 화면 및 예약 가능 사이트 조회 흐름 구현
- 예약 확정 전 동일 사이트/동일 날짜 범위 중복 예약 검증 구현
- 마이페이지 예약 목록, 상세, 변경 요청, 취소 요청 화면 구현
- 예약 변경 요청 데이터 모델 및 처리 흐름 구현
- 예약 취소 요청 데이터 모델 및 처리 흐름 구현
- 관리자 예약 목록, 예약 상세, 변경 요청 상세, 취소 요청 상세 화면 구현
- 관리자 변경/취소 요청 승인 및 거절 처리 구현
- 관리자 대시보드에 변경/취소 요청 건수와 미처리 요청 목록 표시

결제 연동 자체는 팀원이 담당했고, 저는 결제 화면 진입 전후 예약 데이터 처리와 예약 확정 전 중복 검증, 예약 상태 전환 흐름을 담당했습니다.

## 예약 도메인 설계

### 예약 상태

예약은 `ReservationStatus`로 현재 상태를 관리합니다.

| 상태 | 의미 |
| --- | --- |
| `PENDING` | 결제 대기 또는 선점 상태 |
| `CONFIRMED` | 예약 확정 |
| `CHANGE_REQ` | 변경 요청 승인 대기 |
| `CANCEL_REQ` | 취소 요청 승인 대기 |
| `CANCEL_COMP` | 취소 완료 |
| `COMPLETED` | 이용 완료 |

### 변경/취소 요청 분리

원본 예약 데이터와 고객 요청 데이터를 분리하기 위해 변경 요청과 취소 요청을 별도 엔티티로 관리했습니다.

- `Reservation`: 확정된 원본 예약 정보
- `ReservationChangeRequest`: 변경 희망 날짜, 사이트, 인원, 변경 전후 금액, 정산 타입, 요청 상태, 거절 사유
- `ReservationCancelRequest`: 취소 사유, 환불 계좌 정보, 요청 상태, 환불 완료 여부, 거절 사유

이 구조를 통해 관리자가 요청을 거절해도 원본 예약을 유지할 수 있고, 요청 이력과 거절 사유를 별도로 남길 수 있습니다.

## 핵심 구현

### 1. 예약 가능 사이트 조회 및 중복 예약 방지

예약 화면에서는 사용자가 선택한 체크인, 체크아웃, 구역, 인원 조건을 기준으로 예약 가능한 사이트만 조회합니다. 서버에서는 이미 예약된 사이트뿐 아니라 승인 대기 중인 변경 요청의 희망 사이트까지 함께 제외합니다.

또한 결제/예약 저장 흐름에서 한 번 더 동일 사이트와 날짜 범위의 예약 여부를 검증하여, 클라이언트 화면에서 가능한 사이트로 보였더라도 최종 저장 시점에 충돌이 있으면 예약을 막도록 구현했습니다.

관련 코드:

- `ReservationService.findAvailableSites`
- `ReservationService.getPaymentForm`
- `ReservationService.reserve`
- `ReservationRepository.findAvailableSites`
- `ReservationRepository.existsBySiteIdAndDateRange`
- `ReservationRepository.existsBySiteIdAndDateRangeExcludingSelf`

### 2. 예약 변경 요청 플로우

고객이 확정된 예약에 대해 변경을 요청하면, 기존 예약을 바로 수정하지 않고 변경 요청 테이블에 희망 정보를 저장합니다.

처리 흐름:

1. 본인 예약 여부 검증
2. 예약 상태가 `CONFIRMED`인지 검증
3. 변경 희망 사이트/날짜의 중복 예약 여부 검증
4. 변경 후 금액 재계산
5. 기존 금액과 비교하여 `ADDITIONAL_PAY`, `PARTIAL_REFUND`, `NONE` 정산 타입 결정
6. 예약 상태를 `CHANGE_REQ`로 변경
7. 변경 요청을 `PENDING` 상태로 저장

관리자가 승인하면 원본 예약의 날짜, 사이트, 인원, 금액을 변경 요청 값으로 갱신하고 예약 상태를 다시 `CONFIRMED`로 되돌립니다. 거절하면 요청 상태를 `REJECTED`로 바꾸고 거절 사유를 저장한 뒤 원본 예약은 유지합니다.

관련 코드:

- `ReservationService.requestChange`
- `ReservationService.approveRequest`
- `ReservationService.rejectRequest`
- `ReservationChangeRequest`
- `SettlementType`
- `RequestStatus`

### 3. 예약 취소 요청 플로우

고객이 예약 취소를 요청하면 즉시 취소하지 않고 취소 요청 테이블에 취소 사유와 환불 계좌 정보를 저장합니다.

처리 흐름:

1. 본인 예약 여부 검증
2. 예약 상태가 `CONFIRMED`인지 검증
3. 온라인 취소 가능 기간 검증
4. 예약 상태를 `CANCEL_REQ`로 변경
5. 취소 요청을 `PENDING` 상태로 저장

관리자가 승인하면 요청 상태를 `APPROVED`로 변경하고 예약 상태를 `CANCEL_COMP`로 변경합니다. 거절하면 요청 상태를 `REJECTED`로 바꾸고 거절 사유를 저장한 뒤 예약 상태를 `CONFIRMED`로 복구합니다.

관련 코드:

- `ReservationService.requestCancel`
- `ReservationService.approveRequest`
- `ReservationService.rejectRequest`
- `ReservationCancelRequest`

### 4. 관리자 예약 관리

관리자는 예약 목록에서 검색, 상태 필터, 페이징을 통해 예약을 확인할 수 있습니다. 예약 상태에 따라 일반 상세, 변경 요청 상세, 취소 요청 상세 화면으로 이동하며, 변경/취소 요청에 대해 승인 또는 거절 처리를 할 수 있습니다.

관리자 대시보드에는 미처리 변경 요청 수, 미처리 취소 요청 수, 최신 미처리 요청 목록을 표시했습니다.

관련 코드:

- `AdminController.dashboard`
- `AdminController.reservationList`
- `AdminController.reservationChangeDetail`
- `AdminController.reservationCancelDetail`
- `AdminController.approveReservation`
- `AdminController.rejectReservation`
- `ReservationService.findAllForAdmin`
- `ReservationService.getDashboardStatistics`
- `ReservationService.findPendingRequests`

## 문제 해결 경험

### 원본 예약과 요청 데이터를 분리해야 하는 문제

예약 변경/취소 요청을 원본 예약에 바로 반영하면 관리자가 거절했을 때 이전 상태로 복구하기 어렵고, 요청 이력을 남기기도 어렵습니다. 이를 해결하기 위해 원본 예약은 `Reservation`에 유지하고, 변경 요청과 취소 요청은 별도 엔티티로 분리했습니다.

그 결과 승인 전까지 원본 예약을 보호할 수 있었고, 승인 시에만 원본 예약을 갱신하며, 거절 시에는 요청 테이블에 거절 사유를 남기고 원본 예약 상태만 복구하는 흐름을 만들 수 있었습니다.

### 예약 가능 목록과 최종 저장 사이의 데이터 불일치 문제

예약 가능 사이트 목록은 화면 진입 시점의 데이터이기 때문에, 사용자가 결제나 예약 확정 단계에 도달하기 전 다른 요청이 먼저 들어올 수 있습니다. 이를 고려해 화면 조회 단계에서 1차로 가능한 사이트만 보여주고, 결제 화면 진입 및 예약 확정 직전에 서버에서 동일 사이트/날짜 범위 충돌을 다시 검사했습니다.

이 방식으로 클라이언트 화면 표시 결과에만 의존하지 않고 서버에서 최종 방어 로직을 수행하도록 했습니다.

## 화면 흐름

### 고객

1. 예약 조건 입력
2. 예약 가능 사이트 목록 조회
3. 예약 정보 확인 및 결제 화면 진입
4. 예약 확정
5. 마이페이지 예약 내역 확인
6. 예약 상세에서 변경 또는 취소 요청
7. 관리자 처리 결과 확인

### 관리자

1. 대시보드에서 미처리 변경/취소 요청 확인
2. 예약 목록에서 상태별 필터링
3. 변경 요청 또는 취소 요청 상세 확인
4. 승인 또는 거절 처리
5. 거절 시 거절 사유 입력

## 주요 파일

| 구분 | 파일 |
| --- | --- |
| 예약 컨트롤러 | `src/main/java/com/camping/erp/domain/reservation/ReservationController.java` |
| 예약 서비스 | `src/main/java/com/camping/erp/domain/reservation/ReservationService.java` |
| 예약 Repository | `src/main/java/com/camping/erp/domain/reservation/ReservationRepository.java` |
| 예약 엔티티 | `src/main/java/com/camping/erp/domain/reservation/Reservation.java` |
| 변경 요청 엔티티 | `src/main/java/com/camping/erp/domain/reservation/ReservationChangeRequest.java` |
| 취소 요청 엔티티 | `src/main/java/com/camping/erp/domain/reservation/ReservationCancelRequest.java` |
| 관리자 컨트롤러 | `src/main/java/com/camping/erp/domain/admin/AdminController.java` |
| 고객 예약 화면 | `src/main/resources/templates/reservation/new.mustache` |
| 마이페이지 예약 화면 | `src/main/resources/templates/mypage/reservations.mustache` |
| 관리자 예약 화면 | `src/main/resources/templates/admin/reservation/list.mustache` |

## 실행 방법

```bash
./gradlew bootRun
```

개발 환경에서는 `application-dev.properties`를 기준으로 실행합니다. DB 접속 정보나 외부 결제 설정은 로컬 환경에 맞게 별도 설정이 필요합니다.

## 테스트

```bash
./gradlew test
```

예약 도메인 통합 테스트는 `src/test/java/com/camping/erp/domain/reservation/ReservationIntegrationTest.java`에서 확인할 수 있습니다.

## 보완하고 싶은 점

- DB 제약조건 또는 비관적/낙관적 락을 이용한 더 강한 동시성 제어
- 결제 성공/실패와 예약 상태 전환의 원자성 강화
- 변경/취소 요청 상태 전환에 대한 테스트 케이스 확대
- 관리자 환불 처리와 요청 승인 흐름의 책임 분리
