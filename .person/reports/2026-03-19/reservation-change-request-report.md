# Report: 예약 변경 요청 및 가예약(Lock) 로직 구현 (Phase 3: Task 3-3)

**Reporter:** parkcoding
**Date:** 2026-03-19

## 1. 작업 개요
이미 확정된 예약을 고객이 다른 날짜나 사이트로 변경 요청할 수 있는 기능을 구현하였습니다. 이 과정에서 관리자 승인 전까지 해당 자리를 다른 사람이 예약하지 못하도록 보호하는 **논리적 가예약(Soft Lock)** 시스템을 구축하였습니다.

## 2. 주요 변경 사항

### 2.1 가예약(Soft Lock) 쿼리 고도화
- **ReservationRepository**: `findAvailableSites` 및 `existsBySiteIdAndDateRange` 쿼리를 수정하였습니다.
- **핵심 로직**: 기존에는 `PENDING`, `CONFIRMED`인 예약만 체크했으나, 이제는 `ReservationChangeRequest` 테이블에서 `PENDING`(승인 대기) 상태인 요청들이 점유하려는 **새로운 날짜와 사이트**도 조회 결과에서 제외합니다.

### 2.2 예약 변경 프로세스 구축
- **GET `/mypage/reservations/{id}/change`**: 기존 예약 정보를 바탕으로 변경 폼을 렌더링합니다.
- **POST `/mypage/reservations/change-request`**: 사용자의 변경 요청을 검증하고 저장합니다.
- **ReservationService.requestChange**:
    - 본인 확인(Exception403) 및 중복 예약 체크(Exception400) 수행.
    - 원본 예약 상태를 `CHANGE_REQ`로 전환하여 목록에서 수정/취소 버튼을 숨깁니다.
    - `ReservationChangeRequest`를 생성하여 신규 정보를 DB에 기록합니다.

### 2.3 뷰(Mustache) 연동
- `reservation-change.mustache`: 기존 예약 정보를 동적으로 바인딩하고, 체크인/체크아웃 날짜 및 인원을 입력받아 POST로 전송하도록 폼을 완성하였습니다.

## 3. 핵심 실행 흐름 및 코드 해설 (Deep Dive)

### Step 1: 중복 예약 체크의 진화 (Repository)
가장 중요한 부분은 다른 사용자가 사이트를 검색할 때, "변경 요청 중인 자리"도 피해 가도록 만드는 것입니다.

```sql
-- ReservationRepository.java 중 일부
SELECT s FROM Site s WHERE s.id NOT IN (
  -- 1. 이미 확정된 예약들 제외
  SELECT r.site.id FROM Reservation r WHERE r.status IN :statuses ...
) AND s.id NOT IN (
  -- 2. 누군가 '변경 요청' 중인 새로운 자리들도 제외 (Lock!)
  SELECT cr.newSite.id FROM ReservationChangeRequest cr 
  WHERE cr.status = 'PENDING' AND (cr.newCheckIn < :checkOut AND cr.newCheckOut > :checkIn)
)
```

### Step 2: 서비스의 역할 (Service)
서비스는 원본을 지키면서 새로운 요청서를 작성하는 역할을 합니다.

```java
@Transactional
public void requestChange(ReservationRequest.ChangeDTO dto, LoginDTO sessionUser) {
    // 1. "내 예약 맞나?" 검증
    Reservation reservation = reservationRepository.findById(dto.getReservationId()).orElseThrow(...);
    
    // 2. "변경하려는 자리가 비어있나?" (위에서 만든 Lock 쿼리 사용)
    boolean isExist = reservationRepository.existsBySiteIdAndDateRange(dto.getNewSiteId(), ...);
    if (isExist) throw new Exception400("이미 선점된 자리입니다.");

    // 3. 원본 예약은 '변경 요청 중' 상태로 동결
    reservation.updateStatus(ReservationStatus.CHANGE_REQ);

    // 4. 새로운 요청서(ReservationChangeRequest)를 작성해서 제출
    ReservationChangeRequest changeRequest = ReservationChangeRequest.builder()
            .reservation(reservation)
            .newCheckIn(dto.getNewCheckIn())
            // ... 새 정보들
            .status(RequestStatus.PENDING)
            .build();
    reservationChangeRequestRepository.save(changeRequest);
}
```

## 4. 쉬운 설명 (비유)
이번 기능은 **"호텔 방 바꾸기 대기표"**와 같습니다.
- 고객이 "저 302호에서 505호로 옮기고 싶어요"라고 요청하면, 호텔리어(Service)는 일단 302호 손님의 상태를 **'방 바꾸는 중'**으로 표시합니다.
- 그리고 아직 지배인(Admin)의 승인이 안 났더라도, 505호 문 앞에 **"예약 대기 중인 방입니다"**라는 푯말(Soft Lock)을 세워둡니다.
- 이렇게 함으로써 다른 새로운 손님이 505호에 체크인하는 사고를 방지하고, 승인이 나면 깔끔하게 방을 옮겨줄 수 있게 됩니다.

## 5. 검증 결과
- [x] 변경 요청 시 원본 예약 상태 `CHANGE_REQ`로 정상 변경 확인.
- [x] 동일한 날짜/사이트에 대해 타 사용자의 가용 사이트 조회 시 제외되는 것(Lock) 확인.
- [x] 본인 확인 및 중복 체크 예외 처리 정상 작동 확인.

## 6. 향후 계획
- Task 3-4: 예약 취소 요청 기능 구현.
- Task 4: 관리자 페이지에서 변경/취소 요청 승인 및 거절 기능 구현.
