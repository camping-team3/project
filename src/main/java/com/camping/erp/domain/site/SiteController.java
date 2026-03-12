package com.camping.erp.domain.site;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;
    private final ZoneService zoneService;

    // ==========================================================
    // 1. 사용자(User-facing) 기능
    // ==========================================================

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            Model model) {

        List<SiteResponse.MainDTO> zones = siteService.getAvailableZonesWithSites(checkIn, checkOut);
        model.addAttribute("zones", zones);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);

        return "index";
    }

    // ==========================================================
    // 2. 관리자(Admin-only) 기능
    // 경로가 /admin으로 시작하므로 AdminInterceptor에 의해 보호됨
    // ==========================================================

    // 구역 및 사이트 통합 관리 페이지
    @GetMapping("/admin/site/management")
    public String management(Model model) {
        List<Zone> zones = zoneService.findAll();
        List<SiteResponse.AdminListDTO> zoneDTOs = zones.stream()
                .map(SiteResponse.AdminListDTO::new)
                .toList();
        model.addAttribute("zones", zoneDTOs);
        return "admin/site/management";
    }

    // 구역 등록 폼
    @GetMapping("/admin/zone/save-form")
    public String zoneSaveForm() {
        return "admin/site/zoneSaveForm";
    }

    // 구역 등록 처리
    @PostMapping("/admin/zone/save")
    public String zoneSave(SiteRequest.ZoneSaveDTO requestDTO) {
        zoneService.save(requestDTO);
        return "redirect:/admin/site/management";
    }

    // 구역 수정 폼
    @GetMapping("/admin/zone/{id}/update-form")
    public String zoneUpdateForm(@PathVariable Long id, Model model) {
        Zone zone = zoneService.findById(id);
        model.addAttribute("zone", new SiteResponse.ZoneUpdateFormDTO(zone));
        return "admin/site/zoneUpdateForm";
    }

    // 구역 수정 처리
    @PostMapping("/admin/zone/{id}/update")
    public String zoneUpdate(@PathVariable Long id, SiteRequest.ZoneUpdateDTO requestDTO) {
        zoneService.update(id, requestDTO);
        return "redirect:/admin/site/management";
    }

    // 구역 성수기 토글 처리
    @PostMapping("/admin/zone/{id}/toggle-peak")
    public String zoneTogglePeak(@PathVariable Long id) {
        zoneService.togglePeak(id);
        return "redirect:/admin/site/management";
    }

    // 구역 삭제 처리
    @PostMapping("/admin/zone/{id}/delete")
    public String zoneDelete(@PathVariable Long id) {
        zoneService.delete(id);
        return "redirect:/admin/site/management";
    }

    // 사이트 등록 폼
    @GetMapping("/admin/site/save-form")
    public String siteSaveForm(Model model) {
        List<SiteResponse.ZoneUpdateFormDTO> zones = zoneService.findAll().stream()
                .map(SiteResponse.ZoneUpdateFormDTO::new)
                .toList();
        model.addAttribute("zones", zones);
        return "admin/site/siteSaveForm";
    }

    // 사이트 등록 처리
    @PostMapping("/admin/site/save")
    public String siteSave(SiteRequest.SiteSaveDTO requestDTO) {
        siteService.save(requestDTO);
        return "redirect:/admin/site/management";
    }

    // 사이트 수정 폼
    @GetMapping("/admin/site/{id}/update-form")
    public String siteUpdateForm(@PathVariable Long id, Model model) {
        Site site = siteService.findById(id);
        List<SiteResponse.ZoneUpdateFormDTO> zones = zoneService.findAll().stream()
                .map(SiteResponse.ZoneUpdateFormDTO::new)
                .toList();
        model.addAttribute("site", new SiteResponse.SiteUpdateFormDTO(site));
        model.addAttribute("zones", zones);
        return "admin/site/siteUpdateForm";
    }

    // 사이트 수정 처리
    @PostMapping("/admin/site/{id}/update")
    public String siteUpdate(@PathVariable Long id, SiteRequest.SiteUpdateDTO requestDTO) {
        siteService.update(id, requestDTO);
        return "redirect:/admin/site/management";
    }

    // 사이트 삭제 처리
    @PostMapping("/admin/site/{id}/delete")
    public String siteDelete(@PathVariable Long id) {
        siteService.delete(id);
        return "redirect:/admin/site/management";
    }
}
