package com.camping.erp.domain.notice;

import com.camping.erp.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notices")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public String list(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                       @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<NoticeResponse.ListDTO> noticePage = noticeService.findAll(keyword, pageable);
        model.addAttribute("notices", new PageResponse<>(noticePage));
        model.addAttribute("keyword", keyword);
        return "admin/notice/list";
    }

    @GetMapping("/save-form")
    public String saveForm() {
        return "admin/notice/save-form";
    }

    @PostMapping("/save")
    public String save(NoticeRequest.SaveDTO saveDTO) {
        log.info("--- Notice Save Request ---");
        log.info("Title: {}", saveDTO.getTitle());
        log.info("Content Length: {}", saveDTO.getContent() != null ? saveDTO.getContent().length() : 0);
        log.info("IsTop: {}", saveDTO.getIsTop());
        log.info("Images Count: {}", saveDTO.getImages() != null ? saveDTO.getImages().size() : 0);
        
        noticeService.save(saveDTO);
        return "redirect:/admin/notices";
    }

    @GetMapping("/{id}/update-form")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        NoticeResponse.DetailDTO notice = noticeService.findById(id);
        model.addAttribute("notice", notice);
        return "admin/notice/update-form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, NoticeRequest.UpdateDTO updateDTO) {
        log.info("--- Notice Update Request ---");
        log.info("ID: {}", id);
        log.info("Title: {}", updateDTO.getTitle());
        log.info("IsTop: {}", updateDTO.getIsTop());
        log.info("New Images: {}", updateDTO.getImages() != null ? updateDTO.getImages().size() : 0);
        log.info("Delete Image IDs: {}", updateDTO.getDeleteImageIds() != null ? updateDTO.getDeleteImageIds().size() : 0);
        
        noticeService.update(id, updateDTO);
        return "redirect:/admin/notices";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        noticeService.delete(id);
        return "redirect:/admin/notices";
    }
}
