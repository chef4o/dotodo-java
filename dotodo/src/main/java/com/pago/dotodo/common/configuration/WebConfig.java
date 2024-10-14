package com.pago.dotodo.common.configuration;

import com.pago.dotodo.common.security.CustomSecurityConfig;
import com.pago.dotodo.common.util.interceptor.AdminAccessInterceptor;
import com.pago.dotodo.common.util.interceptor.TitleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TitleInterceptor titleInterceptor;
    private final AdminAccessInterceptor adminAccessInterceptor;
    private final CustomSecurityConfig securityConfig;

    public WebConfig(TitleInterceptor titleInterceptor, AdminAccessInterceptor adminAccessInterceptor, CustomSecurityConfig securityConfig) {
        this.titleInterceptor = titleInterceptor;
        this.adminAccessInterceptor = adminAccessInterceptor;
        this.securityConfig = securityConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(titleInterceptor)
                .addPathPatterns("/notes", "/notes/**", "/profile", "/profile/**");
        registry.addInterceptor(adminAccessInterceptor)
                .addPathPatterns(securityConfig.getAdminAccessPages());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPatternParser(new PathPatternParser());
    }

}
