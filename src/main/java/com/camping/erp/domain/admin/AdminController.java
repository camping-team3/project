package com.camping.erp.domain.admin;

import com.camping.erp.domain.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ReservationService reservationService;

    @GetMapping("/admin")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/stat")
    public String stat() {
        return "admin/stat";
    }

    @GetMapping("/admin/reservations")
    public String reservationList(AdminRequest.ReservationSearchDTO searchDTO, Model model) {
        List<AdminResponse.ReservationListDTO> reservations = reservationService.findAllForAdmin(searchDTO);
        model.addAttribute("reservations", reservations);
        model.addAttribute("search", searchDTO);
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

    @GetMapping("/admin/notices")
    public String noticeList() {
        return "admin/notice/list";
    }

    @GetMapping("/admin/notices/new")
    public String noticeNew() {
        return "admin/notice/new";
    }

    @GetMapping("/admin/galleries")
    public String galleryList() {
        return "admin/gallery/list";
    }

    @GetMapping("/admin/galleries/new")
    public String galleryNew() {
        return "admin/gallery/new";
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
