package com.camping.erp.domain.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SiteController {

    @GetMapping("/sites/{id}")
    public String detail(@PathVariable Long id) {
        return "site/detail";
    }
}
