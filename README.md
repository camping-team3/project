# Forest Haven Camping 예약/관리 시스템

SSR 기반 캠핑장 예약 및 관리자 승인 처리 시스템입니다. 고객 예약 흐름과 관리자 예약 승인/거절 흐름 중 **예약 도메인 전반**을 담당했습니다.

## 프로젝트 정보

| 항목 | 내용 |
| --- | --- |
| 기간 | 2026.02.26 ~ 2026.03.25 |
| 유형 | 학원 팀 프로젝트 |
| 아키텍처 | Spring MVC 기반 SSR |
| 담당 범위 | 예약 도메인 |
| 담당 브랜치 | `feat/reservation`, `feat/reservation-phase3` |

## 기술 스택

`Java` `Spring Boot` `Spring MVC` `Spring Data JPA` `MySQL` `Mustache` `Gradle`

## 담당 개발

- 날짜/인원 조건 기반 예약 가능 사이트 조회 및 중복 예약 방지 로직 구현
- 예약 확정 전 서버 측 재검증을 통해 동일 사이트·동일 기간 중복 예약 방지
- 예약 변경/취소 요청을 별도 엔티티로 분리하여 원본 예약과 요청 이력 관리
- 관리자 페이지에서 변경/취소 요청 상세 조회, 승인, 거절 및 거절 사유 저장 흐름 구현
- `PENDING`, `CONFIRMED`, `CHANGE_REQ`, `CANCEL_REQ` 등 예약 상태 기반 도메인 로직 설계

결제 연동 자체는 팀원이 담당했고, 저는 예약 가능 조회, 예약 데이터 처리, 예약 확정 전 중복 검증, 변경/취소 요청 및 관리자 승인 흐름을 담당했습니다.

## 핵심 구현 요약

### 1. 중복 예약 방지

예약 화면에서는 조건에 맞는 예약 가능 사이트만 조회하고, 예약 확정 전 서버에서 동일 사이트·동일 기간 예약 여부를 다시 검증했습니다. 승인 대기 중인 변경 요청의 희망 사이트도 조회 조건에 포함하여 충돌 가능성을 줄였습니다.

### 2. 변경/취소 요청 분리 설계

원본 예약을 바로 수정하지 않고 `ReservationChangeRequest`, `ReservationCancelRequest`를 별도 엔티티로 분리했습니다. 이를 통해 승인 전 원본 예약을 유지하고, 거절 시 요청 상태와 거절 사유를 이력으로 남길 수 있게 했습니다.

### 3. 관리자 승인/거절 플로우

관리자는 예약 목록과 대시보드에서 변경/취소 요청을 확인하고 승인 또는 거절할 수 있습니다. 승인 시 원본 예약 상태를 갱신하고, 거절 시 원본 예약을 확정 상태로 복구하며 거절 사유를 저장합니다.

## 주요 파일

| 구분 | 파일 |
| --- | --- |
| 예약 컨트롤러 | `src/main/java/com/camping/erp/domain/reservation/ReservationController.java` |
| 예약 서비스 | `src/main/java/com/camping/erp/domain/reservation/ReservationService.java` |
| 예약 Repository | `src/main/java/com/camping/erp/domain/reservation/ReservationRepository.java` |
| 변경 요청 엔티티 | `src/main/java/com/camping/erp/domain/reservation/ReservationChangeRequest.java` |
| 취소 요청 엔티티 | `src/main/java/com/camping/erp/domain/reservation/ReservationCancelRequest.java` |
| 관리자 컨트롤러 | `src/main/java/com/camping/erp/domain/admin/AdminController.java` |

## 상세 문서

- [예약 도메인 상세 구현 문서](docs/reservation-portfolio.md)

## 실행 및 테스트

```bash
./gradlew bootRun
./gradlew test
```

로컬 실행 시 DB 접속 정보와 외부 결제 설정은 환경에 맞게 별도 설정이 필요합니다.
