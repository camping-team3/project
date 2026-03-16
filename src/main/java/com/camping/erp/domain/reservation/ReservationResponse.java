package com.camping.erp.domain.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class ReservationResponse {

    @Getter @Setter
    public static class PaymentFormDTO {
        private Long siteId;
        private String siteName;
        private String zoneName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private long nights;
        private Integer peopleCount;
        private Integer basePeople;
        private Long pricePerNight;
        private Long extraPrice;
        private Long totalPrice;

        @Builder
        public PaymentFormDTO(Long siteId, String siteName, String zoneName, LocalDate checkIn, LocalDate checkOut, long nights, Integer peopleCount, Integer basePeople, Long pricePerNight, Long extraPrice, Long totalPrice) {
            this.siteId = siteId;
            this.siteName = siteName;
            this.zoneName = zoneName;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.nights = nights;
            this.peopleCount = peopleCount;
            this.basePeople = basePeople;
            this.pricePerNight = pricePerNight;
            this.extraPrice = extraPrice;
            this.totalPrice = totalPrice;
        }
    }

    @Getter @Setter
    @Builder
    public static class ReserveDTO {
        private Long id;
        private String siteName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Long totalPrice;
    }

    @Getter @Setter
    @Builder
    public static class CompleteDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private long nights;
        private Integer peopleCount;
        private Long totalPrice;
        private String username;
    }
}
