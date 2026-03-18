package com.camping.erp.domain.notice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
                @NotBlank(message = "제목은 필수 입력 사항입니다.")
                @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
                private String title;

                @NotBlank(message = "내용은 필수 입력 사항입니다.")
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
                @NotBlank(message = "제목은 필수 입력 사항입니다.")
                @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
                private String title;

                @NotBlank(message = "내용은 필수 입력 사항입니다.")
                private String content;

                @Builder.Default
                private Boolean isTop = false;
                private List<MultipartFile> images;
                private List<Long> deleteImageIds;
        }
}
