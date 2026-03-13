package com.camping.erp.domain.site;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class SiteResponse {

    @Getter @Setter
    public static class ListDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private Integer maxPeople;
        private Long price;
        private Boolean isAvailable;

        @Builder
        public ListDTO(Long id, String siteName, String zoneName, Integer maxPeople, Long price, Boolean isAvailable) {
            this.id = id;
            this.siteName = siteName;
            this.zoneName = zoneName;
            this.maxPeople = maxPeople;
            this.price = price;
            this.isAvailable = isAvailable;
        }
    }
}
