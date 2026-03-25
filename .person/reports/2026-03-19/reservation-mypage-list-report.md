# Report: 마이페이지 예약 목록 구현 (Phase 3: Task 3-1)

**Reporter:** parkcoding
**Date:** 2026-03-19

## 1. 작업 개요
Phase 3 '확장 도메인' 개발의 일환으로, 고객이 본인의 예약 내역을 확인하고 상태에 따라 예약 변경/취소 요청을 할 수 있는 **마이페이지 예약 목록 페이지**를 구현하였습니다.

## 2. 주요 변경 사항

### 2.1 컨트롤러 및 도메인 책임 정리
- **ReservationController**: `/mypage/reservations` 매핑을 통해 실제 데이터(DB) 조회 및 비즈니스 로직 처리를 담당하도록 구현하였습니다.
- **UserController**: 기존에 중복으로 존재하던 `/mypage/reservations` 매핑을 제거하여 도메인 간 책임을 명확히 분리하고 런타임 충돌을 방지하였습니다.

### 2.2 데이터 모델 (DTO) 고도화
- **ReservationResponse.ListDTO**:
    - `canModify`: `CONFIRMED` 상태이면서 이용일이 오늘 이후인 경우에만 '변경/취소' 버튼 노출.
    - `isWait`: `CHANGE_REQ` 또는 `CANCEL_REQ` 상태인 경우 '승인 대기 중' 상태 표시.
    - `isCompleted`: 이용 완료 상태 제어.
    - **에러 수정**: `Site` 엔티티에 이미지 연관관계가 정의되지 않아 발생하던 컴파일 에러를 해결하기 위해, 임시로 `upload` 폴더의 캠핑장 이미지를 기본값으로 사용하도록 로직을 안정화하였습니다.

## 3. 핵심 실행 흐름 및 코드 해설 (Deep Dive)

이 기능이 작동하는 전체 과정을 단계별로 분석합니다.

### Step 1: 컨트롤러 (진입점 및 데이터 취합)
`ReservationController.java`의 `reservations` 메서드가 요청을 가장 먼저 받습니다.

```java
@GetMapping("/mypage/reservations")
public String reservations(Model model) {
    // 1. 세션에서 로그인한 사용자 정보를 가져옴 (누구의 예약을 찾을지 결정)
    UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
    if (sessionUser == null) {
        return "redirect:/login-form"; // 로그인 안 되어 있으면 쫓아냄
    }

    // 2. 서비스 레이어에 해당 사용자의 모든 예약 목록을 최신순으로 요청
    List<Reservation> reservationList = reservationService.findByUserIdOrderByCreatedAtDesc(sessionUser.getId());

    // 3. Entity 목록을 화면에 보여주기 적합한 DTO 목록으로 변환 (중요한 비즈니스 로직 포함)
    LocalDate today = LocalDate.now();
    List<ReservationResponse.ListDTO> dtos = reservationList.stream()
            .map(r -> ReservationResponse.ListDTO.fromEntity(r, today))
            .toList();

    // 4. 변환된 데이터를 모델에 담아 머스타치 뷰로 전달
    model.addAttribute("reservations", dtos);
    model.addAttribute("userName", sessionUser.getName());
    return "mypage/reservations";
}
```

### Step 2: 서비스 및 리포지토리 (데이터 조회)
DB에서 순수 데이터를 가져오는 역할입니다.

```java
// ReservationService.java
public List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId) {
    // Repository를 통해 DB에서 '작성일 역순'으로 예약 정보를 긁어옴
    return reservationRepository.findByUserIdOrderByCreatedAtDesc(userId);
}
```

### Step 3: DTO 변환 (비즈니스 판단의 핵심)
`ReservationResponse.java`의 `fromEntity` 메서드는 DB 데이터(`Entity`)를 화면에 "어떻게 보여줄지" 결정하는 **통역사** 역할을 합니다.

```java
public static ListDTO fromEntity(Reservation reservation, LocalDate today) {
    // 1. 날짜를 "2024.03.19 (목)" 형태로 예쁘게 변환
    String dayOfWeek = reservation.getCheckIn().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    
    return ListDTO.builder()
            .id(reservation.getId())
            .siteName(reservation.getSite().getSiteName())
            .totalPrice(String.format("%,d원", reservation.getTotalPrice())) // 100,000원 처럼 콤마 추가
            
            // 2. [가장 중요] 버튼 노출 여부 계산
            // 예약 확정(CONFIRMED) 상태이고, 체크인 날짜가 오늘보다 미래여야만 '수정/취소' 가능
            .canModify(reservation.getStatus() == ReservationStatus.CONFIRMED && reservation.getCheckIn().isAfter(today))
            
            // 3. 승인 대기 상태 여부 계산
            // 변경 요청(CHANGE_REQ)이나 취소 요청(CANCEL_REQ) 중이면 '대기 중' 표시
            .isWait(reservation.getStatus() == ReservationStatus.CHANGE_REQ || reservation.getStatus() == ReservationStatus.CANCEL_REQ)
            
            // 4. 화면에 보여줄 상태 설명 텍스트
            .statusDescription(reservation.getStatus() == ReservationStatus.CHANGE_REQ ? "변경 승인 대기" :
                             reservation.getStatus() == ReservationStatus.CANCEL_REQ ? "취소 승인 대기" : "")
            .build();
}
```

### Step 4: 뷰 렌더링 (사용자에게 최종 전달)
`reservations.mustache`에서 DTO가 계산해 준 플래그(`boolean`)를 사용하여 화면을 동적으로 구성합니다.

```html
<!-- DTO의 canModify가 true일 때만 이 버튼들이 화면에 나타남 -->
{{#canModify}}
    <a href="/mypage/reservation/change-form/{{id}}" class="btn btn-primary">예약 변경</a>
    <a href="/mypage/reservation/cancel-form/{{id}}" class="btn btn-outline-secondary">예약 취소</a>
{{/canModify}}

<!-- DTO의 isWait이 true일 때만 '승인 대기 중' 문구가 나타남 -->
{{#isWait}}
    <span class="text-warning">{{statusDescription}} 중</span>
{{/isWait}}
```

## 4. 쉬운 설명 (비유)
이번 작업은 마치 **"개인 비서가 내 스케줄러를 예쁘게 정리해 준 것"**과 같습니다.
- **컨트롤러**는 비서입니다. 주인의 이름표를 확인하고 서류함(DB)에서 예약 서류를 꺼내옵니다.
- **DTO(통역사)**는 서류의 내용을 꼼꼼히 체크합니다. "이 예약은 이미 다녀오셨네? 그럼 취소 버튼은 가리자", "이건 지금 사장님 결재 대기 중이네?"라고 판단하여 형광펜으로 표시(Flag)를 남깁니다.
- **머스타치(화면)**는 비서가 정리해 준 노트를 보고, 형광펜 표시가 있는 부분만 골라서 예쁘게 액자에 담아 주인(사용자)에게 보여주는 역할을 합니다.

## 5. 검증 결과
- [x] 세션 사용자 기반 예약 목록 조회 정상 작동 확인.
- [x] 예약 상태(`CONFIRMED`, `CHANGE_REQ`, `CANCEL_REQ`, `COMPLETED`)에 따른 버튼 노출 및 텍스트 표시 로직 검증 완료.
- [x] 컴파일 에러 해결 및 뷰 렌더링 정상 확인.

## 6. 향후 계획
- Task 3-2: 예약 상세 페이지 구현 및 요청 이력 리스트 출력.
- Task 3-3 & 3-4: 실제 예약 변경 및 취소 요청 폼/로직 구현.
