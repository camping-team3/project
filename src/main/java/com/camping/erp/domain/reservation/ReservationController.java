package com.camping.erp.domain.reservation;

import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservationController {
    private final SiteService siteService;

    @GetMapping("/reservations/new")
    public String newForm(@RequestParam(value = "checkIn", required = false) LocalDate checkIn,
                          @RequestParam(value = "checkOut", required = false) LocalDate checkOut,
                          Model model) {
        
        if (checkIn == null) checkIn = LocalDate.now();
        if (checkOut == null) checkOut = LocalDate.now().plusDays(1);

        List<SiteResponse.ListDTO> availableSites = siteService.findAvailableSites(checkIn, checkOut);
        model.addAttribute("availableSites", availableSites);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);

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
