package com.camping.erp.domain.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {

    @GetMapping("/reservations/new")
    public String newForm() {
        return "reservation/new";
    }

    @GetMapping("/reservations/payment")
    public String payment() {
        return "reservation/payment";
    }

    @GetMapping("/reservations/complete")
    public String complete() {
        return "reservation/complete";
    }
}
