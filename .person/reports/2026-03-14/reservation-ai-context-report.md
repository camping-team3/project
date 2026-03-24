# reservation AI-CONTEXT 업데이트 리포트
2026-03-14
Reporter: parkcoding
작업 단계: reservation 도메인 AI-CONTEXT.md 동기화 (deepinit)

## 작업 요약
`reservation` 도메인의 실제 파일 구조와 내용을 분석하여 `AI-CONTEXT.md` 파일을 최신 상태로 업데이트했습니다.

## 변경 사항
- **주요 파일 목록 수정**: 존재하지 않는 `BookingService.java`를 실제 파일인 `ReservationService.java`로 변경하고, `ReservationController.java`와 `ReservationRepository.java` 설명을 추가했습니다.
- **하위 디렉토리 추가**: `enums/` 디렉토리와 `ReservationStatus`에 대한 정보를 명시했습니다.
- **AI 작업 지침 및 테스트 강화**: 중복 예약 방지 로직의 중요성과 테스트 필요성을 강조했습니다.
- **의존성 명시**: 연관된 엔티티(`User`, `Site`) 및 글로벌 설정(`BaseTimeEntity`)을 기재했습니다.

## 검증 결과
- `src/main/java/com/camping/erp/domain/reservation/AI-CONTEXT.md` 파일이 실제 소스 코드와 일치함을 확인했습니다.

## 설명
이 작업은 도서관의 **[색인 카드(AI-CONTEXT)]**를 실제 서가에 있는 **[책들(소스 코드)]**과 일치하도록 정리한 것입니다. 특히, 잘못 적혀 있던 책 이름(`BookingService` -> `ReservationService`)을 바로잡고, 새로 들어온 책들(`Controller`, `Repository`)의 위치를 정확히 기록하여 나중에 AI 에이전트가 이 구역을 방문했을 때 길을 잃지 않도록 안내 지도를 최신화한 것입니다.
