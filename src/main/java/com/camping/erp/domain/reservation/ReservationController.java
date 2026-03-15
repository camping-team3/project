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
        // 인원수 기본값 설정
        if (searchDTO.getPeopleCount() == null) {
            searchDTO.setPeopleCount(2);
        }

        List<SiteResponse.ListDTO> sites = reservationService.findAvailableSites(searchDTO);

        // 검색 데이터 재추출 (서비스에서 설정된 기본값 포함)
        LocalDate checkIn = searchDTO.getCheckIn();
        LocalDate checkOut = searchDTO.getCheckOut();
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        // 날짜 포맷팅 (2024.05.24(금) 형식)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E)", Locale.KOREAN);

        model.addAttribute("sites", sites);
        model.addAttribute("search", searchDTO);
        model.addAttribute("nights", nights);
        model.addAttribute("checkInDisplay", checkIn.format(formatter));
        model.addAttribute("checkOutDisplay", checkOut.format(formatter));

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
