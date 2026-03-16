package com.camping.erp.domain.admin;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class AdminResponse {

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
