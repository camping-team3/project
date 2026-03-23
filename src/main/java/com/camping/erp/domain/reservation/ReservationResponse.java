package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
        private boolean canModify; // 예약 확정 & 이용 전
        private boolean isWait; // 승인 대기 중 (변경/취소 요청 중)
        private boolean isCompleted; // 이용 완료
        private boolean isReviewDone; // 리뷰 작성 완료 (추후 확장용)

        public static ListDTO fromEntity(Reservation reservation, LocalDate today) {
            String dayOfWeekIn = reservation.getCheckIn().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
            String dayOfWeekOut = reservation.getCheckOut().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

            // TODO: Site/Zone 이미지 연관관계 확정 후 이미지 경로 로직 보완 예정
            // 현재 Site 엔티티에 images 연관관계가 없어 임시로 upload 폴더의 이미지를 사용함
            String imageUrl = "/upload/1df7d7fc-2f49-4f29-a27b-71f68cbfb104_wesley-shen-2l2EslhTaOM-unsplash.jpg";

            return ListDTO.builder()
                    .id(reservation.getId())
                    .siteName(reservation.getSite().getSiteName())
                    .siteImage(imageUrl)
                    .checkIn(reservation.getCheckIn().toString().replace("-", ".") + " (" + dayOfWeekIn + ")")
                    .checkOut(reservation.getCheckOut().toString().replace("-", ".") + " (" + dayOfWeekOut + ")")
                    .totalPrice(String.format("%,d원", reservation.getTotalPrice()))
                    .reservationDate(reservation.getCreatedAt().toLocalDate().toString().replace("-", "."))
                    .canModify(reservation.getStatus() == ReservationStatus.CONFIRMED
                            && reservation.getCheckIn().isAfter(today))
                    .isWait(reservation.getStatus() == ReservationStatus.CHANGE_REQ
                            || reservation.getStatus() == ReservationStatus.CANCEL_REQ)
                    .isCompleted(reservation.getStatus() == ReservationStatus.COMPLETED)
                    .statusDescription(reservation.getStatus() == ReservationStatus.CHANGE_REQ ? "변경 승인 대기"
                            : reservation.getStatus() == ReservationStatus.CANCEL_REQ ? "취소 승인 대기" : "")
                    .build();
        }
    }

    @Getter
    @Builder
    public static class PaymentFormDTO {
        private Long reservationId; // 가예약(PENDING) ID 추가
        private Long siteId;
        private String siteName;
        private String zoneName;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private long nights;
        private Integer peopleCount;
        private Integer basePeople;
        private Long pricePerNight;
        private Long extraPrice;
        private Long totalPrice;
        private Long extraPersonFee;
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
    @AllArgsConstructor
    @NoArgsConstructor
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

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private String siteImage;
        private String checkIn;
        private String checkOut;
        private String nights;
        private String totalPrice;
        private String visitorName;
        private String visitorPhone;
        private Integer peopleCount;
        private String reservationDate;
        private String status;
        private String statusDescription;

        // 상태 제어 플래그
        private boolean canModify;
        private boolean isWait;
        private boolean isCompleted;

        // 요청 이력
        private List<ChangeRequestHistoryDTO> changeRequests;
        private List<CancelRequestHistoryDTO> cancelRequests;

        public static DetailDTO fromEntity(Reservation reservation, LocalDate today) {
            long nights = ChronoUnit.DAYS.between(reservation.getCheckIn(), reservation.getCheckOut());
            String dayOfWeekIn = reservation.getCheckIn().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
            String dayOfWeekOut = reservation.getCheckOut().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

            List<ChangeRequestHistoryDTO> changeHistories = reservation.getChangeRequests().stream()
                    .map(ChangeRequestHistoryDTO::fromEntity)
                    .toList();

            List<CancelRequestHistoryDTO> cancelHistories = reservation.getCancelRequests().stream()
                    .map(CancelRequestHistoryDTO::fromEntity)
                    .toList();

            return DetailDTO.builder()
                    .id(reservation.getId())
                    .siteName(reservation.getSite().getSiteName())
                    .zoneName(reservation.getSite().getZone().getName())
                    .siteImage("/upload/1df7d7fc-2f49-4f29-a27b-71f68cbfb104_wesley-shen-2l2EslhTaOM-unsplash.jpg")
                    .checkIn(reservation.getCheckIn().toString().replace("-", ".") + " (" + dayOfWeekIn + ")")
                    .checkOut(reservation.getCheckOut().toString().replace("-", ".") + " (" + dayOfWeekOut + ")")
                    .nights(nights + "박 " + (nights + 1) + "일")
                    .totalPrice(String.format("%,d원", reservation.getTotalPrice()))
                    .visitorName(reservation.getVisitorName())
                    .visitorPhone(reservation.getVisitorPhone())
                    .peopleCount(reservation.getPeopleCount())
                    .reservationDate(reservation.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")))
                    .status(reservation.getStatus().name())
                    .statusDescription(reservation.getStatus() == ReservationStatus.CONFIRMED ? "예약 확정"
                            : reservation.getStatus() == ReservationStatus.CHANGE_REQ ? "변경 승인 대기"
                                    : reservation.getStatus() == ReservationStatus.CANCEL_REQ ? "취소 승인 대기"
                                            : reservation.getStatus() == ReservationStatus.COMPLETED ? "이용 완료"
                                                    : "취소 완료")
                    .canModify(reservation.getStatus() == ReservationStatus.CONFIRMED
                            && reservation.getCheckIn().isAfter(today))
                    .isWait(reservation.getStatus() == ReservationStatus.CHANGE_REQ
                            || reservation.getStatus() == ReservationStatus.CANCEL_REQ)
                    .isCompleted(reservation.getStatus() == ReservationStatus.COMPLETED)
                    .changeRequests(changeHistories)
                    .cancelRequests(cancelHistories)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangeRequestHistoryDTO {
        private Long id;
        private String requestDate;
        private String newCheckIn;
        private String newCheckOut;
        private String newSiteName;
        private String status;
        private String statusDescription;
        private String rejectionReason;

        public static ChangeRequestHistoryDTO fromEntity(ReservationChangeRequest request) {
            return ChangeRequestHistoryDTO.builder()
                    .id(request.getId())
                    .requestDate(request.getCreatedAt().toLocalDate().toString().replace("-", "."))
                    .newCheckIn(request.getNewCheckIn().toString().replace("-", "."))
                    .newCheckOut(request.getNewCheckOut().toString().replace("-", "."))
                    .newSiteName(request.getNewSite().getSiteName())
                    .status(request.getStatus().name())
                    .statusDescription(request.getStatus().getDescription())
                    .rejectionReason(request.getRejectionReason())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CancelRequestHistoryDTO {
        private Long id;
        private String requestDate;
        private String reason;
        private String status;
        private String statusDescription;
        private String rejectionReason;

        public static CancelRequestHistoryDTO fromEntity(ReservationCancelRequest request) {
            return CancelRequestHistoryDTO.builder()
                    .id(request.getId())
                    .requestDate(request.getCreatedAt().toLocalDate().toString().replace("-", "."))
                    .reason(request.getReason())
                    .status(request.getStatus().name())
                    .statusDescription(request.getStatus().getDescription())
                    .rejectionReason(request.getRejectionReason())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangeFormDTO {
        private Long id;
        private String siteName;
        private String zoneName;
        private String siteImage;
        private String checkIn;
        private String checkOut;
        private Integer peopleCount;
        private Long totalPrice;
        private String visitorName;
        private String visitorPhone;

        public static ChangeFormDTO fromEntity(Reservation reservation) {
            return ChangeFormDTO.builder()
                    .id(reservation.getId())
                    .siteName(reservation.getSite().getSiteName())
                    .zoneName(reservation.getSite().getZone().getName())
                    .siteImage("/upload/1df7d7fc-2f49-4f29-a27b-71f68cbfb104_wesley-shen-2l2EslhTaOM-unsplash.jpg")
                    .checkIn(reservation.getCheckIn().toString())
                    .checkOut(reservation.getCheckOut().toString())
                    .peopleCount(reservation.getPeopleCount())
                    .totalPrice(reservation.getTotalPrice())
                    .visitorName(reservation.getVisitorName())
                    .visitorPhone(reservation.getVisitorPhone())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangeDoneDTO {
        private Long reservationId;
        private String newSiteName;
        private String newZoneName;
        private String newCheckIn;
        private String newCheckOut;
        private Integer newPeopleCount;
        private String requestDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CancelDoneDTO {
        private Long reservationId;
        private String reason;
        private String requestDate;
    }

    // [Task 4-2] 관리자용 예약 변경 상세 비교 DTO
    @Getter @Setter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminChangeDetailDTO {
        private Long id; // 예약 ID
        private String username;
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
    }

    // [Task 4-2] 관리자용 예약 취소 상세 DTO
    @Getter @Setter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminCancelDetailDTO {
        private Long id; // 예약 ID
        private String username;
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
    }
}
