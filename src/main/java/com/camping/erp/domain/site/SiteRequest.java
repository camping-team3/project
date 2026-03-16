package com.camping.erp.domain.site;

import lombok.Getter;
import lombok.Setter;

public class SiteRequest {
    
    @Getter @Setter
    public static class ZoneSaveDTO {
        private String name;
        private Long normalPrice;
        private Long peakPrice;
        private int basePeople;
        private Long extraPersonFee;

        public Zone toEntity() {
            return Zone.builder()
                    .name(name)
                    .normalPrice(normalPrice)
                    .peakPrice(peakPrice)
                    .basePeople(basePeople)
                    .extraPersonFee(extraPersonFee)
                    .build();
        }
    }

    @Getter @Setter
    public static class SiteSaveDTO {
        private String siteName;
        private Integer maxPeople;
        private Long zoneId;
        private boolean isAvailable = true;

        public Site toEntity(Zone zone) {
            return Site.builder()
                    .siteName(siteName)
                    .maxPeople(maxPeople)
                    .zone(zone)
                    .isAvailable(isAvailable)
                    .build();
        }
    }
}
