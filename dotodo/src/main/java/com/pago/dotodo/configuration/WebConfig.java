package com.pago.dotodo.configuration;

import com.pago.dotodo.util.interceptor.TitleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TitleInterceptor titleInterceptor;

    public WebConfig(TitleInterceptor titleInterceptor) {
        this.titleInterceptor = titleInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(titleInterceptor);
    }

}
