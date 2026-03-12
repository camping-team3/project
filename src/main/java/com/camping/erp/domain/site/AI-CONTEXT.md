<!-- Parent: ../AI-CONTEXT.md -->

# domain/site/

## 목적
캠핑 공간 및 요금 정책 관리. 구역(Zone)과 개별 사이트(Site) 정보를 관리하고, 사용자의 날짜별 예약 가용성을 판단한다.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Zone.java` / `ZoneRepository.java` | 구역 엔티티 및 요금 정책 관리 리포지토리 |
| `Site.java` / `SiteRepository.java` | 사이트 엔티티 및 **날짜별 가용성 체크 쿼리** 포함 |
| `ZoneService.java` / `SiteService.java` | 구역/사이트 CRUD 및 예약 가능 목록 필터링 비즈니스 로직 |
| `SiteController.java` | **사용자 및 관리자 통합 컨트롤러** (루트 검색 및 /admin/site/** 관리 기능 포함) |
| `SiteRequest.java` / `SiteResponse.java` | **엔티티 기반 생성자 패턴**이 적용된 DTO |

## AI 작업 지침
- **예약 가용성**: `SiteRepository.findAvailableSites` 쿼리를 사용하여 `Reservation` 도메인을 직접 수정하지 않고도 중복 예약을 필터링해야 한다.
- **DTO 패턴**: `SiteResponse`의 모든 DTO는 엔티티를 매개변수로 받는 생성자를 통해 데이터를 생성하며, `set` 메서드 사용을 금지한다.
- **가격 산출**: `SiteService.getAvailableZonesWithSites` 내에서 숙박 기간에 따른 총 요금 계산 로직을 유지 및 관리한다.
