# 📄 Phase 1: 도메인 설계 및 JPA 엔티티 매핑 보고서

## 1. 작업 개요
- **작업 일시**: 2026-03-11
- **목표**: ERD 명세에 따른 데이터베이스 모델링 및 JPA 엔티티 매핑, 도메인별 기본 구조 확립

## 2. 주요 수행 내용
- **BaseTimeEntity 도입**: 모든 엔티티에 `createdAt`, `updatedAt` 자동 기록 설정 완료.
- **도메인 엔티티 매핑 (25%)**:
  - `User`: 회원 관리 및 권한 체계 구축.
  - `Zone/Site`: 캠핑 구역 및 사이트 구조화.
  - `Reservation`: 예약 상태 및 기간 관리 매핑.
  - `Payment/Refund`: 결제 및 환불 이력 추적 구조화.
  - `Board/Notice/Gallery/Qna/Review/Image`: 커뮤니티 및 이미지 관리 시스템 매핑.
- **클래스 뼈대 생성**:
  - 각 도메인별 `Repository`, `Service`, `DTO` 파일 총 20개 이상 생성.
  - 사용자 직접 구현을 위해 내부 메서드 로직은 비워둔 상태로 유지.

## 3. 결과물 위치
- 엔티티: `src/main/java/com/camping/erp/domain/{domain}/{Entity}.java`
- 서비스/레포지토리/DTO: 동일 경로 하위 관련 파일
- 공통 설정: `src/main/java/com/camping/erp/global/_core/BaseTimeEntity.java`

## 4. 향후 계획 (Phase 2)
- 회원가입 및 로그인 기능 구현 (UserService 채우기)
- 날짜별 예약 가능 사이트 조회 로직 개발
- 결제 연동 및 동시성 제어(Redis Lock) 도입 준비

---
*본 보고서는 AI-GUIDE 및 Workflow에 따라 작성되었습니다.*
