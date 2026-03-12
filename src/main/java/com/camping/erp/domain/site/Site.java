package com.camping.erp.domain.site;

import com.camping.erp.global._core.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "site_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private Integer maxPeople;

    @Builder
    public Site(Long id, Zone zone, String siteName, Integer maxPeople) {
        this.id = id;
        this.zone = zone;
        this.siteName = siteName;
        this.maxPeople = maxPeople;
    }

    public void update(Zone zone, String siteName, Integer maxPeople) {
        this.zone = zone;
        this.siteName = siteName;
        this.maxPeople = maxPeople;
    }
}
