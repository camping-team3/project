package com.camping.erp.domain.site;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class SiteResponse {

    @Getter
    @Setter
    public static class ListDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private Integer maxPeople;
        private Long pricePerNight; // 1박 요금 추가
        private Long totalPrice;     // 숙박 일수 포함 총액 추가
        private Boolean isAvailable;

        @Builder
        public ListDTO(Long id, String siteName, String zoneName, Integer maxPeople, Long pricePerNight, Long totalPrice, Boolean isAvailable) {
            this.id = id;
            this.siteName = siteName;
            this.zoneName = zoneName;
            this.maxPeople = maxPeople;
            this.pricePerNight = pricePerNight;
            this.totalPrice = totalPrice;
            this.isAvailable = isAvailable;
        }
    }

    @Getter
    @Setter
    public static class DetailDTO {
    }
}
