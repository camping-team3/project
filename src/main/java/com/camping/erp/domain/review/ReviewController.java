package com.camping.erp.domain.review;

import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.domain.reservation.ReservationRepository;
import com.camping.erp.domain.review.dto.ReviewRequest;
import com.camping.erp.domain.review.dto.ReviewResponse;
import com.camping.erp.domain.user.User;
import com.camping.erp.domain.user.UserResponse;
import com.camping.erp.domain.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/reviews")
    public String list(@PageableDefault(size = 6, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        ReviewResponse.ListWrapperDTO respDTO = reviewService.findAll(pageable);
        model.addAttribute("model", respDTO);
        return "review/list";
    }

    // --- 마이페이지 리뷰 관리 ---

    @GetMapping("/mypage/reviews")
    public String myReviews(Model model) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        List<ReviewResponse.ListDTO> reviews = reviewService.findByUserId(sessionUser.getId());
        model.addAttribute("reviews", reviews);
        return "mypage/reviews";
    }

    @GetMapping("/reviews/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        Review review = reviewService.findById(id);
        if (!review.getUser().getId().equals(sessionUser.getId())) {
            throw new IllegalArgumentException("본인의 리뷰만 수정할 수 있습니다.");
        }

        model.addAttribute("review", review);
        return "review/edit";
    }

    @PostMapping("/reviews/{id}/update")
    public String update(@PathVariable("id") Long id, @Valid ReviewRequest.UpdateDTO req, Errors errors) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getAllErrors().get(0).getDefaultMessage());
        }

        User user = userService.findById(sessionUser.getId());
        reviewService.update(id, user, req);
        return "redirect:/mypage/reviews";
    }

    @PostMapping("/reviews/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        User user = userService.findById(sessionUser.getId());
        reviewService.deleteByUser(id, user);
        return "redirect:/mypage/reviews";
    }

    // --- 리뷰 작성 ---
    @GetMapping("/reviews/new")
    public String newForm(@RequestParam(name = "reservationId") Long reservationId, Model model) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
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
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getAllErrors().get(0).getDefaultMessage());
        }

        User user = userService.findById(sessionUser.getId());
        reviewService.save(user, req);
        return "redirect:/mypage/reservations"; // 리뷰 등록 후 마이페이지 예약 내역으로
    }
}
