package com.camping.erp.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 모든 컨트롤러 뷰 렌더링 시 공통 모델 데이터를 주입하는 어드바이스
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * 모든 페이지에서 CSRF 토큰을 {{_csrf.token}} 형태로 사용할 수 있게 주입
     */
    @ModelAttribute("_csrf")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
}
