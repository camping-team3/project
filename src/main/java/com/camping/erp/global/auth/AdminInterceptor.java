package com.camping.erp.global.auth;

import org.springframework.web.servlet.HandlerInterceptor;

import com.camping.erp.domain.user.UserResponse;
import com.camping.erp.domain.user.enums.UserRole;
import com.camping.erp.global.handler.ex.Exception401;
import com.camping.erp.global.handler.ex.Exception403;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        String uri = request.getRequestURI();
        HttpSession session = request.getSession();
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");

        System.out.println("[DEBUG] AdminInterceptor - URI: " + uri);

        if (sessionUser == null) {
            System.out.println("[DEBUG] AdminInterceptor - Session NULL, Blocking URI: " + uri);
            throw new Exception401("인증이 필요합니다");
        }

        if (sessionUser.getRole() != UserRole.ADMIN) {
            System.out.println("[DEBUG] AdminInterceptor - Not ADMIN, Blocking URI: " + uri);
            throw new Exception403("관리자 권한이 없습니다");
        }

        return true;
    }
}
