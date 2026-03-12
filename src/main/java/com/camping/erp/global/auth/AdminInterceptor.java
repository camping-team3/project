package com.camping.erp.global.auth;

import org.springframework.web.servlet.HandlerInterceptor;

import com.camping.erp.domain.user.User;
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
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            throw new Exception401("인증이 필요합니다");
        }

        if (sessionUser.getRole() != UserRole.ADMIN) {
            throw new Exception403("관리자 권한이 없습니다");
        }

        return true;
    }
}
