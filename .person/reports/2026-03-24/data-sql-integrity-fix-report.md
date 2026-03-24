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
