# 📋 [보고서] 캠핑장 예약 시스템 중복 예약 방지 메커니즘 분석

**작성일**: 2026-03-20  
**Reporter**: parkcoding
**주제**: 신규 예약 및 예약 변경 시 데이터 정합성 보장 로직 검토

---

## 1. 개요
본 시스템은 사용자가 화면에서 가용 사이트를 조회하는 단계뿐만 아니라, **실제 DB에 데이터를 저장하기 직전(Service Layer)**에 최종 검증을 수행하여 0.1초 사이의 동시 요청이나 비정상적인 접근으로 인한 중복 예약을 원천 차단합니다.

## 2. 핵심 검증 쿼리 (Repository Layer)
모든 중복 체크의 근간이 되는 쿼리입니다. 단순히 기존 예약만 보는 것이 아니라, **'관리자가 승인 대기 중인 변경 요청'**까지 포함하여 검사합니다.

### [ReservationRepository.java]
```java
@Query("SELECT (COUNT(r) > 0 OR COUNT(cr) > 0) FROM Site s " +
        "LEFT JOIN Reservation r ON r.site.id = s.id " +
        "  AND r.status IN :statuses " +
        "  AND (r.checkIn < :checkOut AND r.checkOut > :checkIn) " +
        "LEFT JOIN ReservationChangeRequest cr ON cr.newSite.id = s.id " +
        "  AND cr.status = 'PENDING' " +
        "  AND (cr.newCheckIn < :checkOut AND cr.newCheckOut > :checkIn) " +
        "WHERE s.id = :siteId")
boolean existsBySiteIdAndDateRange(...);
```
- **날짜 겹침 로직**: `(r.checkIn < :checkOut AND r.checkOut > :checkIn)` 공식을 사용하여 시작일과 종료일이 하루라도 겹치는 모든 케이스를 잡아냅니다.
- **이중 체크(Double-Track)**: 
    1. `Reservation` 테이블: 이미 확정되었거나 결제 대기 중인 예약 확인.
    2. `ReservationChangeRequest` 테이블: 다른 사용자가 해당 자리로 옮기겠다고 신청하여 승인을 기다리는 상태(가예약) 확인.

---

## 3. 서비스 단계별 방어 로직 (Service Layer)

### 3.1 신규 예약 생성 시 (`reserve` 메서드)
사용자가 결제 버튼을 누르는 순간, 서버 측에서 다시 한번 해당 자리가 비어 있는지 확인합니다.

```java
// [ReservationService.java]
public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request, ...) {
    // 1. 활성 상태 정의 (대기, 확정, 변경 요청 중 포함)
    List<ReservationStatus> activeStatuses = List.of(
            ReservationStatus.PENDING, 
            ReservationStatus.CONFIRMED, 
            ReservationStatus.CHANGE_REQ
    );

    // 2. 최종 중복 체크 실행
    boolean isExist = reservationRepository.existsBySiteIdAndDateRange(
            site.getId(), request.getCheckIn(), request.getCheckOut(), activeStatuses);

    if (isExist) {
        throw new Exception400("이미 예약된 기간입니다."); // 중복 발생 시 예외 송출 및 트랜잭션 롤백
    }
    
    // 3. 검증 통과 시에만 예약 저장 진행...
}
```

### 3.2 예약 변경 요청 시 (`requestChange` 메서드)
기존 예약자가 날짜나 사이트를 바꾸려 할 때, 옮기려는 자리가 '진짜' 비어 있는지 검증합니다.

```java
// [ReservationService.java]
public void requestChange(ReservationRequest.ChangeDTO dto, ...) {
    // 옮기려는 새로운 사이트(newSiteId)에 대해 동일한 중복 체크 수행
    boolean isExist = reservationRepository.existsBySiteIdAndDateRange(
            newSite.getId(), dto.getNewCheckIn(), dto.getNewCheckOut(), activeStatuses);

    if (isExist) {
        throw new Exception400("해당 기간은 이미 예약되었거나 변경 요청 중인 자리입니다.");
    }

    // 검증 성공 시 원본 예약 상태를 'CHANGE_REQ'로 변경하여 원본 자리도 보호
    reservation.updateStatus(ReservationStatus.CHANGE_REQ);
    // ... 변경 요청 정보 저장
}
```

---

## 4. 추가 보안 및 정합성 전략 (핵심 요약)

1.  **가예약(Lock) 효과**: 
    - 사용자가 변경 요청을 보내면, 관리자가 승인하기 전이라도 해당 자리는 `ReservationChangeRequest` 테이블에 `PENDING` 상태로 기록됩니다. 
    - 위에서 언급한 `existsBySiteIdAndDateRange` 쿼리가 이 `PENDING` 건을 읽어내기 때문에, **관리자 승인 전에도 타인이 해당 자리를 예약하는 것을 자동으로 방어**합니다.
2.  **원천 데이터 보호**:
    - 단순히 클라이언트(JS) 단의 체크에 의존하지 않고, 서버 트랜잭션 내에서 직접 쿼리를 날려 확인하므로 매크로 등을 이용한 비정상 요청도 차단 가능합니다.
3.  **상태값의 엄격한 관리**:
    - `CHANGE_REQ` 상태인 예약 건은 '현재 사용 중인 자리'와 '옮겨갈 예정인 자리' 양쪽 모두에 대해 점유권을 가집니다. 이를 통해 승인 프로세스 도중 발생할 수 있는 데이터 공백을 방지합니다.

---

**결론**: 본 시스템은 이중 테이블 조회 및 상태 기반 검증 로직을 통해 **신규 예약, 변경 요청, 관리자 승인 전 대기 상태** 등 모든 프로세스에서 예약 중복이 발생할 수 없도록 설계되었습니다.