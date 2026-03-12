package com.camping.erp.global.handler;

import com.camping.erp.global.handler.ex.*;
import com.camping.erp.global.util.Resp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        String acceptHeader = request.getHeader("Accept");
        return "XMLHttpRequest".equals(header) || (acceptHeader != null && acceptHeader.contains("application/json"));
    }

    @ExceptionHandler(Exception400.class)
    public Object ex400(Exception400 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return String.format("""
                <script>
                    alert('%s');
                    history.back();
                </script>
                """, e.getMessage());
    }

    @ExceptionHandler(Exception401.class)
    public Object ex401(Exception401 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        return String.format("""
                <script>
                    alert('%s');
                    location.href = '/login-form';
                </script>
                """, e.getMessage());
    }

    @ExceptionHandler(Exception403.class)
    public Object ex403(Exception403 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return String.format("""
                <script>
                    alert('%s');
                    history.back();
                </script>
                """, e.getMessage());
    }

    @ExceptionHandler(Exception404.class)
    public Object ex404(Exception404 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return String.format("""
                <script>
                    alert('%s');
                    history.back();
                </script>
                """, e.getMessage());
    }

    @ExceptionHandler(Exception500.class)
    public Object ex500(Exception500 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return String.format("""
                <script>
                    alert('%s');
                    history.back();
                </script>
                """, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Object exUnknown(Exception e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, "관리자에게 문의하세요");
        }
        return String.format("""
                <script>
                    alert('%s');
                    history.back();
                </script>
                """, "관리자에게 문의하세요");
    }
}
