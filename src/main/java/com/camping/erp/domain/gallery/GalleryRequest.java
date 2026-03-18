package com.camping.erp.domain.gallery;

<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
=======
>>>>>>> dev
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
        @NotBlank(message = "제목은 필수 입력 사항입니다.")
        @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
        private String title;

        @NotBlank(message = "카테고리는 필수 선택 사항입니다.")
        private String category;

        @NotBlank(message = "촬영일은 필수 입력 사항입니다.")
        private String shootingDate;

        @NotBlank(message = "내용은 필수 입력 사항입니다.")
        private String content;
        
        @NotEmpty(message = "최소 한 장 이상의 사진을 업로드해야 합니다.")
        private List<MultipartFile> images;
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

        @NotBlank(message = "카테고리는 필수 선택 사항입니다.")
        private String category;

        @NotBlank(message = "촬영일은 필수 입력 사항입니다.")
        private String shootingDate;

        @NotBlank(message = "내용은 필수 입력 사항입니다.")
        private String content;
        
        private List<MultipartFile> images; // 추가
        private List<Long> deleteImageIds; // 추가
    }
}
