package com.camping.erp.domain.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    // 회원가입 페이지
    @GetMapping("/join-form")
    public String joinForm() {
        return "auth/join-form";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(UserRequest.JoinDTO request) {
        userService.join(request);
        return "redirect:/login-form";
    }

    // 로그인 페이지
    @GetMapping("/login-form")
    public String loginForm() {
        return "auth/login-form";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO request) {
        UserResponse.LoginDTO sessionUser = userService.login(request);
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    // 마이페이지 홈
    @GetMapping("/mypage")
    public String home(Model model) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        UserResponse.DetailDTO user = userService.findUser(sessionUser.getId());
        model.addAttribute("user", user);
        return "mypage/home";
    }

    // 예약 내역
    @GetMapping("/mypage/reservations")
    public String reservations() {
        return "mypage/reservations";
    }

    // 내 리뷰
    @GetMapping("/mypage/reviews")
    public String reviews() {
        return "mypage/reviews";
    }

    // 예약 변경
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
