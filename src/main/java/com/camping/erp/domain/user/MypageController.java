package com.camping.erp.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MypageController {

    @GetMapping("/mypage/reservations/{id}/change")
    public String reservationChange(@PathVariable("id") Long id) {
        return "mypage/reservation-change";
    }

    @GetMapping("/mypage/reservations/{id}/change-done")
    public String reservationChangeDone(@PathVariable("id") Long id) {
        return "mypage/reservation-change-done";
    }

    @GetMapping("/mypage/reservations/{id}/cancel")
    public String reservationCancel(@PathVariable("id") Long id) {
        return "mypage/reservation-cancel";
    }

    @GetMapping("/mypage/reservations/{id}/cancel-done")
    public String reservationCancelDone(@PathVariable("id") Long id) {
        return "mypage/reservation-cancel-done";
    }
}
