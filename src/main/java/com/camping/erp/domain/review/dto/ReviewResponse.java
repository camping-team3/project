package com.camping.erp.domain.review.dto;

import com.camping.erp.domain.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewResponse {

    @Getter
    public static class AdminListDTO {
        private Long id;
        private String username;
        private String zoneName;
        private String siteName;
        private String content;
        private Integer rating;
        private Integer aiDangerScore;
        private boolean isReviewed;
        private boolean isDeleted;
        private String adminReason;
        private String createdAt;

        public AdminListDTO(Review review) {
            this.id = review.getId();
            this.username = review.getUser().getUsername();
            this.zoneName = review.getReservation().getSite().getZone().getName();
            this.siteName = review.getReservation().getSite().getSiteName();
            this.content = review.getContent();
            this.rating = review.getRating();
            this.aiDangerScore = review.getAiDangerScore();
            this.isReviewed = review.isReviewed();
            this.isDeleted = review.isDeleted();
            this.adminReason = review.getAdminReason();
            this.createdAt = review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
    }

    @Getter
    @Builder
    public static class ListWrapperDTO {
        private List<ListDTO> reviews;
        private Double avgRating;
        private Long totalCount;
        private boolean hasPrev;
        private boolean hasNext;
        private List<PageNumberDTO> pages;
        private Integer prevPage;
        private Integer nextPage;
    }

    @Getter
    public static class PageNumberDTO {
        private Integer number;
        private Integer displayNumber;
        private boolean active;

        public PageNumberDTO(int number, int current) {
            this.number = number;
            this.displayNumber = number + 1;
            this.active = (number == current);
        }
    }

    @Getter
    public static class StarDTO {
        private boolean active;
        public StarDTO(boolean active) {
            this.active = active;
        }
    }

    @Getter
    public static class ListDTO {
        private Long id;
        private String username;
        private String content;
        private Integer rating;
        private List<StarDTO> stars;
        private String createdAt;
        private String zoneName;
        private String siteName;
        private Long siteId;
        private List<ImageDTO> images;
        private boolean hasMoreImages;
        private int imageCount;

        public ListDTO(Review review) {
            this.id = review.getId();
            this.username = review.getUser().getUsername();
            this.content = review.getContent();
            this.rating = review.getRating();
            
            this.stars = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                this.stars.add(new StarDTO(i <= review.getRating()));
            }
            
            this.createdAt = review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.zoneName = review.getReservation().getSite().getZone().getName();
            this.siteName = review.getReservation().getSite().getSiteName();
            this.siteId = review.getReservation().getSite().getId();
            
            this.images = review.getImages().stream()
                    .map(img -> new ImageDTO(img.getFileName()))
                    .collect(Collectors.toList());
            this.hasMoreImages = this.images.size() > 1;
            this.imageCount = this.images.size() - 1;
        }
    }

    @Getter
    public static class ImageDTO {
        private String fileName;
        public ImageDTO(String fileName) {
            this.fileName = fileName;
        }
    }
}
