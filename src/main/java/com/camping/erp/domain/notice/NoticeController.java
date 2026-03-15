package com.camping.erp.domain.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    // 사용자용 공지사항 목록
    @GetMapping("/notices")
    public String list(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                       @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<NoticeResponse.ListDTO> notices = noticeService.findAll(keyword, pageable);
        model.addAttribute("notices", notices);
        model.addAttribute("keyword", keyword);
        return "notice/list";
    }

    // 사용자용 공지사항 상세
    @GetMapping("/notices/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        NoticeResponse.DetailDTO notice = noticeService.findById(id);
        model.addAttribute("notice", notice);
        return "notice/detail";
    }
}
