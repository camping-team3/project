package com.camping.erp.domain.admin;

import com.camping.erp.domain.gallery.GalleryRequest;
import com.camping.erp.domain.gallery.GalleryResponse;
import com.camping.erp.domain.gallery.GalleryService;
import com.camping.erp.domain.notice.NoticeRequest;
import com.camping.erp.domain.notice.NoticeResponse;
import com.camping.erp.domain.notice.NoticeService;
import com.camping.erp.domain.payment.RefundResponse;
import com.camping.erp.domain.payment.RefundService;
import com.camping.erp.domain.qna.QnaResponse;
import com.camping.erp.domain.qna.QnaService;
import com.camping.erp.domain.reservation.ReservationResponse;
import com.camping.erp.domain.reservation.ReservationService;
import com.camping.erp.domain.review.ReviewService;
import com.camping.erp.domain.site.SiteRequest;
import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.SiteService;
import com.camping.erp.domain.user.UserResponse;
import com.camping.erp.domain.user.UserService;
import com.camping.erp.global.dto.PageResponse;
import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.util.Resp;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final SiteService siteService;
    private final ReservationService reservationService;
    private final NoticeService noticeService;
    private final GalleryService galleryService;
    private final QnaService qnaService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final RefundService refundService;
    private final HttpSession session;

    @GetMapping("/admin")
    public String dashboard(Model model) {
        // 1. 미처리 QnA 통계 개수 조회 (unansweredCount 포함)
        Map<String, Long> stats = qnaService.getStatistics();
        model.addAllAttributes(stats);

        // 2. 미처리 QnA 목록 최신 5개 조회 (status="pending" 필터 적용)
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
        QnaResponse.PageDTO pageDTO = qnaService.findAll("pending", null, pageable);
        model.addAttribute("unansweredQnas", pageDTO.getQnas());

        return "admin/dashboard";
    }

    // --- 리뷰 관리 ---
    @GetMapping("/admin/reviews")
    public String reviewList(@RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "sort", defaultValue = "latest") String sort,
                             @RequestParam(name = "filter", defaultValue = "all") String filter,
                             @RequestParam(name = "keyword", defaultValue = "") String keyword,
                             Model model) {
        Pageable pageable = PageRequest.of(page, 10, sort.equals("danger") ? Sort.by("aiDangerScore").descending() : Sort.by("id").descending());
        AdminResponse.ReviewPageDTO response = reviewService.findAllForAdmin(pageable);
        
        model.addAttribute("response", response);
        model.addAttribute("isDangerSort", "danger".equals(sort));
        model.addAttribute("keyword", keyword);
        return "admin/review/list";
    }

    @PostMapping("/admin/reviews/{id}/keep")
    public String keepReview(@PathVariable("id") Long id) {
        reviewService.keepByAdmin(id);
        return "redirect:/admin/reviews";
    }

    @PostMapping("/admin/reviews/{id}/delete")
    public String deleteReview(@PathVariable("id") Long id, @RequestParam("reason") String reason) {
        reviewService.deleteByAdmin(id, reason);
        return "redirect:/admin/reviews";
    }

    @PostMapping("/admin/users/{userId}/expel")
    public String expelUser(@PathVariable("userId") Long userId) {
        userService.expelUser(userId);
        return "redirect:/admin/reviews";
    }

    // --- 사이트 관리 ---
    @GetMapping("/admin/sites")
    public String siteList(Model model) {
        List<SiteResponse.AdminZoneDTO> zones = siteService.findAllForAdmin();
        model.addAttribute("zones", zones);
        return "admin/site/list";
    }

    @PostMapping("/admin/zones")
    public String saveZone(SiteRequest.ZoneSaveDTO requestDTO) {
        siteService.saveZone(requestDTO);
        return "redirect:/admin/sites";
    }

    @PostMapping("/admin/zones/{id}/update")
    public String updateZone(@PathVariable("id") Long id, SiteRequest.ZoneSaveDTO requestDTO) {
        siteService.updateZone(id, requestDTO);
        return "redirect:/admin/sites";
    }

    @PostMapping("/admin/zones/{id}/delete")
    public String deleteZone(@PathVariable("id") Long id) {
        siteService.deleteZone(id);
        return "redirect:/admin/sites";
    }

    @PostMapping("/admin/sites")
    public String saveSite(SiteRequest.SiteSaveDTO requestDTO) {
        siteService.saveSite(requestDTO);
        return "redirect:/admin/sites";
    }

    @PostMapping("/admin/sites/{id}/update")
    public String updateSite(@PathVariable("id") Long id, SiteRequest.SiteSaveDTO requestDTO) {
        siteService.updateSite(id, requestDTO);
        return "redirect:/admin/sites";
    }

    @PostMapping("/admin/sites/{id}/delete")
    public String deleteSite(@PathVariable("id") Long id) {
        siteService.deleteSite(id);
        return "redirect:/admin/sites";
    }

    // --- 예약 관리 ---
    @GetMapping("/admin/reservations")
    public String reservationList(AdminRequest.ReservationSearchDTO searchDTO,
            @RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        AdminResponse.ReservationPageDTO response = reservationService.findAllForAdmin(searchDTO, pageable);
        model.addAttribute("response", response);
        model.addAttribute("search", searchDTO);
        return "admin/reservation/list";
    }

    @GetMapping("/admin/reservations/{id}/change-detail")
    public String reservationChangeDetail(@PathVariable("id") Long id, Model model) {
        AdminResponse.AdminChangeDetailDTO detail = reservationService.getAdminChangeDetail(id);
        model.addAttribute("detail", detail);
        return "admin/reservation/change-detail";
    }

    @GetMapping("/admin/reservations/{id}/cancel-detail")
    public String reservationCancelDetail(@PathVariable("id") Long id, Model model) {
        AdminResponse.AdminCancelDetailDTO detail = reservationService.getAdminCancelDetail(id);
        model.addAttribute("detail", detail);
        return "admin/reservation/cancel-detail";
    }

    @GetMapping("/admin/reservations/{id}/detail")
    public String reservationDetail(@PathVariable("id") Long id, Model model) {
        AdminResponse.AdminReservationDetailDTO detail = reservationService.getAdminReservationDetail(id);
        model.addAttribute("detail", detail);
        return "admin/reservation/detail";
    }

    @GetMapping("/api/admin/reservations/{id}/refund-info")
    @ResponseBody
    public ResponseEntity<?> getRefundInfo(@PathVariable("id") Long id) {
        RefundResponse.RefundInfo info = refundService.getRefundInfo(id);
        return Resp.ok(info);
    }

    @PostMapping("/admin/reservations/{id}/refund")
    @ResponseBody
    public ResponseEntity<?> approveRefund(@PathVariable("id") Long id, @RequestParam("reason") String reason) {
        refundService.approveRefund(id, reason);
        return Resp.ok("환불 승인 및 결제 취소가 완료되었습니다.");
    }

    @PostMapping("/admin/reservations/{id}/approve")
    public String approveReservation(@PathVariable("id") Long id) {
        reservationService.approveRequest(id);
        return "redirect:/admin/reservations";
    }

    @PostMapping("/admin/reservations/{id}/reject")
    public String rejectReservation(@PathVariable("id") Long id, AdminRequest.RejectDTO rejectDTO) {
        reservationService.rejectRequest(id, rejectDTO);
        return "redirect:/admin/reservations";
    }

    // --- 공지사항 관리 ---
    @GetMapping("/admin/notices")
    public String noticeList(@RequestParam(name = "keyword", defaultValue = "") String keyword,
            @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<NoticeResponse.ListDTO> noticePage = noticeService.findAll(keyword, pageable);
        model.addAttribute("notices", new PageResponse<>(noticePage));
        model.addAttribute("keyword", keyword);
        return "admin/notice/list";
    }

    @GetMapping("/admin/notices/save-form")
    public String noticeSaveForm() {
        return "admin/notice/save-form";
    }

    @PostMapping("/admin/notices/save")
    public String noticeSave(@Valid NoticeRequest.SaveDTO saveDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new Exception400(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        noticeService.save(saveDTO);
        return "redirect:/admin/notices";
    }

    @GetMapping("/admin/notices/{id}/update-form")
    public String noticeUpdateForm(@PathVariable("id") Long id, Model model) {
        NoticeResponse.DetailDTO notice = noticeService.findById(id);
        model.addAttribute("notice", notice);
        return "admin/notice/update-form";
    }

    @PostMapping("/admin/notices/{id}/update")
    public String noticeUpdate(@PathVariable("id") Long id, @Valid NoticeRequest.UpdateDTO updateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new Exception400(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        noticeService.update(id, updateDTO);
        return "redirect:/admin/notices";
    }

    @PostMapping("/admin/notices/{id}/delete")
    public String noticeDelete(@PathVariable("id") Long id) {
        noticeService.delete(id);
        return "redirect:/admin/notices";
    }

    // --- 갤러리 관리 ---
    @GetMapping("/admin/galleries")
    public String galleryList(@PageableDefault(size = 10) Pageable pageable, 
                              @RequestParam(value = "keyword", required = false) String keyword, 
                              Model model) {
        Page<GalleryResponse.ListDTO> galleryPage = galleryService.findAll(pageable, keyword);
        model.addAttribute("galleries", new PageResponse<>(galleryPage));
        model.addAttribute("keyword", keyword);
        return "admin/gallery/list";
    }

    @GetMapping("/admin/galleries/save-form")
    public String gallerySaveForm() {
        return "admin/gallery/save-form";
    }

    @PostMapping("/admin/galleries/save")
    public String gallerySave(@Valid GalleryRequest.SaveDTO saveDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new Exception400(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        galleryService.save(saveDTO);
        return "redirect:/admin/galleries";
    }

    @GetMapping("/admin/galleries/{id}/update-form")
    public String galleryUpdateForm(@PathVariable("id") Long id, Model model) {
        GalleryResponse.DetailDTO gallery = galleryService.findById(id);
        model.addAttribute("gallery", gallery);
        return "admin/gallery/update-form";
    }

    @PostMapping("/admin/galleries/{id}/update")
    public String galleryUpdate(@PathVariable("id") Long id, @Valid GalleryRequest.UpdateDTO updateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new Exception400(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        galleryService.update(id, updateDTO);
        return "redirect:/admin/galleries";
    }

    @PostMapping("/admin/galleries/{id}/delete")
    public String galleryDelete(@PathVariable("id") Long id) {
        galleryService.delete(id);
        return "redirect:/admin/galleries";
    }

    // --- 기타 ---
    @GetMapping("/admin/qna")
    public String qnaList(
            @RequestParam(value = "status", defaultValue = "all") String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {
        
        UserResponse.LoginDTO sessionAdmin = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());
        QnaResponse.PageDTO pageDTO = qnaService.findAll(status, sessionAdmin, pageable);
        
        model.addAttribute("qnas", pageDTO.getQnas());
        model.addAttribute("pagination", pageDTO);

        Map<String, Long> stats = qnaService.getStatistics();
        model.addAllAttributes(stats);

        model.addAttribute("status", status);
        model.addAttribute("isAll", "all".equalsIgnoreCase(status));
        model.addAttribute("isPending", "pending".equalsIgnoreCase(status));
        model.addAttribute("isCompleted", "completed".equalsIgnoreCase(status));

        return "admin/qna/list";
    }

    @GetMapping("/admin/qna/{id}/answer")
    public String qnaAnswer(@PathVariable("id") Long id, Model model) {
        UserResponse.LoginDTO sessionAdmin = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        QnaResponse.DetailDTO qna = qnaService.findById(id, sessionAdmin);
        model.addAttribute("qna", qna);
        return "admin/qna/answer";
    }

    @PostMapping("/admin/qna/{id}/comment")
    public String saveComment(@PathVariable("id") Long id, @RequestParam("content") String content) {
        UserResponse.LoginDTO sessionAdmin = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        log.debug("답변 등록 시도 - 문의 ID: {}, 관리자 ID: {}", id, sessionAdmin.getId());
        qnaService.saveComment(id, content, sessionAdmin);
        return "redirect:/admin/qna/" + id + "/answer";
    }

    @PostMapping("/admin/qna/{id}/delete")
    @ResponseBody
    public String qnaDelete(@PathVariable("id") Long id) {
        UserResponse.LoginDTO sessionAdmin = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        qnaService.delete(id, sessionAdmin);
        return """
                <script>
                    alert('해당 문의가 삭제되었습니다.');
                    location.href = '/admin/qna';
                </script>
                """;
    }

    @GetMapping("/admin/sites/season")
    public String siteSeason(Model model) {
        LocalDate now = LocalDate.now();
        model.addAttribute("currentYear", now.getYear());
        model.addAttribute("currentMonth", now.getMonthValue());

        List<AdminResponse.CalendarDayDTO> calendarDays = new ArrayList<>();
        LocalDate firstOfMonth = now.withMonth(now.getMonthValue()).withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; 
        
        LocalDate startDate = firstOfMonth.minusDays(dayOfWeek);
        for (int i = 0; i < 42; i++) {
            LocalDate date = startDate.plusDays(i);
            calendarDays.add(AdminResponse.CalendarDayDTO.builder()
                    .day(date.getDayOfMonth())
                    .date(date)
                    .isCurrentMonth(date.getMonthValue() == now.getMonthValue())
                    .isToday(date.equals(now))
                    .build());
        }
        model.addAttribute("calendarDays", calendarDays);

        return "admin/site/season";
    }
}
