package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class ReservationResponse {

    @Getter
    @Setter
    @Builder
    public static class MyPageWrapperDTO {
        private List<MyPageListDTO> reservations;
        private int totalCount;
    }

    @Getter
    @Setter
    @Builder
    public static class MyPageListDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private long nights;
        private Long totalPrice;
        private Integer peopleCount;
        private ReservationStatus status;
        private String statusText;
        private String statusClass;
        private boolean isReviewable; // 퇴실 완료 및 COMPLETED 상태
        private boolean hasReview;    // 이미 리뷰를 작성했는지
        private Long reviewId;        // 작성된 리뷰 ID (있을 경우)
    }

    @Getter
    @Setter
    @Builder
    public static class PaymentFormDTO {
        private Long siteId;
        private String siteName;
        private String zoneName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Long nights;
        private Integer peopleCount;
        private Integer basePeople;
        private Long pricePerNight;
        private Long extraPrice;
        private Long totalPrice;
        private Long extraPersonFee; // 추가 요금 단가 추가
    }

    @Getter
    @Setter
    @Builder
    public static class ReserveDTO {
        private Long id;
        private String siteName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Long totalPrice;
    }

    @Getter
    @Setter
    @Builder
    public static class CompleteDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Long nights;
        private Integer peopleCount;
        private Long totalPrice;
        private String username;
    }
}
