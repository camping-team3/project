package com.camping.erp.domain.qna;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QnaController {

    @GetMapping("/qna")
    public String list() {
        return "qna/list";
    }

    @GetMapping("/qna/new")
    public String newForm() {
        return "qna/new";
    }
}
