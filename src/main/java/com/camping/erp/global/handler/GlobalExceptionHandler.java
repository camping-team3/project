package com.camping.erp.global.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public @ResponseBody Object handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
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
    public @ResponseBody ResponseEntity<String> ex400(Exception400 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(scriptAlert(e.getMessage(), "history.back();"), getHtmlHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception401.class)
    public @ResponseBody ResponseEntity<String> ex401(Exception401 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(scriptAlert(e.getMessage(), "location.href = '/login-form';"), getHtmlHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception403.class)
    public @ResponseBody ResponseEntity<String> ex403(Exception403 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(scriptAlert(e.getMessage(), "history.back();"), getHtmlHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception404.class)
    public @ResponseBody ResponseEntity<String> ex404(Exception404 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scriptAlert(e.getMessage(), "history.back();"), getHtmlHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception500.class)
    public @ResponseBody ResponseEntity<String> ex500(Exception500 e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(scriptAlert(e.getMessage(), "history.back();"), getHtmlHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 정적 리소스 미발견(404) 에러를 로그 없이 조용히 처리
    @ExceptionHandler(NoResourceFoundException.class)
    public @ResponseBody ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody ResponseEntity<String> exUnknown(Exception e, HttpServletRequest request) {
        e.printStackTrace();

        String msg = e.getMessage() != null ? e.getMessage() : "알 수 없는 에러가 발생했습니다.";
        
        if (isAjaxRequest(request)) {
            return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<>(scriptAlert(msg, "history.back();"), getHtmlHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpHeaders getHtmlHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");
        return headers;
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
