# Image Entity and Data Integration Report
Date: 2026-03-17
Reporter: Taehyeok (Assistant)
Task: Image Entity Modification and data.sql Section Consolidation

## Summary
The `Image` entity and its corresponding initial data in `data.sql` were fragmented and inconsistent due to concurrent development. This task involved unifying the `Image` entity's fields and consolidating the `image_tb` insert statements in `data.sql` into a single, comprehensive section.

## Changes
### 1. Domain: Image Entity (`Image.java`)
- Added missing `@ManyToOne` and `@JoinColumn` annotations for `notice`, `zone`, and `site` fields.
- Updated the `@Builder` to include the `notice` field.
- Ensured consistency between all relationship fields (Gallery, Review, Notice, Zone, Site).

### 2. Database: Initial Data (`data.sql`)
- Merged "11. Gallery Image Management" into "10. Image Management".
- Unified multiple `INSERT INTO image_tb` statements into one.
- Standardized the column list to include all 5 relationship IDs (`gallery_id`, `review_id`, `notice_id`, `zone_id`, `site_id`).
- Re-indexed the IDs for `image_tb` to be sequential (1 to 6).

## Analogy (Easy Explanation)
기존에는 사진첩이 여기저기 흩어져 있고, 어떤 사진은 제목이 있고 어떤 사진은 날짜가 없는 등 제각각이었습니다. 이번 작업을 통해 모든 사진을 하나의 '통합 앨범'으로 합치고, 각 사진마다 공지사항용인지, 리뷰용인지, 혹은 캠핑장 구역 사진인지를 명확하게 표시할 수 있는 통일된 라벨을 붙여준 것과 같습니다.

## Verification
- `Image.java` compile check (visual)
- `data.sql` syntax and constraint check (visual)
