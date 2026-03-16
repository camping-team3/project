package com.camping.erp.global._core.auth;

import com.camping.erp.domain.user.User;
import com.camping.erp.global._core.handler.ex.Exception403;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            // LoginInterceptor에서 이미 거르겠지만, 안전을 위해 추가
            throw new com.camping.erp.global._core.handler.ex.Exception401("인증이 필요합니다");
        }

        if (!"ADMIN".equals(sessionUser.getRole())) {
            throw new Exception403("관리자 권한이 없습니다");
        }

        return true;
    }
}
