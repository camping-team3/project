package com.camping.erp.domain.site;

import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

public class SiteResponse {

    @Getter
    public static class MainDTO {
        private final Long zoneId;
        private final String zoneName;
        private final Long normalPrice;
        private final Long peakPrice;
        private final boolean isPeak;
        private final List<SiteDTO> sites;

        public MainDTO(Zone zone, List<Site> availableSites, long nights) {
            this.zoneId = zone.getId();
            this.zoneName = zone.getName();
            this.normalPrice = zone.getNormalPrice();
            this.peakPrice = zone.getPeakPrice();
            this.isPeak = zone.isPeak();
            
            // 실시간 성수기 여부에 따른 단가 결정
            Long pricePerNight = zone.isPeak() ? zone.getPeakPrice() : zone.getNormalPrice();
            
            this.sites = availableSites.stream()
                    .filter(site -> site.getZone().getId().equals(zone.getId()))
                    .map(site -> new SiteDTO(site, pricePerNight * nights))
                    .collect(Collectors.toList());
        }

        @Getter
        public static class SiteDTO {
            private final Long id;
            private final String siteName;
            private final Integer maxPeople;
            private final Long totalPrice;

            public SiteDTO(Site site, Long totalPrice) {
                this.id = site.getId();
                this.siteName = site.getSiteName();
                this.maxPeople = site.getMaxPeople();
                this.totalPrice = totalPrice;
            }
        }
    }

    @Getter
    public static class AdminListDTO {
        private final Long id;
        private final String name;
        private final Long normalPrice;
        private final Long peakPrice;
        private final boolean isPeak;
        private final List<SiteDTO> sites;

        public AdminListDTO(Zone zone) {
            this.id = zone.getId();
            this.name = zone.getName();
            this.normalPrice = zone.getNormalPrice();
            this.peakPrice = zone.getPeakPrice();
            this.isPeak = zone.isPeak();
            this.sites = zone.getSites().stream()
                    .map(site -> new SiteDTO(site, null)) // 관리자용은 총액 불필요
                    .collect(Collectors.toList());
        }

        @Getter
        public static class SiteDTO {
            private final Long id;
            private final String siteName;
            private final Integer maxPeople;

            public SiteDTO(Site site, Long totalPrice) {
                this.id = site.getId();
                this.siteName = site.getSiteName();
                this.maxPeople = site.getMaxPeople();
            }
        }
    }

    @Getter
    public static class ZoneUpdateFormDTO {
        private final Long id;
        private final String name;
        private final Long normalPrice;
        private final Long peakPrice;
        private final boolean isPeak;

        public ZoneUpdateFormDTO(Zone zone) {
            this.id = zone.getId();
            this.name = zone.getName();
            this.normalPrice = zone.getNormalPrice();
            this.peakPrice = zone.getPeakPrice();
            this.isPeak = zone.isPeak();
        }
    }

    @Getter
    public static class SiteUpdateFormDTO {
        private final Long id;
        private final Long zoneId;
        private final String siteName;
        private final Integer maxPeople;

        public SiteUpdateFormDTO(Site site) {
            this.id = site.getId();
            this.zoneId = site.getZone().getId();
            this.siteName = site.getSiteName();
            this.maxPeople = site.getMaxPeople();
        }
    }
}
