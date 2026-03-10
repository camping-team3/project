<!-- Parent: ../AI-CONTEXT.md -->

# domain/site/

## 목적
캠핑 사이트(공간) 및 요금 설정 관리. 구역(Zone)별 가격 정책과 개별 사이트 정보를 관리한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Site.java` | 사이트 엔티티 (zone_id, name, max_people) |
| `SiteService.java` | 사이트 가용 여부 및 가격 계산 로직 |
| `SiteRepository.java` | 구역 및 사이트 정보 조회 |

## AI 작업 지침
- **가격 정책**: `Zone` 테이블에 정의된 `normal_price`와 `peak_price`를 바탕으로 성수기 여부를 판단하여 합계 금액을 산출한다.
- **가용성**: 예약 내역(`Reservation`)과 대조하여 특정 날짜의 예약 가능 여부를 반환한다.

## 의존성
- 외부: Spring Data JPA
