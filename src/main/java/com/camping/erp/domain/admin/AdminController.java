package com.camping.erp.domain.admin;

import com.camping.erp.domain.qna.QnaResponse;
import com.camping.erp.domain.qna.QnaService;
import com.camping.erp.domain.site.SiteRequest;
import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.SiteService;
import com.camping.erp.domain.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final SiteService siteService;
    private final QnaService qnaService;
    private final HttpSession session;

    @GetMapping("/admin")
    public String dashboard() {
        return "admin/dashboard";
    }

    // ... (기존 메서드들 유지)

    @GetMapping("/admin/qna")
    public String qnaList(@RequestParam(value = "status", defaultValue = "all") String status, Model model) {
        User sessionAdmin = (User) session.getAttribute("sessionUser");
        List<QnaResponse.ListDTO> qnas = qnaService.findAll(status, sessionAdmin);
        model.addAttribute("qnas", qnas);

        // 통계 데이터 추가
        Map<String, Long> stats = qnaService.getStatistics();
        model.addAllAttributes(stats);

        // 필터링 활성화 상태 표시용
        model.addAttribute("status", "all".equalsIgnoreCase(status) ? null : status);
        model.addAttribute("isAll", "all".equalsIgnoreCase(status));
        model.addAttribute("isPending", "pending".equalsIgnoreCase(status));
        model.addAttribute("isCompleted", "completed".equalsIgnoreCase(status));

        return "admin/qna/list";
    }

    @GetMapping("/admin/qna/{id}/answer")
    public String qnaAnswer(@PathVariable Long id, Model model) {
        User sessionAdmin = (User) session.getAttribute("sessionUser");
        QnaResponse.DetailDTO qna = qnaService.findById(id, sessionAdmin);
        model.addAttribute("qna", qna);
        return "admin/qna/answer";
    }

    @PostMapping("/admin/qna/{id}/comment")
    public String saveComment(@PathVariable Long id, String content) {
        User sessionAdmin = (User) session.getAttribute("sessionUser");
        qnaService.saveComment(id, content, sessionAdmin);
        return "redirect:/admin/qna";
    }

    @PostMapping("/admin/qna/{id}/delete")
    @ResponseBody
    public String qnaDelete(@PathVariable Long id) {
        User sessionAdmin = (User) session.getAttribute("sessionUser");
        qnaService.delete(id, sessionAdmin);
        return """
                <script>
                    alert('해당 문의가 삭제되었습니다.');
                    location.href = '/admin/qna';
                </script>
                """;
    }

    @GetMapping("/admin/sites/season")
    public String siteSeason() {
        return "admin/site/season";
    }
}
