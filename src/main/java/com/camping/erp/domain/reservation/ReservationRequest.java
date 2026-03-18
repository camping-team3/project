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
}
