package com.pago.dotodo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        logger.error("Authentication failed: {}", exception.getMessage());
        request.getSession().setAttribute("bad_credentials", true);
        request.getSession().setAttribute("username",
                request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.sendRedirect("/auth/login?error=true");

        if (!isTestEnvironment()) {
            response.sendRedirect("/auth/login?error=true");
        }
    }

    private boolean isTestEnvironment() {
        String activeProfile = System.getProperty("spring.profiles.active");
        return activeProfile != null && activeProfile.contains("test");
    }
}
