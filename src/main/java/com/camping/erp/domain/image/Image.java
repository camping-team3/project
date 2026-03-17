package com.camping.erp.domain.image;

import com.camping.erp.domain.gallery.Gallery;
import com.camping.erp.domain.notice.Notice;
import com.camping.erp.domain.review.Review;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileName;

    @Builder
    public Image(Long id, Gallery gallery, Review review, Notice notice, String filePath, String fileName) {
        this.id = id;
        this.gallery = gallery;
        this.review = review;
        this.notice = notice;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
