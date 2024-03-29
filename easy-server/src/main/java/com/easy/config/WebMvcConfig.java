package com.easy.config;

import com.easy.interceptor.UserLoginTokenInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.easy.interceptor.AdminLoginTokenInterceptor;
import com.easy.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration class
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AdminLoginTokenInterceptor adminLoginTokenInterceptor;

    @Autowired
    private UserLoginTokenInterceptor userLoginTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Add interceptor for admin paths, excluding the login path
        registry.addInterceptor(adminLoginTokenInterceptor).addPathPatterns("/admin/**").excludePathPatterns(List.of("/admin/employee/login", "/admin/employee/logout"));
        // Add interceptor for user paths, excluding the login path and status path
        registry.addInterceptor(userLoginTokenInterceptor).addPathPatterns("/user/**").excludePathPatterns("/user/user/login", "/user/shop/status");
    }

    /**
     * Extend message converters.
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("Extending Spring Framework's message converters. Use custom converter for JSON data.");
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // Add to the first position to give it the highest priority, overriding the default
        converters.add(0, messageConverter);
    }
}
