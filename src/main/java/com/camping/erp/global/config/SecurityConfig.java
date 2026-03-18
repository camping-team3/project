package com.camping.erp.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 비활성화 (H2 콘솔 및 단순 폼 테스트용)
        http.csrf(AbstractHttpConfigurer::disable);

        // H2 콘솔 사용을 위한 FrameOptions 허용
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        // 모든 요청 허용 (인증은 Interceptor에서 처리)
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        // 기본 로그인 폼 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
