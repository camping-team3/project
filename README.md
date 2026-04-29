# Forest Haven Camping 예약/관리 시스템

동일 사이트·동일 기간 중복 예약을 방지하기 위해 조회 단계 + 예약 확정 직전 이중 검증 구조를 적용한 SSR 기반 캠핑장 예약/관리 시스템입니다.
고객 예약 흐름과 관리자 예약 승인/거절 흐름 중 **예약 도메인 전반**을 담당했습니다.

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

- 예약 확정 직전 서버 재검증을 통한 동일 사이트·동일 기간 중복 예약 방지
- 예약 변경/취소 요청 엔티티 분리를 통한 원본 데이터 보호 및 이력 관리 구조 설계
- 예약 상태(`PENDING`, `CONFIRMED`, `CHANGE_REQ`, `CANCEL_REQ`) 기반 도메인 로직 구현
- 관리자 변경/취소 요청 승인·거절 및 거절 사유 저장 흐름 구현

## 핵심 구현 요약

### 1. 중복 예약 방지

- 예약 가능 조회 + 예약 확정 직전 서버 재검증을 통해 동일 사이트·동일 기간 중복 예약 방지
- 승인 대기 중인 변경 요청의 희망 사이트까지 포함하여 충돌 가능성 최소화
- 조회 시점과 저장 시점 간 데이터 불일치 문제를 고려한 구조

### 2. 변경/취소 요청 분리 설계

- Reservation(원본)과 ReservationChangeRequest, ReservationCancelRequest를 분리 설계
- 승인 전 원본 데이터 보호, 승인 시에만 반영 / 거절 시 요청 이력 및 사유 저장 구조 구현

### 3. 관리자 승인/거절 플로우

- 변경/취소 요청을 승인/거절 기반 상태 전이 흐름으로 처리
- 승인 시 원본 예약 갱신, 거절 시 상태 복구 및 거절 사유 저장

## 문제 해결 경험

| 문제 | 해결 방식 | 핵심 포인트 |
| --- | --- | --- |
| 예약 변경/취소 요청을 원본 예약에 바로 반영하면 거절 시 복구와 이력 관리가 어려움 | `Reservation` 원본 데이터와 `ReservationChangeRequest`, `ReservationCancelRequest` 요청 데이터를 분리하고, 승인 시에만 원본 예약에 반영 | 상태 관리, 데이터 보호 설계 |
| 예약 가능 목록 조회 시점과 실제 예약 확정 시점이 달라 중복 예약 가능성이 존재 | 조회 단계에서 1차 필터링 후, 결제 화면 진입 및 예약 확정 직전에 서버에서 동일 사이트·날짜 범위 충돌 재검증 | 서버 중심 검증, 데이터 정합성 확보 |
| 예약 기능과 사이트 기능의 경계가 불명확해 캠핑장/사이트 선택 기능이 중복 구현됨 | 기능 통합 과정에서 공통 기능의 소유 주체와 도메인별 책임 범위를 재정의할 필요성을 인식 | 책임 분리, 재사용 구조 이해 |

## 보완하고 싶은 점

- 현재는 예약 확정 직전에 서버에서 중복 검증을 수행하지만,
  동시 요청 상황에서는 검증 시점이 겹칠 수 있어 완전히 안전하지 않을 수 있음

- 이를 보완하기 위해 DB 제약조건 또는 비관적/낙관적 락을 활용한
  동시성 제어 적용 필요성 인식

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
