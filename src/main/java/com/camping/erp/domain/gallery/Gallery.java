package com.camping.erp.domain.gallery;

import com.camping.erp.domain.image.Image;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gallery_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gallery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category; // 캠핑, 팁/노하우, 호수뷰 등

    @Column(nullable = false)
    private String shootingDate; // 촬영일

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Long viewCount; // 조회수

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Gallery(Long id, String title, String category, String shootingDate, String content, Long viewCount) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.shootingDate = shootingDate;
        this.content = content;
        this.viewCount = (viewCount == null) ? 0L : viewCount;
    }

    public void update(String title, String category, String shootingDate, String content) {
        this.title = title;
        this.category = category;
        this.shootingDate = shootingDate;
        this.content = content;
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.updateGallery(this);
    }
}
