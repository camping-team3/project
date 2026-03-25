package com.camping.erp.domain.user;

import com.camping.erp.domain.user.enums.UserRole;
import com.camping.erp.domain.user.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    // 회원 목록 조회
    @GetMapping("/users")
    public String userList(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        Page<User> userPage = userService.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id")));
        
        // [수정] 서비스 코드 수정 없이 컨트롤러에서 페이징 정보를 미리 계산하여 모델에 추가합니다.
        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", userPage.getNumber());
        model.addAttribute("displayPage", userPage.getNumber() + 1); // 1부터 시작하는 페이지 번호
        model.addAttribute("prevPage", userPage.getNumber() - 1);
        model.addAttribute("nextPage", userPage.getNumber() + 1);
        model.addAttribute("isFirst", userPage.isFirst());
        model.addAttribute("isLast", userPage.isLast());
        
        return "admin/user/list";
    }

    // 회원 상세 조회
    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable("id") Long id, Model model) {
        UserResponse.DetailDTO user = userService.findUser(id);
        model.addAttribute("user", user);
        return "admin/user/detail";
    }

    // 회원 권한 변경 처리
    @PostMapping("/users/{id}/update-role")
    public String updateRole(@PathVariable("id") Long id, @RequestParam("role") UserRole role) {
        userService.updateRole(id, role);
        return "redirect:/admin/users/" + id;
    }

    // 회원 상태 변경 처리
    @PostMapping("/users/{id}/update-status")
    public String updateStatus(@PathVariable("id") Long id, @RequestParam("status") UserStatus status) {
        userService.updateStatus(id, status);
        return "redirect:/admin/users/" + id;
    }
}
