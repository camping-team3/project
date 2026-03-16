package com.camping.erp.global.config;

import com.camping.erp.global._core.auth.AdminInterceptor;
import com.camping.erp.global._core.auth.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 모든 인증이 필요한 경로
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/mypage/**", "/reservation/**", "/admin/**", "/boards/**", "/replies/**")
                .excludePathPatterns("/boards/[0-9]+"); // 상세 조회는 비로그인 허용 (필요시)

        // 관리자 전용 경로
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**");
    }
}
