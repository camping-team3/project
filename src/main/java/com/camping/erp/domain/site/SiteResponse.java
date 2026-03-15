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
        private Integer basePeople; // 기준 인원 필드 추가
        private Long pricePerNight; // 1박 요금 필드 추가
        private Boolean isAvailable;

        @Builder
        public ListDTO(Long id, String siteName, String zoneName, Integer maxPeople, Integer basePeople, Long pricePerNight, Boolean isAvailable) {
            this.id = id;
            this.siteName = siteName;
            this.zoneName = zoneName;
            this.maxPeople = maxPeople;
            this.basePeople = basePeople;
            this.pricePerNight = pricePerNight;
            this.isAvailable = isAvailable;
        }
    }

    @Getter
    @Setter
    public static class DetailDTO {
    }
}
