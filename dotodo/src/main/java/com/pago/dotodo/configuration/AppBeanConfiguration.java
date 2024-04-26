package com.pago.dotodo.configuration;

import com.pago.dotodo.model.dto.binding.UserTokenDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class AppBeanConfiguration {
    @Bean
    @SessionScope
    public UserTokenDto loggedUser() {
        return new UserTokenDto();
    }
}
