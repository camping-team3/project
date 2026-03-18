package com.camping.erp.domain.gallery;

import com.camping.erp.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/galleries")
public class AdminGalleryController {

    private final GalleryService galleryService;

    @GetMapping
    public String list(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<GalleryResponse.ListDTO> galleryPage = galleryService.findAll(pageable);
        model.addAttribute("galleries", new PageResponse<>(galleryPage));
        return "admin/gallery/list";
    }

    @GetMapping("/save-form")
    public String saveForm() {
        return "admin/gallery/save-form";
    }

    @PostMapping("/save")
    public String save(GalleryRequest.SaveDTO saveDTO) {
        galleryService.save(saveDTO);
        return "redirect:/admin/galleries";
    }

    @GetMapping("/{id}/update-form")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        GalleryResponse.DetailDTO gallery = galleryService.findById(id);
        model.addAttribute("gallery", gallery);
        return "admin/gallery/update-form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, GalleryRequest.UpdateDTO updateDTO) {
        galleryService.update(id, updateDTO);
        return "redirect:/admin/galleries";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        galleryService.delete(id);
        return "redirect:/admin/galleries";
    }
}
