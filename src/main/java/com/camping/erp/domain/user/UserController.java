package com.camping.erp.domain.user;

import com.camping.erp.global.util.Resp;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users/check-username")
    public @ResponseBody ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
        boolean isDuplicate = userService.existsByUsername(username);
        if (isDuplicate) {  
            return Resp.fail(org.springframework.http.HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다.");
        }
        return Resp.ok("사용 가능한 아이디입니다.");
    }

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
    public String login(UserRequest.LoginDTO request, HttpSession session) {
        UserResponse.LoginDTO sessionUser = userService.login(request);
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login-form";
    }

    // 마이페이지 홈
    @GetMapping("/mypage")
    public String home(Model model, HttpSession session) {
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

    // 예약 상세 보기
    @GetMapping("/mypage/reservations/{id}")
    public String reservationDetail(@PathVariable("id") Long id, Model model, HttpSession session) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        UserResponse.DetailDTO user = userService.findUser(sessionUser.getId());
        model.addAttribute("user", user);
        return "mypage/reservation-detail";
    }
}
