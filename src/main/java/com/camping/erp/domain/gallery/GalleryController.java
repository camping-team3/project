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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class GalleryController {
    private final GalleryService galleryService;

    // 사용자용 포토 갤러리 목록
    @GetMapping("/galleries")
    public String list(@PageableDefault(size = 8) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
        Page<GalleryResponse.ListDTO> galleryPage = galleryService.findAll(pageable, keyword);
        model.addAttribute("galleries", new PageResponse<>(galleryPage));
        model.addAttribute("keyword", keyword);
        return "gallery/list";
    }

    // 사용자용 포토 갤러리 상세
    @GetMapping("/galleries/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        // 조회수 증가가 포함된 서비스 메서드 호출
        GalleryResponse.DetailDTO gallery = galleryService.findByIdWithViewCount(id);
        model.addAttribute("gallery", gallery);
        return "gallery/detail";
    }
}
