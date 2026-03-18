# Data SQL Integrity Fix Report
Date: 2026-03-17
Reporter: Taehyeok (Assistant)
Task: Fixing H2 IntegrityConstraintViolation in data.sql

## Summary
When starting the server, a `JdbcSQLIntegrityConstraintViolationException` occurred because `image_tb` was attempting to reference non-existent records in `gallery_tb`. This was caused by the omission of `gallery_tb` insert statements and incorrect execution order of SQL scripts.

## Changes
### 1. Database: Initial Data (`data.sql`)
- **Added `gallery_tb` Data**: Inserted sample records for `gallery_tb` (IDs 1 and 2) to satisfy foreign key requirements for `image_tb`.
- **Reordered Insertions**: Moved the `image_tb` insert statement to the very end of the file. This ensures that all parent tables (`user_tb`, `zone_tb`, `site_tb`, `notice_tb`, `gallery_tb`, `review_tb`) are populated before the child table (`image_tb`) references them.

## Analogy (Easy Explanation)
도서관에 책(이미지)을 꽂으려고 하는데, 책꽂이(갤러리) 자체가 아직 만들어지지 않아서 책을 꽂을 수 없는 상황이었습니다. 이번 작업을 통해 먼저 튼튼한 책꽂이를 만들고, 그 다음에 책을 순서대로 꽂도록 순서를 바로잡았습니다. 이제 도서관(서버)이 문제없이 문을 열 수 있습니다.

## Verification
- Verified the presence of `gallery_tb` before `image_tb` in `data.sql`.
- Confirmed all foreign key IDs match between tables.
- Server should now start without data initialization errors.
