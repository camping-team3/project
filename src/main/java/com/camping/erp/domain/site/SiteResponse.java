package com.camping.erp.domain.site;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class SiteResponse {

    @Getter
    @Setter
    public static class ListDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private Long price;
        private Double rating;

        public ListDTO(Site site) {
            this.id = site.getId();
            this.siteName = site.getSiteName();
            if (site.getZone() != null) {
                this.zoneName = site.getZone().getName();
                this.price = site.getZone().getNormalPrice();
            } else {
                this.zoneName = "미지정 구역";
                this.price = 0L;
            }
            this.rating = 4.9; // TODO: 리뷰 연동 시 실제 별점으로 변경
        }
    }

    @Getter
    @Setter
    public static class AdminZoneDTO {
        private Long id;
        private String name;
        private Long normalPrice;
        private Long peakPrice;
        private List<AdminSiteDTO> sites;

        public AdminZoneDTO(Zone zone) {
            this.id = zone.getId();
            this.name = zone.getName();
            this.normalPrice = zone.getNormalPrice();
            this.peakPrice = zone.getPeakPrice();
            this.sites = zone.getSites().stream().map(AdminSiteDTO::new).toList();
        }
    }

    @Getter
    @Setter
    public static class AdminSiteDTO {
        private Long id;
        private String siteName;
        private Integer maxPeople;

        public AdminSiteDTO(Site site) {
            this.id = site.getId();
            this.siteName = site.getSiteName();
            this.maxPeople = site.getMaxPeople();
        }
    }

    @Getter
    @Setter
    public static class DetailDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private Integer maxPeople;
        private Long normalPrice;
        private Long peakPrice;
        private Double rating;

        public DetailDTO(Site site) {
            this.id = site.getId();
            this.siteName = site.getSiteName();
            this.maxPeople = site.getMaxPeople();
            if (site.getZone() != null) {
                this.zoneName = site.getZone().getName();
                this.normalPrice = site.getZone().getNormalPrice();
                this.peakPrice = site.getZone().getPeakPrice();
            }
            this.rating = 4.9; // TODO: 리뷰 연동 시 실제 별점으로 변경
        }
    }
}
