package com.camping.erp.domain.site;

import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zone_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Zone extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long normalPrice;

    @Column(nullable = false)
    private Long peakPrice;

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    private List<Site> sites = new ArrayList<>();

    @Builder
    public Zone(Long id, String name, Long normalPrice, Long peakPrice) {
        this.id = id;
        this.name = name;
        this.normalPrice = normalPrice;
        this.peakPrice = peakPrice;
    }
}
