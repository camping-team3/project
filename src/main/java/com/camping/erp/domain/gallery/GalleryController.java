package com.camping.erp.domain.gallery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GalleryController {

    @GetMapping("/galleries")
    public String list() {
        return "gallery/list";
    }
}
