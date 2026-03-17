package com.camping.erp.domain.notice;

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
@Table(name = "notice_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isTop;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Notice(Long id, String title, String content, Boolean isTop) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isTop = isTop != null && isTop;
    }

    public void update(String title, String content, Boolean isTop) {
        this.title = title;
        this.content = content;
        this.isTop = isTop;
    }

    public void addImage(com.camping.erp.domain.image.Image image) {
        this.images.add(image);
        image.setNotice(this);
    }
}
