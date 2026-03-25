package com.camping.erp.domain.image;

import com.camping.erp.domain.gallery.Gallery;
import com.camping.erp.domain.notice.Notice;
import com.camping.erp.domain.review.Review;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.Zone;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone; // 구역 사진 지원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site; // 사이트별 상세 사진 지원

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileName;

    public String getFullPath() {
        // [강력 조치] 경로에 /upload/가 포함되어 있으면 무조건 static/images 샘플로 대체
        if (filePath != null && filePath.contains("/upload/")) {
            String[] samples = {"1.png", "2.png", "3.jpg", "4.jpg", "5.jpg", "6.jpg", "camping_review1.jpg", "camping_review2.jpg", "camping_review3.jpg"};
            // ID를 기반으로 결정된 샘플 반환 (결과가 일정하도록)
            int index = (id != null) ? (int) (id % samples.length) : 0;
            return "/images/" + samples[index];
        }
        return filePath + fileName;
    }

    @Builder
    public Image(Long id, Gallery gallery, Review review, Notice notice, Zone zone, Site site, String filePath,
            String fileName) {
        this.id = id;
        this.gallery = gallery;
        this.review = review;
        this.notice = notice;
        this.zone = zone;
        this.site = site;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void updateNotice(Notice notice) {
        this.notice = notice;
    }

    public void updateGallery(Gallery gallery) {
        this.gallery = gallery;
    }

    public void updateReview(Review review) {
        this.review = review;
    }
}
