package com.camping.erp.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReviewRequest {

    @Getter
    @Setter
    public static class SaveDTO {
        @NotNull(message = "예약 정보를 확인할 수 없습니다.")
        private Long reservationId;

        @NotNull(message = "별점을 선택해주세요.")
        @Min(value = 1, message = "별점은 최소 1점입니다.")
        @Max(value = 5, message = "별점은 최대 5점입니다.")
        private Integer rating;

        @NotNull(message = "리뷰 내용을 입력해주세요.")
        @Size(min = 10, max = 1000, message = "리뷰는 10자 이상 1000자 이하로 작성해주세요.")
        private String content;

        private List<MultipartFile> images;
    }

    @Getter
    @Setter
    public static class UpdateDTO {
        @NotNull(message = "별점을 선택해주세요.")
        @Min(value = 1, message = "별점은 최소 1점입니다.")
        @Max(value = 5, message = "별점은 최대 5점입니다.")
        private Integer rating;

        @NotNull(message = "리뷰 내용을 입력해주세요.")
        @Size(min = 10, max = 1000, message = "리뷰는 10자 이상 1000자 이하로 작성해주세요.")
        private String content;

        private List<MultipartFile> images;
    }
}
