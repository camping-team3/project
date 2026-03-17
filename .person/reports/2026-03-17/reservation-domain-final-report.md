# Reservation 도메인 통합 리포트 (Phase 2 완료)
날짜: 2026-03-17
작성자: parkcoding

## 🗺️ 1. 전체 흐름 (Flow)

예약 도메인은 사용자가 날짜를 선택하는 순간부터 결제를 마치고 관리자가 확인하기까지의 일련의 과정을 다룹니다.

1.  **검색 및 가용성 확인 (Search)**: 사용자가 체크인/체크아웃 날짜와 인원을 선택합니다. 시스템은 해당 기간에 이미 예약된 사이트를 제외한 '예약 가능한 사이트 목록'을 반환합니다.
2.  **선택 및 동적 요금 계산 (Select)**: 지도의 핀이나 리스트를 통해 사이트를 선택하면, 기준 인원 초과 시 추가 요금이 동적으로 계산되어 우측 요약창에 표시됩니다.
3.  **결제 정보 확인 (Payment Form)**: 선택한 정보(날짜, 사이트, 인원, 금액)를 들고 결제 페이지로 이동하여 최종 정보를 확인합니다.
4.  **예약 확정 (Reserve)**: 시스템은 결제 직전, '그 짧은 사이'에 누군가 먼저 예약하지 않았는지 마지막으로 중복 체크를 수행합니다. 안전하다면 예약을 확정(`CONFIRMED`)하고 DB에 저장합니다.
5.  **관리자 확인 (Admin)**: 관리자는 대시보드에서 상태별, 날짜별로 예약 현황을 조회할 수 있습니다.

---

## 💻 2. 핵심 코드 요약 및 분석

### A. 가용 사이트 조회 (JPQL 쿼리)
이 도메인에서 가장 중요한 로직은 "내가 원하는 날짜에 겹치는 예약이 없는가?"를 찾아내는 것입니다.

```java
// ReservationRepository.java
@Query("SELECT s FROM Site s " +
       "WHERE s.id NOT IN (" +
       "    SELECT r.site.id FROM Reservation r " +
       "    WHERE r.status IN :activeStatuses " +
       "    AND (r.checkIn < :checkOut AND r.checkOut > :checkIn)" + // 💡 핵심 중복 로직
       ") " +
       "AND (:zoneId IS NULL OR s.zone.id = :zoneId) " +
       "AND s.maxPeople >= :peopleCount")
List<Site> findAvailableSites(@Param("checkIn") LocalDate checkIn,
                              @Param("checkOut") LocalDate checkOut,
                              @Param("activeStatuses") List<ReservationStatus> activeStatuses,
                              @Param("zoneId") Long zoneId,
                              @Param("peopleCount") Integer peopleCount);
```
*   **주석 설명**:
    *   `s.id NOT IN (...)`: 괄호 안의 조건에 해당하는 사이트는 빼고(`NOT IN`) 가져오라는 뜻입니다.
    *   `(r.checkIn < :checkOut AND r.checkOut > :checkIn)`: 두 기간이 단 하루라도 겹치는지 확인하는 수학적 공식입니다. 내 체크아웃 날짜보다 기존 예약의 체크인이 빠르고, 내 체크인 날짜보다 기존 예약의 체크아웃이 늦다면 무조건 겹칩니다.

### B. 최종 예약 로직 및 검증 (Service)
프론트엔드(화면)에서 넘어온 가격 데이터는 절대 믿지 않습니다. 서버에서 직접 다시 계산합니다.

```java
// ReservationService.java
@Transactional
public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request, User sessionUser) {
    // 1. 동시성 이슈 방지를 위한 최종 중복 체크
    boolean isExist = reservationRepository.existsBySiteIdAndDateRange(
            request.getSiteId(), request.getCheckIn(), request.getCheckOut(), activeStatuses);
    if (isExist) throw new Exception400("이미 예약된 기간입니다.");

    // 2. 서버 측 가격 재계산 (보안)
    long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
    int extraPeople = Math.max(0, request.getPeopleCount() - 2); 
    long calculatedPrice = (site.getZone().getNormalPrice() + ((long) extraPeople * 10000)) * nights;

    // 3. 엔티티 생성 및 저장
    Reservation reservation = Reservation.builder()
            .totalPrice(calculatedPrice) 
            .build();
    reservationRepository.save(reservation);
}
```

---

## 🧸 3. 초등학생도 이해하는 쉬운 비유

캠핑장 예약을 **'도서관에서 책 빌리기'**라고 상상해 보세요.

1.  **가용 사이트 조회 (Search)**:
    *   "해리포터 책(특정 사이트)을 3월 1일부터 3월 3일까지 빌릴 수 있나요?"
    *   사서(시스템)는 대출 장부(DB)를 확인합니다. "음, 3월 2일에 이미 누가 빌려가기로 되어 있네요. 안 됩니다." (이것이 위에서 본 JPQL 겹침 확인 로직입니다.)
2.  **최종 검증 (Reserve)**:
    *   도서관 컴퓨터로 내가 빌리겠다고 버튼을 누르는 찰나에, 옆 자리 친구도 동시에 똑같은 책의 대출 버튼을 눌렀습니다!
    *   그래서 사서는 책을 건네주기 1초 전에 장부를 **마지막으로 한 번 더 확인**합니다. "어? 방금 0.1초 전에 다른 분이 빌려갔어요!" (이것이 Service의 중복 체크 로직입니다.)
3.  **서버 측 가격 재계산**:
    *   내가 몰래 바코드 가격표를 1,000원으로 고쳐서 계산대로 가져갔습니다.
    *   하지만 계산원(서버)은 바코드를 믿지 않고, 장부에 적힌 원래 가격(DB 데이터)을 보고 "이건 5,000원입니다."라고 다시 계산합니다.

---

## 🧠 4. 어려운 기술/개념 해설

### 1. JPQL 기간 중복 체크 공식
두 기간(A와 B)이 겹치는 것을 코드로 어떻게 짤까요?
`A.start < B.end AND A.end > B.start`
이 공식 하나면 모든 겹치는 경우의 수(일부 겹침, 완전히 포함됨, 감싸안음 등)를 전부 걸러낼 수 있습니다. 이 공식은 캘린더나 예약 시스템을 만들 때 외워두면 평생 쓰는 마법의 공식입니다.

### 2. PRG (Post-Redirect-Get) 패턴
예약을 완료(`POST /reservations`)한 후, 사용자가 실수로 브라우저 '새로고침(F5)'을 누르면 결제나 예약이 두 번 연속으로 요청될 수 있습니다.
이를 막기 위해 서버는 성공적으로 처리가 끝나면 데이터를 반환하는 대신, "성공 페이지로 가세요!"라고 주소를 넘겨줍니다(`Redirect`). 그러면 브라우저는 그 주소로 이동(`GET /reservations/complete`)하기 때문에, 새로고침을 눌러도 그냥 성공 페이지를 다시 볼 뿐 예약이 두 번 되지 않습니다.

### 3. 클라이언트 사이드 필터링 (Interactive Map)
캠핑장 맵에서 A구역을 누를 때마다 서버에 새로 데이터를 달라고 요청하면 느리고 데이터 낭비가 심합니다. 그래서 처음에 모든 구역의 데이터를 화면(클라이언트)에 다 가져다 놓습니다.
그리고 핀을 누르면 화면단(JavaScript)에서 다른 구역은 단순히 `d-none` 클래스를 추가해 '안 보이게' 숨겨버리는 방식을 썼습니다. 서버 통신이 없어 매우 빠르고 부드러운 사용자 경험을 제공합니다.
