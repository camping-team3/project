package com.camping.erp.domain.review;

import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.domain.reservation.ReservationRepository;
import com.camping.erp.domain.review.dto.ReviewRequest;
import com.camping.erp.domain.user.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReservationRepository reservationRepository;
    private final HttpSession session;

    @GetMapping("/reviews/new")
    public String newForm(@RequestParam(name = "reservationId") Long reservationId, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (!reservation.getUser().getId().equals(sessionUser.getId())) {
            throw new IllegalArgumentException("본인의 예약만 리뷰를 작성할 수 있습니다.");
        }

        model.addAttribute("reservation", reservation);
        return "review/new";
    }

    @PostMapping("/reviews/save")
    public String save(@Valid ReviewRequest.SaveDTO req, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getAllErrors().get(0).getDefaultMessage());
        }

        reviewService.save(sessionUser, req);
        return "redirect:/mypage/reservations"; // 리뷰 등록 후 마이페이지 예약 내역으로
    }
}
