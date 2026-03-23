# Review Rating Recalculation Report

**Date:** 2026-03-23
**Reporter:** handikim
**Task:** Phase 4 - Task 3: 평점 재계산 로직 구현

---

## 📝 작업 요약
관리자가 리뷰를 삭제하거나 유지하기로 결정했을 때, 해당 캠핑장(Site)과 구역(Zone)의 평점 및 리뷰 개수를 실시간으로 정확하게 재계산하여 반영하는 로직을 구현했습니다.

## 🛠️ 변경 사항

### 1. ReviewRepository (`ReviewRepository.java`)
- **활성 리뷰 조회**: `isDeleted = false`인 리뷰들만 골라내는 전용 쿼리(`findActiveReviewsBySiteId`, `findActiveReviewsByZoneId`)를 추가했습니다.

### 2. Site & Zone 엔티티 (`Site.java`, `Zone.java`)
- **일괄 업데이트 메서드**: 기존의 1점씩 가감하는 방식 외에, 외부에서 계산된 전체 통계값을 한 번에 주입할 수 있는 `updateRating(count, avg)` 메서드를 추가했습니다.

### 3. ReviewService (`ReviewService.java`)
- **recalculateAverageRating 구현**: 특정 사이트와 구역의 모든 활성 리뷰를 뒤져서 평균과 개수를 다시 뽑아내는 핵심 로직입니다.
- **트리거 연결**:
    - `save`: 새 리뷰 등록 시 호출.
    - `deleteByAdmin`: 관리자 삭제 시 호출 (삭제된 리뷰 제외 후 재계산).
    - `keepByAdmin`: 관리자가 유지(복구) 결정 시 호출 (다시 평점에 포함).

## ✅ 검증 결과
- 관리자가 리뷰를 삭제하면 즉시 해당 캠핑장의 `reviewCount`가 줄어들고 `avgRating`이 다시 계산되는 흐름 확인.
- 복구(유지) 시에도 평점이 원래대로 돌아오는 정합성 확보.

---

## 💡 비유로 설명하기
이번 작업은 **'학교 성적표 자동 갱신 시스템'을 만든 것**과 같습니다.
- 기존 방식: 학생 한 명이 전학 오거나 가면 기존 점수에서 일일이 더하고 뺐습니다. 계산 실수가 생길 위험이 있었습니다.
- 개선 방식: 학생 명단에 변화가 생기면, 즉시 출석부(DB)를 처음부터 끝까지 다시 훑어서 평균 점수를 새로 뽑습니다.
이 방식을 통해 AI가 비방 리뷰를 걸러내고 관리자가 삭제 버튼을 누를 때마다, 캠핑장 성적표(평점)가 단 1점의 오차도 없이 실시간으로 업데이트됩니다.
