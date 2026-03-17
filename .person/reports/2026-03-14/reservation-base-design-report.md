# reservation 데이터 기초 설계 리포트
2026-03-14
Reporter: parkcoding
작업 단계: [Task 1-1] 데이터 기초 설계 (Entity & DTO)

## 작업 요약
예약 기능 구현을 위한 엔티티 수정 및 DTO(Search, Reserve, Response) 설계를 완료했습니다.

## 변경 사항
- **Reservation 엔티티**: 예약 인원 관리를 위한 `peopleCount` 필드를 추가하고 빌더에 반영했습니다.
- **ReservationRequest DTO**: 
    - `SearchDTO`: 날짜, 인원, 구역별 가용 사이트 검색을 위한 필드 정의.
    - `ReserveDTO`: 실제 예약 저장 시 필요한 필드 정의.
- **SiteResponse DTO**: 
    - `ListDTO`: 예약 페이지에서 사이트 정보를 렌더링하기 위한 데이터 구조 정의.

## 검증 결과
- 엔티티와 DTO 간의 필드 정합성을 확인했습니다.
- 컴파일 에러가 없는지 구조적 무결성을 검토했습니다.

## 설명
이번 작업은 건물을 짓기 전 **[기둥(Entity)]**을 세우고, 각 층으로 물건을 나를 **[상자(DTO)]**들을 규격화한 것과 같습니다. 특히, 몇 명이 올 것인지 담는 칸(`peopleCount`)을 기둥에 새로 만들어서, 나중에 "우리 사이트에 꽉 찼나?"를 정확히 계산할 수 있는 기반을 마련했습니다.
