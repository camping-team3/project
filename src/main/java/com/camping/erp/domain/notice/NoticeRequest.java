package com.camping.erp.domain.notice;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class NoticeRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaveDTO {
        private String title;
        private String content;
        private Boolean isTop;

        public Notice toEntity() {
            return Notice.builder()
                    .title(title)
                    .content(content)
                    .isTop(isTop != null && isTop)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateDTO {
        private String title;
        private String content;
        private Boolean isTop;
    }
}
