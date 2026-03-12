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

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "gallery", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Gallery(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
