package com.camping.erp.domain.admin;

import com.camping.erp.domain.reservation.enums.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class AdminResponse {

    @Getter
    @Setter
    @Builder
    public static class ReviewPageDTO {
        private List<ReviewListDTO> reviews;
        private PaginationDTO pagination;
    }

    @Getter
    @Setter
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
    }

    @Getter
    @Setter
    @Builder
    public static class ReservationPageDTO {
        private List<ReservationListDTO> reservations;
        private PaginationDTO pagination;
    }

    @Getter
    @Setter
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

    @Getter
    @Setter
    @Builder
    public static class PageNumberDTO {
        private int number;
        private int displayDigit; // 화면에 표시될 1부터 시작하는 숫자
        private boolean isCurrent;
    }

    @Getter
    @Setter
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

        public boolean isPending() {
            return status == ReservationStatus.PENDING;
        }

        public boolean isChangeReq() {
            return status == ReservationStatus.CHANGE_REQ;
        }

        public boolean isCancelReq() {
            return status == ReservationStatus.CANCEL_REQ;
        }
    }

    // [Task 4-2] 관리자용 예약 변경 상세 비교 DTO
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminChangeDetailDTO {
        private Long id; // 예약 ID
        private Long userId;
        private String username;
        private String visitorName;
        private String visitorPhone;
        private Long totalPrice;

        // 기존 정보
        private String oldSiteName;
        private String oldCheckIn;
        private String oldCheckOut;
        private long oldNights;
        private Integer oldPeopleCount;

        // 변경 요청 정보
        private String newSiteName;
        private String newCheckIn;
        private String newCheckOut;
        private long newNights;
        private Integer newPeopleCount;

        // 요청 이력
        private List<com.camping.erp.domain.reservation.ReservationResponse.ChangeRequestHistoryDTO> changeRequests;
        private List<com.camping.erp.domain.reservation.ReservationResponse.CancelRequestHistoryDTO> cancelRequests;
    }

    // [Task 4-2] 관리자용 예약 취소 상세 DTO
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminCancelDetailDTO {
        private Long id; // 예약 ID
        private Long userId;
        private String username;
        private String visitorName;
        private String visitorPhone;
        private String siteName;
        private String checkIn;
        private String checkOut;
        private long nights;
        private Integer peopleCount;
        private Long totalPrice;

        // 취소 요청 정보
        private String reason;
        private String refundBank;
        private String refundAccount;
        private String refundAccountHolder;

        // 요청 이력
        private List<com.camping.erp.domain.reservation.ReservationResponse.ChangeRequestHistoryDTO> changeRequests;
        private List<com.camping.erp.domain.reservation.ReservationResponse.CancelRequestHistoryDTO> cancelRequests;
    }

    // [Task 4-3] 관리자용 예약 상세 정보 통합 조회 DTO
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminReservationDetailDTO {
        private Long id; // 예약 ID
        private Long userId;
        private String username;
        private String visitorName;
        private String visitorPhone;
        private String siteName;
        private String checkIn;
        private String checkOut;
        private long nights;
        private Integer peopleCount;
        private Long totalPrice;
        private String statusText;
        private String statusClass;

        // 요청 이력
        private List<com.camping.erp.domain.reservation.ReservationResponse.ChangeRequestHistoryDTO> changeRequests;
        private List<com.camping.erp.domain.reservation.ReservationResponse.CancelRequestHistoryDTO> cancelRequests;
    }
}
