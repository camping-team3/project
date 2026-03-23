# Report: 예약 방문자 정보(성함, 연락처) 누락 문제 해결 보고서

**Date**: 2026-03-23
**Reporter**: parkcoding (Git user.name)
**Status**: RESOLVED

## 🔍 문제 현상 (Issue Description)
고객이 예약 결제 화면에서 방문자 성함과 휴대폰 번호를 올바르게 입력하고 결제를 완료했음에도 불구하고, DB의 `reservation_tb` 테이블에 해당 정보가 `null`로 저장되거나 정보가 누락되는 현상이 발생했습니다.

## ⚠️ 원인 분석 (Root Cause Analysis)

### 1. 프론트엔드 데이터 전송 누락
기존 `payment.mustache` 파일의 JavaScript 로직에서 PortOne 결제 성공 후, 사용자가 입력한 방문자 정보를 서버의 예약 확정 API(`/reservations/reserve`)로 전송하는 POST 요청 과정이 생략되어 있었습니다. 결제 성공 시 단순히 결과 페이지만 보여주는 리다이렉트(`location.href`)만 수행되어 서버로 데이터가 전달되지 않았습니다.

### 2. 백엔드 상태 관리 및 정보 업데이트 부재
`ReservationService.reserve` 메서드에서 가예약(`PENDING`) 시점에 생성된 기존 예약 엔티티를 찾아 업데이트하지 않고, 매번 새로운 예약 엔티티를 생성하려는 시도가 있었습니다. 또한, 전달받은 DTO의 방문자 정보가 유효한지 검증하고 엔티티에 반영하는 로직이 프론트엔드와의 연결 끊김으로 인해 정상 작동하지 않았습니다.

## 🛠️ 조치 사항 (Action Taken)

### 1. 프론트엔드 (`payment.mustache`) 수정
결제 성공 직후, 사용자가 화면에 입력한 방문자 정보를 서버로 확실히 전송하기 위해 HTML Form의 hidden 필드를 활용하고 자동 제출 로직을 추가했습니다.

**[수정된 폼 구조]**
```html
<form id="payment-form" action="/reservations/reserve" method="POST">
    <!-- 가예약 정보 및 방문자 정보를 위한 hidden 필드 추가 -->
    <input type="hidden" name="visitorName" id="hiddenVisitorName">
    <input type="hidden" name="visitorPhone" id="hiddenVisitorPhone">
    ...
</form>
```

**[수정된 결제 성공 처리 스크립트]**
```javascript
// PortOne 결제 성공 시 호출되는 로직
if (response.code == null) {
    // 1. 화면의 입력값을 hidden 필드에 동기화
    document.getElementById('hiddenVisitorName').value = visitorNameInput.value.trim();
    document.getElementById('hiddenVisitorPhone').value = visitorPhoneInput.value.trim();
    
    // 2. 서버의 /reservations/reserve 로 폼 최종 제출
    document.getElementById('payment-form').submit();
}
```

### 2. 백엔드 (`ReservationService.java`) 로직 보완
`reserve` 메서드에서 가예약 데이터를 재사용하고 방문자 정보를 명시적으로 업데이트하도록 수정했습니다.

**[수정된 reserve 메서드 주요 로직]**
```java
@Transactional
public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request, LoginDTO sessionUser) {
    // 1. 기존 PENDING 상태의 가예약 엔티티 조회 (중복 생성 방지)
    Reservation reservation = reservationRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
            .stream()
            .filter(r -> r.getStatus() == ReservationStatus.PENDING)
            .findFirst()
            .orElseGet(() -> Reservation.builder()...build());

    // 2. 전달받은 방문자 정보를 엔티티에 반영
    reservation.updateReservationInfo(
            request.getCheckIn(),
            request.getCheckOut(),
            site,
            request.getPeopleCount(),
            request.getVisitorName(),  // DTO에서 전달받은 방문자 성함
            request.getVisitorPhone()  // DTO에서 전달받은 방문자 연락처
    );

    // 3. 예약 상태를 CONFIRMED로 확정
    reservation.updateStatus(ReservationStatus.CONFIRMED);
    reservationRepository.save(reservation);
}
```

## ✅ 결과 및 검증
- **데이터 무결성 확보**: 결제 성공 시 사용자가 입력한 방문자 정보가 서버로 정확히 전달되어 DB(`visitor_name`, `visitor_phone` 컬럼)에 정상 저장됨을 확인했습니다.
- **예약 번호 일관성**: 가예약 시 생성된 ID를 그대로 사용하여 예약 확정 처리가 이루어지므로 데이터 중복 문제가 해결되었습니다.

---
**비유로 설명하기**: 이번 조치는 '결제창'이라는 정거장에서 멈췄던 '방문자 정보'라는 승객을, 결제가 완료되자마자 '예약 확정'이라는 기차에 태워 종착지인 'DB'까지 무사히 운행시킨 것과 같습니다. 이전에는 승객을 정거장에 남겨두고 기차만 혼자 떠났던 셈입니다.
