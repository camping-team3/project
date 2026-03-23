package com.camping.erp.global.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.handler.ex.Exception401;
import com.camping.erp.global.handler.ex.Exception403;
import com.camping.erp.global.handler.ex.Exception404;
import com.camping.erp.global.handler.ex.Exception500;
import com.camping.erp.global.util.Resp;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        String acceptHeader = request.getHeader("Accept");
        return "XMLHttpRequest".equals(header) || (acceptHeader != null && acceptHeader.contains("application/json"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Object handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        String message = "업로드 가능한 파일 크기를 초과했습니다.";
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.PAYLOAD_TOO_LARGE, message);
        }
        return String.format("""
                <script>
                    alert('%s');
                    history.back();
                </script>
                """, message);
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

    // 정적 리소스 미발견(404) 에러를 로그 없이 조용히 처리
    @ExceptionHandler(NoResourceFoundException.class)
    public @ResponseBody Object handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        return Resp.fail(HttpStatus.NOT_FOUND, "Not Found");
    }

    @ExceptionHandler(Exception.class)
    public Object exUnknown(Exception e, HttpServletRequest request) {
        String msg = e.getMessage() != null ? e.getMessage() : "";
        String uri = request.getRequestURI() != null ? request.getRequestURI() : "";
        
        if (msg.contains("No static resource") || uri.contains(".well-known") || msg.contains("rejected")) {
            return Resp.fail(HttpStatus.NOT_FOUND, "Not Found");
        }

        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error");
        }
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
