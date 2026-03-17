package com.camping.erp.domain.qna;

import com.camping.erp.domain.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;
    private final HttpSession session;

    @GetMapping("/qna")
    public String list(@RequestParam(value = "status", defaultValue = "all") String status, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        List<QnaResponse.ListDTO> qnas = qnaService.findAll(status, sessionUser);
        model.addAttribute("qnas", qnas);
        
        // 필터링 활성화 상태 표시용
        model.addAttribute("isAll", "all".equalsIgnoreCase(status));
        model.addAttribute("isPending", "pending".equalsIgnoreCase(status));
        model.addAttribute("isCompleted", "completed".equalsIgnoreCase(status));
        
        return "qna/list";
    }

    @GetMapping("/qna/new")
    public String newForm() {
        return "qna/new";
    }

    @PostMapping("/qna/save")
    public String save(QnaRequest.SaveDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        qnaService.save(requestDTO, sessionUser);
        return "redirect:/qna";
    }

    @GetMapping("/qna/{id}")
    public String detail(@PathVariable Long id, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        QnaResponse.DetailDTO qna = qnaService.findById(id, sessionUser);
        model.addAttribute("qna", qna);
        return "qna/detail";
    }

    @GetMapping("/qna/{id}/edit-form")
    public String editForm(@PathVariable Long id, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        QnaResponse.DetailDTO qna = qnaService.findById(id, sessionUser);
        
        // 답변 완료된 경우 수정 폼 접근 차단
        if (qna.getIsAnswered()) {
            throw new com.camping.erp.global.handler.ex.Exception400("답변이 완료된 질문은 수정할 수 없습니다.");
        }
        
        model.addAttribute("qna", qna);
        return "qna/edit-form";
    }

    @PostMapping("/qna/{id}/update")
    public String update(@PathVariable Long id, QnaRequest.UpdateDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        qnaService.update(id, requestDTO, sessionUser);
        return "redirect:/qna";
    }

    @PostMapping("/qna/{id}/delete")
    @ResponseBody
    public String delete(@PathVariable Long id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        qnaService.delete(id, sessionUser);
        
        return """
                <script>
                    alert('문의가 정상적으로 삭제되었습니다.');
                    location.href = '/qna';
                </script>
                """;
    }
}
