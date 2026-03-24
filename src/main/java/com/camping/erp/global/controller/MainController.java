package com.camping.erp.global.controller;

import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final SiteService siteService;

    @GetMapping("/")
    public String index(Model model) {
        List<SiteResponse.ListDTO> sites = siteService.findAll();
        model.addAttribute("sites", sites);
        return "index";
    }
}
