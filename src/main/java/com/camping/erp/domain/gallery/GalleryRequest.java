package com.camping.erp.domain.gallery;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class GalleryRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaveDTO {
        private String title;
        private String category;
        private String shootingDate;
        private String content;
        private List<MultipartFile> images;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateDTO {
        private String title;
        private String category;
        private String shootingDate;
        private String content;
        private List<MultipartFile> images; // 추가
        private List<Long> deleteImageIds; // 추가
    }
}
