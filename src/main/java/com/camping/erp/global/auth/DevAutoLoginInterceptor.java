package com.camping.erp.global.auth;

import com.camping.erp.domain.user.User;
import com.camping.erp.domain.user.UserRepository;
import com.camping.erp.global.handler.ex.Exception500;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class DevAutoLoginInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    private final String username;

    public DevAutoLoginInterceptor(UserRepository userRepository, String username) {
        this.userRepository = userRepository;
        this.username = username;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();

        if (session.getAttribute("sessionUser") == null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception500("Dev auto-login 실패: '" + username + "' 유저가 없습니다. data.sql을 확인하세요."));
            session.setAttribute("sessionUser", user);
        }

        return true;
    }
}
