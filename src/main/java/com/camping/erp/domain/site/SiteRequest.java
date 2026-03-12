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
        private Long zoneId;
        private String siteName;
        private Integer maxPeople;

        public Site toEntity(Zone zone) {
            return Site.builder()
                    .zone(zone)
                    .siteName(siteName)
                    .maxPeople(maxPeople)
                    .build();
        }
    }

    @Getter @Setter
    public static class ZoneUpdateDTO {
        private String name;
        private Long normalPrice;
        private Long peakPrice;
    }

    @Getter @Setter
    public static class SiteUpdateDTO {
        private Long zoneId;
        private String siteName;
        private Integer maxPeople;
    }
}
