# 📊 데이터베이스 ERD 설계 명세 (Database Schema)

본 문서는 캠핑장 예약 시스템의 핵심 데이터 구조와 엔티티 간의 관계를 정의합니다.

---

## 1. 핵심 설계 원칙 (Design Principles)
- **Soft Delete (익명화):** 회원 탈퇴 시 데이터 무결성을 위해 `User` 정보를 익명화(`ANONYMOUS`) 처리하고 이력은 보존한다.
- **연박 관리:** `Reservation`은 `check_in`, `check_out` 필드를 통해 단일 레코드로 관리한다.
- **유연가 정책:** `Zone` 테이블에 평시/성수기 가격을 두며, 별도의 `Season` 테이블로 성수기 기간을 관리한다.
- **인원별 요금:** `Zone`의 기준 인원 초과 시 인당 추가 요금을 합산한다.
- **확장성:** 공지사항, 갤러리, Q&A를 개별 테이블로 분리하여 각 도메인 특화 기능을 지원한다.

---

## 2. 테이블 상세 명세 (Tables)

### 2.1 회원 (User)
| 컬럼명 | 타입 | 제약조건 | 설명 |
| :--- | :--- | :--- | :--- |
| id | BigInt | PK, AI | 고유 번호 |
| username | String | Unique, Not Null | 로그인 ID |
| password | String | Not Null | 암호화된 비밀번호 |
| name | String | Not Null | 실명 |
| email | String | Not Null | 이메일 |
| phone | String | Not Null | 연락처 |
| role | Enum | USER, ADMIN | 권한 (Default: USER) |
| status | Enum | ACTIVE, ANONYMOUS | 계정 상태 |
| created_at | Timestamp | Default Now | 가입 일시 |

### 2.2 공간 및 성수기 관리 (Zone, Site, Season)
- **Zone (구역):** 구역별 공통 설정
  - `id` (PK), `name` (구역명), `normal_price` (평시 요금), `peak_price` (성수기 요금), `base_people` (기준 인원), `extra_person_fee` (인당 추가 요금)
- **Site (상세 위치):** 개별 캠핑 사이트
  - `id` (PK), `zone_id` (FK), `site_name` (A-1 등), `max_people` (최대 수용 인원), `is_available` (예약 가능 여부)
- **Season (성수기 설정):**
  - `id` (PK), `name` (시즌명), `start_date` (시작일), `end_date` (종료일)

### 2.3 예약 및 결제 (Reservation, Payment, Refund)
- **Reservation (예약):**
  - `id` (PK), `user_id` (FK), `site_id` (FK), `check_in`, `check_out`, `total_price`, `people_count` (예약 인원), `visitor_name` (방문자명), `visitor_phone` (방문자 연락처), `status` (PENDING, CONFIRMED, CANCEL_REQ, CANCEL_COMP, COMPLETED)
- **Payment (결제):** 포트원 연동 정보
  - `id` (PK), `reservation_id` (FK), `imp_uid` (결제 고유번호), `amount` (결제 금액), `status`, `pay_date`
- **Refund (환불 이력):**
  - `id` (PK), `reservation_id` (FK), `reason` (취소 사유), `refund_amount` (확정 환불액), `cancelled_at`

### 2.4 커뮤니티 (Notice, Gallery, Qna, Review, Comment)
- **Notice (공지사항):** `id`, `title`, `content`, `is_top` (상단 고정 여부), `created_at`
- **Gallery (포토 갤러리):** 
  - `id` (PK), `title`, `category` (카테고리), `shooting_date` (촬영일), `content` (내용), `view_count` (조회수), `created_at`
- **Qna (질문 답변):** `id`, `user_id` (FK), `title`, `content`, `is_answered` (답변 여부), `created_at`
- **Comment (관리자 답변):** `id`, `qna_id` (FK), `admin_id` (FK), `content`, `created_at`
- **Review (이용 후기):** `id`, `user_id` (FK), `reservation_id` (FK, Unique), `rating` (별점), `content`, `created_at`

### 2.5 파일 관리 (Image)
- **Image (공통 이미지):** 다중 이미지 지원
  - `id` (PK), `gallery_id` (FK, Nullable), `review_id` (FK, Nullable), `notice_id` (FK, Nullable), `zone_id` (FK, Nullable), `site_id` (FK, Nullable), `file_path`, `file_name`

---

## 3. 엔티티 관계도 (Relationships)
1. **User : Reservation** (1:N)
2. **Zone : Site** (1:N)
3. **Site : Reservation** (1:N)
4. **Reservation : Payment** (1:1)
5. **Reservation : Refund** (1:1)
6. **Reservation : Review** (1:1) - 한 예약당 하나의 리뷰만 허용
7. **Qna : Comment** (1:N)
8. **Gallery : Image** (1:N)
9. **Review : Image** (1:N)
10. **Notice : Image** (1:N) - 공지사항 내 첨부 이미지
11. **Zone : Image** (1:N) - 구역 대표/상세 사진
12. **Site : Image** (1:N) - 사이트 상세 사진
13. **User : Review** (1:N)
