package com.camping.erp.domain.user;

import com.camping.erp.domain.user.enums.UserRole;
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
        model.addAttribute("userPage", userPage);
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
}
