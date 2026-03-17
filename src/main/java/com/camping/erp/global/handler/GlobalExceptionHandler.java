package com.camping.erp.global.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.handler.ex.Exception401;
import com.camping.erp.global.handler.ex.Exception403;
import com.camping.erp.global.handler.ex.Exception404;
import com.camping.erp.global.handler.ex.Exception500;
import com.camping.erp.global.util.Resp;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        String acceptHeader = request.getHeader("Accept");
        return "XMLHttpRequest".equals(header) || (acceptHeader != null && acceptHeader.contains("application/json"));
    }

    @ExceptionHandler(Exception400.class)
    public @ResponseBody Object ex400(Exception400 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return scriptAlert(e.getMessage(), "history.back();");
    }

    @ExceptionHandler(Exception401.class)
    public @ResponseBody Object ex401(Exception401 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        return scriptAlert(e.getMessage(), "location.href = '/login-form';");
    }

    @ExceptionHandler(Exception403.class)
    public @ResponseBody Object ex403(Exception403 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return scriptAlert(e.getMessage(), "history.back();");
    }

    @ExceptionHandler(Exception404.class)
    public @ResponseBody Object ex404(Exception404 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return scriptAlert(e.getMessage(), "history.back();");
    }

    @ExceptionHandler(Exception500.class)
    public @ResponseBody Object ex500(Exception500 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return scriptAlert(e.getMessage(), "history.back();");
    }

    @ExceptionHandler(Exception.class)
    public Object exUnknown(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, "관리자에게 문의하세요");
        }
        // 리다이렉트이므로 @ResponseBody를 붙이지 않음
        return "redirect:/login-form";
    }

    private String scriptAlert(String msg, String action) {
        return String.format("""
                <script>
                    alert('%s');
                    %s
                </script>
                """, msg, action);
    }
}
