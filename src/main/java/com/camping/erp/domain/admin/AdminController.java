package com.camping.erp.domain.admin;

import com.camping.erp.domain.site.SiteRequest;
import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final SiteService siteService;

    @GetMapping("/admin")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/sites")
    public String siteList(Model model) {
        List<SiteResponse.AdminZoneDTO> zones = siteService.findAllForAdmin();
        System.out.println("Admin Zone Count: " + zones.size());
        zones.forEach(z -> System.out.println("Zone: " + z.getName() + ", Site Count: " + (z.getSites() != null ? z.getSites().size() : 0)));
        model.addAttribute("zones", zones);
        return "admin/site/list";
    }

    @PostMapping("/admin/zones")
    public String saveZone(SiteRequest.ZoneSaveDTO requestDTO) {
        siteService.saveZone(requestDTO);
        return "redirect:/admin/sites";
    }

    @PostMapping("/admin/zones/{id}/update")
    public String updateZone(@PathVariable Long id, SiteRequest.ZoneSaveDTO requestDTO) {
        siteService.updateZone(id, requestDTO);
        return "redirect:/admin/sites";
    }

    @PostMapping("/admin/zones/{id}/delete")
    public String deleteZone(@PathVariable Long id) {
        siteService.deleteZone(id);
        return "redirect:/admin/sites";
    }

    @PostMapping("/admin/sites")
    public String saveSite(SiteRequest.SiteSaveDTO requestDTO) {
        siteService.saveSite(requestDTO);
        return "redirect:/admin/sites";
    }

    @PostMapping("/admin/sites/{id}/update")
    public String updateSite(@PathVariable Long id, SiteRequest.SiteSaveDTO requestDTO) {
        siteService.updateSite(id, requestDTO);
        return "redirect:/admin/sites";
    }

    @PostMapping("/admin/sites/{id}/delete")
    public String deleteSite(@PathVariable Long id) {
        siteService.deleteSite(id);
        return "redirect:/admin/sites";
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
