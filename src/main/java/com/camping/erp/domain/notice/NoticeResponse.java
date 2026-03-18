package com.camping.erp.domain.notice;

import com.camping.erp.domain.image.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class NoticeResponse {

    @Getter
    @NoArgsConstructor
    public static class ListDTO {
        private Long id;
        private String title;
        private Boolean isTop;
        private LocalDateTime createdAt;

        public ListDTO(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.isTop = notice.getIsTop();
            this.createdAt = notice.getCreatedAt();
        }

        public String getCreatedAt() {
            return createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DetailDTO {
        private Long id;
        private String title;
        private String content;
        private Boolean isTop;
        private LocalDateTime createdAt;
        private List<ImageDTO> images;

        public DetailDTO(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.isTop = notice.getIsTop();
            this.createdAt = notice.getCreatedAt();
            this.images = notice.getImages().stream()
                    .map(ImageDTO::new)
                    .collect(Collectors.toList());
        }

        public String getCreatedAt() {
            return createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        }

        @Getter
        @NoArgsConstructor
        public static class ImageDTO {
            private Long id;
            private String filePath;
            private String fileName;

            public ImageDTO(Image image) {
                this.id = image.getId();
                this.filePath = image.getFilePath();
                this.fileName = image.getFileName();
            }
        }
    }
}
