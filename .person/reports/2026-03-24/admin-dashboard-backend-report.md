# Report: 관리자 대시보드 백엔드 데이터 연동 상세 가이드

- **Date**: 2026-03-24
- **Reporter**: parkcoding
- **Task**: 관리자 대시보드에 예약 요청 통계 및 목록 공급 로직 구현

## 🚀 전체 실행 흐름 (Execution Flow)
사용자가 관리자 페이지(`/admin`)에 접속하면 다음과 같은 순서로 데이터가 처리됩니다.
1. **Controller**: 브라우저의 요청을 받아 `ReservationService`에게 필요한 데이터를 요청합니다.
2. **Service**: 데이터베이스 조회를 위해 `ReservationRepository`를 호출하고, DB에서 가져온 엔티티(`Reservation`)를 화면에 그리기 적합한 `DTO`로 가공합니다.
3. **Repository**: 실제 데이터베이스에 쿼리를 날려 변경/취소 요청 상태인 데이터를 찾아옵니다.
4. **Result**: 가공된 데이터가 `Model`에 담겨 Mustache 템플릿으로 전달되어 대시보드 화면이 완성됩니다.

---

## 🛠️ 계층별 코드 및 상세 설명

### 1. Repository 계층 (`ReservationRepository.java`)
**역할**: 데이터베이스와의 직접적인 통신을 담당합니다. 필요한 데이터를 효율적으로 가져오기 위한 쿼리를 정의합니다.

```java
/**
 * [추가] 특정 상태의 예약 건수 조회 (대시보드 위젯 통계용)
 * 역할: DB에서 특정 ReservationStatus를 가진 행의 개수를 단순 카운트합니다.
 */
long countByStatus(ReservationStatus status);

/**
 * [추가] 여러 상태에 해당하는 예약 목록 조회 (페이징 지원)
 * 역할: 변경 요청(CHANGE_REQ)과 취소 요청(CANCEL_REQ)을 한꺼번에 조회합니다.
 * 특징: JOIN FETCH를 사용하여 연관된 User, Site, Zone 정보를 한 번의 쿼리로 가져와 성능을 최적화(N+1 문제 해결)했습니다.
 */
@Query("SELECT r FROM Reservation r JOIN FETCH r.user JOIN FETCH r.site s JOIN FETCH s.zone " +
        "WHERE r.status IN :statuses")
Page<Reservation> findByStatuses(@Param("statuses") List<ReservationStatus> statuses, Pageable pageable);
```

### 2. Service 계층 (`ReservationService.java`)
**역할**: 비즈니스 로직을 처리합니다. Repository에서 가져온 로우 데이터(Entity)를 UI에 맞게 변환(DTO)하고 필요한 계산을 수행합니다.

```java
/**
 * [추가] 대시보드용 예약 통계 데이터 생성
 * 설명: Repository를 호출하여 변경 요청 건수와 취소 요청 건수를 각각 구한 후, Map에 담아 반환합니다.
 */
public Map<String, Long> getDashboardStatistics() {
    return Map.of(
        "changeReqCount", reservationRepository.countByStatus(ReservationStatus.CHANGE_REQ),
        "cancelReqCount", reservationRepository.countByStatus(ReservationStatus.CANCEL_REQ)
    );
}

/**
 * [추가] 대시보드용 미처리 요청 목록 가공
 * 설명: 
 *  1. CHANGE_REQ, CANCEL_REQ 상태를 리스트로 묶어 Repository에 전달합니다.
 *  2. DB에서 조회된 Page<Reservation> 엔티티들을 AdminResponse.ReservationListDTO로 변환합니다.
 *  3. 이 과정에서 'CHANGE_REQ'는 '변경 요청'으로, 'CANCEL_REQ'는 '취소 요청'으로 한글 텍스트를 입힙니다.
 */
public AdminResponse.ReservationPageDTO findPendingRequests(Pageable pageable) {
    List<ReservationStatus> pendingStatuses = List.of(ReservationStatus.CHANGE_REQ, ReservationStatus.CANCEL_REQ);
    Page<Reservation> page = reservationRepository.findByStatuses(pendingStatuses, pageable);

    // 엔티티 -> DTO 변환 과정
    List<AdminResponse.ReservationListDTO> dtoList = page.getContent().stream()
            .map(r -> {
                long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
                String statusText = r.getStatus() == ReservationStatus.CHANGE_REQ ? "변경 요청" : "취소 요청";
                
                return AdminResponse.ReservationListDTO.builder()
                        .id(r.getId())
                        .username(r.getUser().getName())
                        .siteName(r.getSite().getSiteName())
                        .checkIn(r.getCheckIn())
                        .checkOut(r.getCheckOut())
                        .nights(nights)
                        .totalPrice(r.getTotalPrice())
                        .status(r.getStatus())
                        .statusText(statusText)
                        .statusClass("info") // 요청은 파란색(info) 계열로 통일
                        .build();
            }).toList();

    // 페이징 정보와 함께 반환
    return AdminResponse.ReservationPageDTO.builder()
            .reservations(dtoList)
            .pagination(AdminResponse.PaginationDTO.builder()
                    .totalPages(page.getTotalPages())
                    .totalElements(page.getTotalElements())
                    .currentPage(page.getNumber())
                    .hasPrev(page.hasPrevious())
                    .hasNext(page.hasNext())
                    .prevPage(page.getNumber() - 1)
                    .nextPage(page.getNumber() + 1)
                    .build())
            .build();
}
```

### 3. Controller 계층 (`AdminController.java`)
**역할**: 클라이언트의 요청을 매핑하고, Service에서 받은 데이터를 뷰(Mustache)에 전달합니다.

```java
@GetMapping("/admin")
public String dashboard(Model model) {
    // ... 기존 QnA 관련 로직 유지 ...

    // 1. 예약 요청 통계 데이터 주입 (changeReqCount, cancelReqCount)
    Map<String, Long> resStats = reservationService.getDashboardStatistics();
    model.addAllAttributes(resStats);

    // 2. 예약 요청 목록 데이터 주입 (최신 5건, ID 역순 정렬)
    // PageRequest.of(0, 5, Sort.by("id").descending()) 를 통해 '첫 페이지', '5개씩', 'ID 큰 순서대로'를 설정합니다.
    Pageable resPageable = PageRequest.of(0, 5, Sort.by("id").descending());
    AdminResponse.ReservationPageDTO resPageDTO = reservationService.findPendingRequests(resPageable);
    model.addAttribute("pendingRequests", resPageDTO);

    return "admin/dashboard";
}
```

---

## ✅ 학습 포인트
- **DTO의 중요성**: DB 엔티티를 직접 화면으로 보내지 않고 `ReservationListDTO`와 같은 전용 객체로 변환함으로써, 화면에 특화된 정보(한글 상태명, 박수 계산 등)를 안전하게 전달할 수 있습니다.
- **Spring Data JPA 활용**: `countByStatus` 처럼 메서드 이름만으로 쿼리를 생성하거나, `@Query`와 `JOIN FETCH`를 조합해 성능을 조율하는 법을 확인할 수 있습니다.
- **Model 바인딩**: `model.addAllAttributes(Map)`를 사용하면 맵 안의 키값들을 Mustache에서 변수명으로 즉시 사용할 수 있어 편리합니다.
