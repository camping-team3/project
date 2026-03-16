package com.camping.erp.domain.site;

import lombok.Getter;
import lombok.Setter;

public class SiteRequest {
    
    @Getter @Setter
    public static class ZoneSaveDTO {
        private String name;
        private Long normalPrice;
        private Long peakPrice;

        public Zone toEntity() {
            return Zone.builder()
                    .name(name)
                    .normalPrice(normalPrice)
                    .peakPrice(peakPrice)
                    .build();
        }
    }

    @Getter @Setter
    public static class SiteSaveDTO {
        private String siteName;
        private Integer maxPeople;
        private Long zoneId;

        public Site toEntity(Zone zone) {
            return Site.builder()
                    .siteName(siteName)
                    .maxPeople(maxPeople)
                    .zone(zone)
                    .build();
        }
    }
}
