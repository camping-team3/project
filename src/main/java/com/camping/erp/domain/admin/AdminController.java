package com.camping.erp.domain.admin;

import com.camping.erp.domain.gallery.GalleryRequest;
import com.camping.erp.domain.gallery.GalleryResponse;
import com.camping.erp.domain.gallery.GalleryService;
import com.camping.erp.domain.notice.NoticeRequest;
import com.camping.erp.domain.notice.NoticeResponse;
import com.camping.erp.domain.notice.NoticeService;
import com.camping.erp.domain.reservation.ReservationService;
import com.camping.erp.domain.site.SiteRequest;
import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.SiteService;
import com.camping.erp.global.dto.PageResponse;
import com.camping.erp.global.handler.ex.Exception400;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final SiteService siteService;
    private final ReservationService reservationService;
    private final NoticeService noticeService;
    private final GalleryService galleryService;

    @GetMapping("/admin")
    public String dashboard() {
        return "admin/dashboard";
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

    @GetMapping("/admin/reservations/{id}/change")
    public String reservationChangeDetail(@PathVariable("id") Long id) {
        return "admin/reservation/change-detail";
    }

    @GetMapping("/admin/reservations/{id}/cancel")
    public String reservationCancelDetail(@PathVariable("id") Long id) {
        return "admin/reservation/cancel-detail";
    }

<<<<<<< HEAD
    // --- 공지사항 관리 ---
=======
    @GetMapping("/admin/users")
    public String userList() {
        return "admin/user/list";
    }

    @GetMapping("/admin/users/{id}")
    public String userDetail(@PathVariable Long id) {
        return "admin/user/detail";
>>>>>>> dev
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
    public String galleryList(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<GalleryResponse.ListDTO> galleryPage = galleryService.findAll(pageable);
        model.addAttribute("galleries", new PageResponse<>(galleryPage));
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
    @GetMapping("/admin/stat")
    public String stat() {
        return "admin/stat";
    }

    @GetMapping("/admin/qna")
    public String qnaList() {
        return "admin/qna/list";
    }

    @GetMapping("/admin/qna/{id}/answer")
    public String qnaAnswer(@PathVariable("id") Long id) {
        return "admin/qna/answer";
    }

    @GetMapping("/admin/sites/season")
    public String siteSeason() {
        return "admin/site/season";
    }
}
