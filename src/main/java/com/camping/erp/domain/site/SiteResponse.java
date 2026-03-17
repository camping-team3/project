package com.camping.erp.domain.site;

import lombok.Builder;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class SiteResponse {

    @Getter
    @Setter
    public static class ResevationAbailableListDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private Integer maxPeople;
        private Integer basePeople; // 기준 인원 필드 추가
        private Long pricePerNight; // 1박 요금 필드 추가
        private Boolean isAvailable;

        @Builder
        public ResevationAbailableListDTO(Long id, String siteName, String zoneName, Integer maxPeople,
                Integer basePeople, Long pricePerNight, Boolean isAvailable) {
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
        private int basePeople;
        private Long extraPersonFee;
        private List<AdminSiteDTO> sites;

        public AdminZoneDTO(Zone zone) {
            this.id = zone.getId();
            this.name = zone.getName();
            this.normalPrice = zone.getNormalPrice();
            this.peakPrice = zone.getPeakPrice();
            this.basePeople = zone.getBasePeople();
            this.extraPersonFee = zone.getExtraPersonFee();
            this.sites = zone.getSites().stream().map(AdminSiteDTO::new).toList();
        }
    }

    @Getter
    @Setter
    public static class AdminSiteDTO {
        private Long id;
        private String siteName;
        private Integer maxPeople;
        private Long zoneId;

        public AdminSiteDTO(Site site) {
            this.id = site.getId();
            this.siteName = site.getSiteName();
            this.maxPeople = site.getMaxPeople();
            if (site.getZone() != null) {
                this.zoneId = site.getZone().getId();
            }
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
