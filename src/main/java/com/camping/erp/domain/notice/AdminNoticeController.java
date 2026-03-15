package com.camping.erp.domain.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notices")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public String list(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                       @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<NoticeResponse.ListDTO> notices = noticeService.findAll(keyword, pageable);
        model.addAttribute("notices", notices);
        model.addAttribute("keyword", keyword);
        return "admin/notice/list";
    }

    @GetMapping("/save-form")
    public String saveForm() {
        return "admin/notice/save-form";
    }

    @PostMapping("/save")
    public String save(NoticeRequest.SaveDTO saveDTO) {
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
        noticeService.update(id, updateDTO);
        return "redirect:/admin/notices";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        noticeService.delete(id);
        return "redirect:/admin/notices";
    }
}
