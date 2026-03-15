package com.camping.erp.domain.reservation;

import com.camping.erp.domain.site.SiteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations/new")
    public String newForm(ReservationRequest.SearchDTO searchDTO, Model model) {
        // 1. 모든 기본값 세팅 (폼 초기값 결정)
        if (searchDTO.getCheckIn() == null) {
            searchDTO.setCheckIn(LocalDate.now());
        }
        if (searchDTO.getCheckOut() == null) {
            searchDTO.setCheckOut(searchDTO.getCheckIn().plusDays(1));
        }
        if (!searchDTO.getCheckOut().isAfter(searchDTO.getCheckIn())) {
            searchDTO.setCheckOut(searchDTO.getCheckIn().plusDays(1));
        }
        if (searchDTO.getPeopleCount() == null) {
            searchDTO.setPeopleCount(2);
        }

        // 2. 서비스 호출 (이미 기본값이 채워진 searchDTO 사용)
        List<SiteResponse.ListDTO> sites = reservationService.findAvailableSites(searchDTO);

        // 3. 뷰를 위한 데이터 가공
        long nights = ChronoUnit.DAYS.between(searchDTO.getCheckIn(), searchDTO.getCheckOut());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E)", Locale.KOREAN);

        model.addAttribute("sites", sites);
        model.addAttribute("search", searchDTO);
        model.addAttribute("nights", nights);
        model.addAttribute("checkInDisplay", searchDTO.getCheckIn().format(formatter));
        model.addAttribute("checkOutDisplay", searchDTO.getCheckOut().format(formatter));

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
