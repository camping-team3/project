package com.camping.erp.domain.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/")
    public String index() {
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
