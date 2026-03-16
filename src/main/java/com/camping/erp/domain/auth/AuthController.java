package com.camping.erp.domain.auth;

import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final SiteService siteService;

    @GetMapping("/")
    public String index(Model model) {
        List<SiteResponse.ListDTO> sites = siteService.findAll();
        model.addAttribute("sites", sites);
        return "index";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "auth/login-form";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "auth/join-form";
    }
}
