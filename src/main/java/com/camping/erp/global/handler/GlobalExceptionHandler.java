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
        System.out.println("[DEBUG] GlobalExceptionHandler - Exception401 caught: " + e.getMessage() + " for URI: " + request.getRequestURI());
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
        System.out.println("[DEBUG] GlobalExceptionHandler - Unknown Exception caught: " + e.getClass().getName() + " - " + e.getMessage() + " for URI: " + request.getRequestURI());
        
        // 404 에러(페이지 없음)인 경우 로그인 폼으로 리다이렉트하지 않고 404 메시지 출력
        if (e.getClass().getName().contains("NoResourceFoundException")) {
            return scriptAlert("요청하신 페이지를 찾을 수 없습니다: " + request.getRequestURI(), "history.back();");
        }

        e.printStackTrace();
        if (isAjaxRequest(request)) {
            return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, "관리자에게 문의하세요: " + e.getMessage());
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
