package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class ReservationResponse {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListDTO {
        private Long id;
        private String siteName;
        private String siteImage;
        private String checkIn;
        private String checkOut;
        private String totalPrice;
        private String reservationDate;
        private String statusDescription;

        // 버튼 노출 및 상태 제어 플래그
        private boolean canModify;    // 예약 확정 & 이용 전
        private boolean isWait;       // 승인 대기 중 (변경/취소 요청 중)
        private boolean isCompleted;  // 이용 완료
        private boolean isReviewDone; // 리뷰 작성 완료 (추후 확장용)

        public static ListDTO fromEntity(Reservation reservation, LocalDate today) {
            String dayOfWeek = reservation.getCheckIn().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

            // TODO: Site/Zone 이미지 연관관계 확정 후 이미지 경로 로직 보완 예정
            // 현재 Site 엔티티에 images 연관관계가 없어 임시로 upload 폴더의 이미지를 사용함
            String imageUrl = "/upload/1df7d7fc-2f49-4f29-a27b-71f68cbfb104_wesley-shen-2l2EslhTaOM-unsplash.jpg";

            return ListDTO.builder()
                    .id(reservation.getId())
                    .siteName(reservation.getSite().getSiteName())
                    .siteImage(imageUrl)
                    .checkIn(reservation.getCheckIn().toString().replace("-", ".") + " (" + dayOfWeek + ")")
                    .checkOut(reservation.getCheckOut().toString().replace("-", "."))
                    .totalPrice(String.format("%,d원", reservation.getTotalPrice()))
                    .reservationDate(reservation.getCreatedAt().toLocalDate().toString().replace("-", "."))
                    .canModify(reservation.getStatus() == ReservationStatus.CONFIRMED && reservation.getCheckIn().isAfter(today))
                    .isWait(reservation.getStatus() == ReservationStatus.CHANGE_REQ || reservation.getStatus() == ReservationStatus.CANCEL_REQ)
                    .isCompleted(reservation.getStatus() == ReservationStatus.COMPLETED)
                    .statusDescription(reservation.getStatus() == ReservationStatus.CHANGE_REQ ? "변경 승인 대기" :
                                     reservation.getStatus() == ReservationStatus.CANCEL_REQ ? "취소 승인 대기" : "")
                    .build();
        }
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
