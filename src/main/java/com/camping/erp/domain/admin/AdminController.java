package com.camping.erp.domain.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/stat")
    public String stat() {
        return "admin/stat";
    }

    @GetMapping("/admin/reservations")
    public String reservationList() {
        return "admin/reservation/list";
    }

    @GetMapping("/admin/reservations/{id}/change")
    public String reservationChangeDetail(@PathVariable Long id) {
        return "admin/reservation/change-detail";
    }

    @GetMapping("/admin/reservations/{id}/cancel")
    public String reservationCancelDetail(@PathVariable Long id) {
        return "admin/reservation/cancel-detail";
    }

    @GetMapping("/admin/users")
    public String userList() {
        return "admin/user/list";
    }

    @GetMapping("/admin/users/{id}")
    public String userDetail(@PathVariable Long id) {
        return "admin/user/detail";
    }

    @GetMapping("/admin/qna")
    public String qnaList() {
        return "admin/qna/list";
    }

    @GetMapping("/admin/qna/{id}/answer")
    public String qnaAnswer(@PathVariable Long id) {
        return "admin/qna/answer";
    }

    @GetMapping("/admin/sites/season")
    public String siteSeason() {
        return "admin/site/season";
    }
}
