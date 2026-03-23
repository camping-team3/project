package com.camping.erp.domain.site;

import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "site_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Site extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @Column(nullable = false)
    private String siteName;

    @Column(nullable = false)
    private int maxPeople;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAvailable = true; // 예약 가능 여부 (점검 중 등)

    @Column(nullable = false)
    @Builder.Default
    private Double avgRating = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer reviewCount = 0;

    public void update(String siteName, Integer maxPeople, Zone zone, boolean isAvailable) {
        this.siteName = siteName;
        this.maxPeople = maxPeople;
        this.zone = zone;
        this.isAvailable = isAvailable;
    }

    public void addRating(Integer newRating) {
        double totalScore = this.avgRating * this.reviewCount;
        this.reviewCount++;
        this.avgRating = (totalScore + newRating) / this.reviewCount;
    }

    public void removeRating(Integer oldRating) {
        if (this.reviewCount <= 1) {
            this.reviewCount = 0;
            this.avgRating = 0.0;
            return;
        }
        double totalScore = this.avgRating * this.reviewCount;
        this.reviewCount--;
        this.avgRating = (totalScore - oldRating) / this.reviewCount;
    }

    public void updateRating(Integer count, Double avg) {
        this.reviewCount = count;
        this.avgRating = avg != null ? avg : 0.0;
    }
}
