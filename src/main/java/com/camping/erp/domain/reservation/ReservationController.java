package com.camping.erp.domain.reservation;

import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.SiteService;
import com.camping.erp.domain.user.User;
import com.camping.erp.domain.user.UserResponse;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class ReservationController {
    private final SiteService siteService;

    private final ReservationService reservationService;
    private final HttpSession session;

    // 예약 페이지 (예약 가능 사이트 목록 조회)
    @GetMapping("/reservations/new")
    public String newForm(ReservationRequest.SearchDTO searchDTO, Model model) {
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

        List<SiteResponse.ResevationAvailableListDTO> sites = reservationService.findAvailableSites(searchDTO);

        long nights = ChronoUnit.DAYS.between(searchDTO.getCheckIn(), searchDTO.getCheckOut());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E)", Locale.KOREAN);

        model.addAttribute("sites", sites);
        model.addAttribute("search", searchDTO);
        model.addAttribute("nights", nights);
        model.addAttribute("checkInDisplay", searchDTO.getCheckIn().format(formatter));
        model.addAttribute("checkOutDisplay", searchDTO.getCheckOut().format(formatter));

        return "reservation/new";
    }

    // 결제 페이지 (결제 폼 데이터 준비)
    @GetMapping("/reservations/payment")
    public String paymentForm(ReservationRequest.ReserveDTO request, Model model) {
        ReservationResponse.PaymentFormDTO paymentInfo = reservationService.getPaymentForm(request);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E)", Locale.KOREAN);
        String dateRange = String.format("%s - %s (%d박)",
                paymentInfo.getCheckIn().format(formatter),
                paymentInfo.getCheckOut().format(formatter),
                paymentInfo.getNights());

        model.addAttribute("payment", paymentInfo);
        model.addAttribute("dateRange", dateRange);

        return "reservation/payment";
    }

    // 예약 실행 (최종 검증 및 저장)
    @PostMapping("/reservations/reserve")
    public String reserve(ReservationRequest.ReserveDTO request) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        ReservationResponse.ReserveDTO response = reservationService.reserve(request, sessionUser);
        return "redirect:/reservations/complete?id=" + response.getId();
    }

    // 예약 완료 페이지 (예약 정보 확인 가능)
    @GetMapping("/reservations/complete")
    public String complete(@RequestParam("id") Long id, Model model) {
        ReservationResponse.CompleteDTO reservation = reservationService.getCompleteDetails(id);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E)", Locale.KOREAN);
        model.addAttribute("reservation", reservation);
        model.addAttribute("checkInDisplay", reservation.getCheckIn().format(formatter));
        model.addAttribute("checkOutDisplay", reservation.getCheckOut().format(formatter));

        return "reservation/complete";
    }
}
