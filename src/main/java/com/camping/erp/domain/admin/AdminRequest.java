package com.camping.erp.domain.admin;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class AdminRequest {

    @Getter @Setter
    public static class ReservationSearchDTO {
        private String keyword; // 예약자명 또는 ID

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate checkIn;

        private ReservationStatus status;
    }

    @Getter @Setter
    public static class RejectDTO {
        private String rejectionReason; // 관리자가 입력한 거절 사유
    }
}
