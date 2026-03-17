package com.camping.erp.domain.gallery;

import com.camping.erp.domain.image.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GalleryResponse {

    @Getter
    @NoArgsConstructor
    public static class ListDTO {
        private Long id;
        private String title;
        private String category;
        private String shootingDate;
        private Long viewCount;
        private String thumbnailPath;
        private LocalDateTime createdAt;

        public ListDTO(Gallery gallery) {
            this.id = gallery.getId();
            this.title = gallery.getTitle();
            this.category = gallery.getCategory();
            this.shootingDate = gallery.getShootingDate();
            this.viewCount = gallery.getViewCount();
            this.createdAt = gallery.getCreatedAt();
            
            // 첫 번째 이미지를 썸네일로 사용
            if (gallery.getImages() != null && !gallery.getImages().isEmpty()) {
                this.thumbnailPath = gallery.getImages().get(0).getFilePath();
            } else {
                this.thumbnailPath = "/img/default-thumbnail.jpg";
            }
        }

        public String getCreatedAt() {
            return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DetailDTO {
        private Long id;
        private String title;
        private String category;
        private String shootingDate;
        private String content;
        private Long viewCount;
        private List<ImageDTO> images;
        private LocalDateTime createdAt;

        public DetailDTO(Gallery gallery) {
            this.id = gallery.getId();
            this.title = gallery.getTitle();
            this.category = gallery.getCategory();
            this.shootingDate = gallery.getShootingDate();
            this.content = gallery.getContent();
            this.viewCount = gallery.getViewCount();
            this.createdAt = gallery.getCreatedAt();
            this.images = gallery.getImages().stream()
                    .map(ImageDTO::new)
                    .collect(Collectors.toList());
        }

        @Getter
        @NoArgsConstructor
        public static class ImageDTO {
            private Long id;
            private String filePath;

            public ImageDTO(Image image) {
                this.id = image.getId();
                this.filePath = image.getFilePath();
            }
        }
    }
}
