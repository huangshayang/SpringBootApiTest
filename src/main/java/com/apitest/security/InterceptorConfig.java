package com.apitest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Arrays;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截请求，通过判断是否有@Auth注解 决定是否需要登录
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**").excludePathPatterns(Arrays.asList("/", "/account/**"));
    }

    @Bean
    public HandlerInterceptorAdapter authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}
