# Gallery Column Mismatch Fix Report
Date: 2026-03-17
Reporter: Taehyeok (Assistant)
Task: Fixing NULL constraint violation for gallery_tb in data.sql

## Summary
The server failed to start due to a `JdbcSQLIntegrityConstraintViolationException: NULL not allowed for column "VIEW_COUNT"`. This was caused by `data.sql` not providing values for the `shooting_date` and `view_count` columns, which are marked as `NOT NULL` in the `Gallery` entity.

## Changes
### 1. Database: Initial Data (`data.sql`)
- **Updated `gallery_tb` INSERT**: Added `shooting_date` and `view_count` to the column list and provided sample values (`'2026-03-01'`, `0`) for each row.
- **Data Consistency**: Ensured that the initial data matches the current `Gallery.java` entity structure.

## Analogy (Easy Explanation)
출입 명부(갤러리)에 '이름'과 '내용'만 적고 들어가려 했는데, 입구에서 '방문 날짜(촬영일)'와 '방문 횟수(조회수)'도 필수로 적어야 한다고 막아 세운 상황이었습니다. 명부에 빠진 항목들을 꼼꼼히 적어 넣어 이제 아무 문제 없이 입장(서버 실행)할 수 있게 되었습니다.

## Verification
- Confirmed all `NOT NULL` columns in `Gallery.java` are now included in `data.sql`.
- Verified SQL syntax for the updated `INSERT` statement.
