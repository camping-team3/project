package com.camping.erp.domain.notice;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class NoticeRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaveDTO {
        private String title;
        private String content;
        private Boolean isTop;
        private List<MultipartFile> images;

        public Notice toEntity() {
            return Notice.builder()
                    .title(title)
                    .content(content)
                    .isTop(isTop != null && isTop)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateDTO {
        private String title;
        private String content;
        @Builder.Default
        private Boolean isTop = false; 
        private List<MultipartFile> images;
        private List<Long> deleteImageIds;
    }
}
