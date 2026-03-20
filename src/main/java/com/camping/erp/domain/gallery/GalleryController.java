package com.camping.erp.domain.gallery;

import com.camping.erp.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class GalleryController {
    private final GalleryService galleryService;

    // 사용자용 포토 갤러리 목록
    @GetMapping("/galleries")
    public String list(@PageableDefault(size = 8) Pageable pageable, Model model) {
        Page<GalleryResponse.ListDTO> galleryPage = galleryService.findAll(pageable);
        model.addAttribute("galleries", new PageResponse<>(galleryPage));
        return "gallery/list";
    }

    // 사용자용 포토 갤러리 상세
    @GetMapping("/galleries/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        GalleryResponse.DetailDTO gallery = galleryService.findById(id);
        model.addAttribute("gallery", gallery);
        return "gallery/detail";
    }
}
