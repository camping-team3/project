package com.camping.erp.domain.reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class ReservationRequest {

    @Getter
    @Setter
    public static class SearchDTO {
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Integer peopleCount;
        private Long zoneId;
    }

    @Getter
    @Setter
    public static class ReserveDTO {
        private Long siteId;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Integer peopleCount;
        private Long totalPrice;
        private String visitorName; // 방문객 성함 추가
        private String visitorPhone; // 방문객 연락처 추가
    }

    @Getter
    @Setter
    public static class ChangeDTO {
        private Long reservationId;
        private Long newSiteId;
        private LocalDate newCheckIn;
        private LocalDate newCheckOut;
        private Integer newPeopleCount;
        private String reason;
    }

    @Getter
    @Setter
    public static class CancelDTO {
        private Long reservationId;
        private String reason; // 취소 사유
        private String refundBank; // 환불 은행
        private String refundAccount; // 환불 계좌번호
        private String refundAccountHolder; // 예금주
    }
}
