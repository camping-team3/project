# [보고서] 관리자 예약 요청 상세 페이지 사용자 정보 렌더링 오류 수정

**Reporter**: parkcoding (Git user)
**Date**: 2026-03-23
**Status**: 완료 (Fixed)

## 1. 개요
관리자 페이지의 '예약 변경 요청 상세' 및 '예약 취소 요청 상세' 화면을 기존 단일 컬럼에서 현대적인 2컬럼 레이아웃으로 개선하는 과정에서, 우측 사이드바에 배치된 **사용자(예약자/방문자) 정보 카드**의 데이터가 정상적으로 표시되지 않는 문제가 발생했습니다. 이를 해결하기 위해 DTO 구조를 개선하고 머스타치 템플릿의 변수 매핑을 수정했습니다.

## 2. 오류 원인 (Root Cause)
- **DTO 필드 누락**: 기존 `ReservationResponse.AdminChangeDetailDTO` 및 `AdminCancelDetailDTO`에 우측 사이드바 렌더링에 필요한 `userId`, `visitorName`, `visitorPhone` 필드가 포함되어 있지 않았습니다.
- **템플릿 변수 불일치**: 머스타치 파일(`*-detail.mustache`)에서 사용자의 고유 ID나 방문자 상세 정보를 호출하고 있었으나, 컨트롤러에서 넘겨주는 데이터 객체에 해당 값이 존재하지 않아 빈 값으로 출력되었습니다.

## 3. 조치 내용 (Resolution)

### 3.1 DTO 클래스 수정 (`ReservationResponse.java`)
각 상세 페이지용 DTO에 사용자 관련 필드를 추가하여 뷰에서 필요한 데이터를 모두 제공하도록 개선했습니다.

```java
// ReservationResponse.java 내부 수정된 DTO 예시
public static class AdminChangeDetailDTO {
    private Long id;
    // ... 중략
    private Long userId;          // 사용자 상세 페이지 이동을 위한 ID 추가
    private String visitorName;   // 방문자 성함 추가
    private String visitorPhone;  // 방문자 연락처 추가

    public AdminChangeDetailDTO(Reservation reservation, ReservationChangeRequest request) {
        this.id = reservation.getId();
        // ... 중략
        this.userId = reservation.getUser().getId();
        this.visitorName = reservation.getVisitorName();
        this.visitorPhone = reservation.getVisitorPhone();
    }
}
```

### 3.2 머스타치 템플릿 수정 (`*-detail.mustache`)
추가된 DTO 필드를 활용하여 사용자 정보 카드를 동적으로 렌더링하도록 수정했습니다.

```html
<!-- change-detail.mustache & cancel-detail.mustache 공통 수정 사항 -->
<div class="fh-card p-4 mb-4 shadow-sm">
    <h3 class="h6 fw-bold mb-4 d-flex align-items-center gap-2">
        <span class="material-symbols-outlined text-primary">person</span>
        예약자 및 방문자 정보
    </h3>
    <!-- ... 중략 ... -->
    <div class="d-flex flex-column gap-3">
        <div class="d-flex justify-content-between">
            <span class="small text-muted">방문자 성함</span>
            <span class="small fw-bold">{{detail.visitorName}}</span>
        </div>
        <div class="d-flex justify-content-between border-top pt-2">
            <span class="small text-muted">비상 연락처</span>
            <span class="small fw-bold">{{detail.visitorPhone}}</span>
        </div>
    </div>
    <!-- 사용자 상세 정보 이동 버튼에 userId 연결 -->
    <button onclick="location.href='/admin/users/{{detail.userId}}'" class="btn btn-outline-primary btn-sm w-100 mt-4 fw-bold rounded-2">
        사용자 상세 정보 보기
    </button>
</div>
```

## 4. 메서드 단위 설명

### `AdminChangeDetailDTO` / `AdminCancelDetailDTO` 생성자
- **기능**: 엔티티(`Reservation`, `Request`)를 뷰에 적합한 데이터 구조로 변환합니다.
- **수정 내용**: `reservation.getUser().getId()`를 통해 사용자 식별자를 확보하고, `Reservation` 엔티티에 저장된 방문자 정보를 DTO 필드에 직접 매핑하도록 로직을 추가했습니다. 이는 뷰 레이어에서 복잡한 객체 탐색 없이 바로 `detail.visitorName`과 같은 단순 경로로 데이터에 접근할 수 있게 합니다.

## 5. 종합 결론
이번 수정으로 관리자는 예약 변경/취소 요청을 검토할 때, 해당 고객이 누구인지(방문자 정보 포함) 즉시 확인하고 필요 시 '사용자 상세 정보 보기' 버튼을 통해 고객의 전체 예약 이력을 빠르게 조회할 수 있게 되었습니다. 

**비유를 통한 설명**: 
마치 택배 기사님이 주소(예약 정보)만 보고 배송하다가, 이제는 받는 사람의 성함과 연락처(사용자 정보)가 적힌 **'스마트 알림창'**이 옆에 새로 생긴 것과 같습니다. 덕분에 기사님은 누구에게 물건을 전달해야 하는지 훨씬 명확하게 알 수 있게 되었습니다.
