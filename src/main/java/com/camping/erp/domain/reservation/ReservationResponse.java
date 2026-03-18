package com.camping.erp.domain.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class ReservationResponse {

    @Getter
    @Setter
    @Builder
    public static class PaymentFormDTO {
        private Long siteId;
        private String siteName;
        private String zoneName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Long nights;
        private Integer peopleCount;
        private Integer basePeople;
        private Long pricePerNight;
        private Long extraPrice;
        private Long totalPrice;
        private Long extraPersonFee; // 추가 요금 단가 추가
    }

    @Getter
    @Setter
    @Builder
    public static class ReserveDTO {
        private Long id;
        private String siteName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Long totalPrice;
    }

    @Getter
    @Setter
    @Builder
    public static class CompleteDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Long nights;
        private Integer peopleCount;
        private Long totalPrice;
        private String username;
    }
}
