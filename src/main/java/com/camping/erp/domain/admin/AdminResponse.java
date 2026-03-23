package com.camping.erp.domain.admin;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class AdminResponse {

    @Getter @Setter
    @Builder
    public static class ReviewPageDTO {
        private List<ReviewListDTO> reviews;
        private PaginationDTO pagination;
    }

    @Getter @Setter
    @Builder
    public static class ReviewListDTO {
        private Long id;
        private String username;
        private Integer rating;
        private String content;
        private String zoneName;
        private String siteName;
        private String createdAt;
        private List<String> images; // 썸네일용
        private Integer aiDangerScore;
        private boolean isReviewed;
        private boolean isDeleted;
        private String adminReason;
        private Integer penaltyCount;
        private Long userId;
        private boolean isExpelled; // 사용자가 이미 탈퇴했는지 여부

        public boolean isHighDanger() {
            return aiDangerScore != null && aiDangerScore >= 3;
        }

        public boolean canExpel() {
            return !isExpelled && penaltyCount != null && penaltyCount >= 3;
        }
    }

    @Getter @Setter
    @Builder
    public static class ReservationPageDTO {
        private List<ReservationListDTO> reservations;
        private PaginationDTO pagination;
    }

    @Getter @Setter
    @Builder
    public static class PaginationDTO {
        private int totalPages;
        private long totalElements;
        private int currentPage;
        private List<PageNumberDTO> pageNumbers;
        private boolean hasPrev;
        private boolean hasNext;
        private int prevPage;
        private int nextPage;
    }

    @Getter @Setter
    @Builder
    public static class PageNumberDTO {
        private int number;
        private int displayDigit; // 화면에 표시될 1부터 시작하는 숫자
        private boolean isCurrent;
    }

    @Getter @Setter
    @Builder
    public static class ReservationListDTO {
        private Long id;
        private String reservationIdDisplay; // 예: RSV-0001
        private String username;
        private String siteName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private long nights;
        private Long totalPrice;
        private ReservationStatus status;
        private String statusText;
        private String statusClass;

        public boolean isPending() { return status == ReservationStatus.PENDING; }
    }
}
