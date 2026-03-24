# 📋 데이터 정합성 오류 해결 보고서 (data.sql)

- **Date**: 2026-03-24
- **Reporter**: Gemini CLI (G)
- **Task**: `db/data.sql` 실행 중 발생한 NULL 제약 조건 위반 오류 해결

---

## 🔍 문제 현황 및 원인 분석

### 1. 현상 (Issue)
애플리케이션 기동 시 `db/data.sql` 스크립트 실행 단계에서 `BeanCreationException` 및 `JdbcSQLIntegrityConstraintViolationException`이 발생하며 실행이 중단되었습니다.

### 2. 원인 (Root Cause)
최근 예약 변경 및 취소 요청(`ReservationChangeRequest`, `ReservationCancelRequest`) 엔티티에 정산 및 환불 완료 여부를 위한 필수 컬럼들이 추가되었습니다.
그러나 `src/main/resources/db/data.sql` 내의 테스트 데이터(Seed Data) INSERT 문에는 해당 컬럼값들이 누락되어 있어, H2 DB의 `NOT NULL` 제약 조건을 위반하게 되었습니다.

---

## 🛠️ 해결 조치 (Changes)

### 1. `reservation_change_request` 테이블 데이터 수정
- **누락 필드 추가**: `old_total_price`, `new_total_price`, `settlement_type`, `is_refunded`
- **데이터 보정**: 원본 예약 정보를 기반으로 금액(100,000원)과 정산 타입(`NONE`)을 정확히 기입하였습니다.

### 2. `reservation_cancel_request` 테이블 데이터 수정
- **누락 필드 추가**: `refund_amount`, `is_refunded`
- **데이터 보정**: 취소 요청 건에 대해 예상 환불 금액(300,000원)과 환불 상태(`FALSE`)를 기입하였습니다.

---

## ✅ 검증 결과 (Validation)

- [x] **엔티티 명세 일치**: `ReservationChangeRequest`, `ReservationCancelRequest` 엔티티의 `@Column(nullable = false)` 설정과 INSERT 문 컬럼 구조가 일치함을 확인했습니다.
- [x] **Enum 값 검증**: `SettlementType`(`ADDITIONAL_PAY`, `PARTIAL_REFUND`, `NONE`) 및 `RequestStatus`(`PENDING`, `APPROVED`, `REJECTED`)의 문자열 값이 올바르게 입력되었습니다.
- [x] **머지 충돌 확인**: 프로젝트 전체에 대해 Git 충돌 마커(`<<<<<<<`, `=======`, `>>>>>>>`)가 없음을 `grep_search`로 최종 확인했습니다.

---

## 💡 한 줄 요약 및 비유
**"새로운 신분증 양식에 '거주지' 항목이 추가되었는데, 기존 명부에는 그 칸이 비어 있어 수리가 거부되던 상황이었습니다. 비어 있던 칸들을 정확한 정보로 채워 넣어 다시 정상적으로 접수되도록 조치했습니다."**
# Report: 예약 데이터 정합성 수정 및 data.sql 구조 개선 (2차 수정)

**Date:** 2026-03-24
**Reporter:** KimNaKim
**Phase:** Phase 3.3 관리자 예약 관리 (Admin Side) - 준비 단계

## 1. 작업 요약
- 관리자 페이지에서 예약 변경/취소 요청이 조회되지 않는 문제를 해결하기 위해 `data.sql`의 데이터를 보충함.
- `reservation_tb`의 상태값과 실제 상세 요청 테이블 간의 불일치를 해소함.
- `data.sql` 내의 `INSERT` 구문들을 테이블별로 그룹화하여 가독성을 높임.
- **(추가)** `review_tb` 컬럼 불일치로 인한 애플리케이션 구동 실패 문제를 해결함.

## 2. 상세 변경 사항
### 데이터 보충 및 정합성 (Data Integrity)
- **예약 취소 요청 추가**: 예약 ID 4번(이게스트)과 10번(김연아)의 데이터를 `reservation_cancel_request` 테이블에 추가하여 관리자 페이지 조회가 가능하도록 함.
- **리뷰 테이블 수정**: `Review` 엔티티 구조와 일치하지 않던 `view_count`, `is_best`, `is_active`, `parent_id` 컬럼을 제거하고, `ai_danger_score`, `is_reviewed`, `is_deleted` 컬럼을 추가함.

### SQL 구조 정리 (Grouping)
- **섹션화**: 쿼리를 10개의 주요 섹션으로 분리하고 의존 순서(부모 -> 자식)에 맞게 재배치함.
- **한글 복구**: 깨져 보이던 한글 데이터 및 주석을 정상화함.

## 3. 검증 결과
- **구동 테스트**: `review_tb` 컬럼 수정 후 SQL 스크립트 실행 오류가 해결됨을 확인함.
- **정적 검증**: 모든 `CANCEL_REQ`, `CHANGE_REQ` 상태의 예약이 상세 요청 테이블과 정상적으로 매핑됨.

## 4. 비유로 설명하는 작업 내용
이번 작업은 **"서류함 정리(data.sql 구조 개선)"**뿐만 아니라 **"잘못 인쇄된 서류 양식(review_tb 컬럼 오류)"**을 바로잡은 것과 같습니다. 양식이 틀리면 아무리 신청서를 잘 써도 시스템이 거부하듯이, 엔티티 양식에 맞춰 `data.sql`을 완벽하게 동기화했습니다. 이제 막혔던 데이터 흐름이 뻥 뚫렸습니다!
