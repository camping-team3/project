package com.camping.erp.domain.review;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReviewController {

    @GetMapping("/reviews/new")
    public String newForm() {
        return "review/new";
    }
}
