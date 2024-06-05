package com.pago.dotodo.security;

import com.pago.dotodo.model.enums.RoleEnum;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomSecurityConfig {
    private static final String[] allowedPages = {
            "/",
            "/home",
            "/contacts",
            "/news",
            "/about-us",
            "/auth/login",
            "/auth/register",
            "/auth/logout"
    };

    private static final String[] administrationRoles = {
            RoleEnum.SUPER_ADMIN.name(),
            RoleEnum.ADMIN.name(),
            RoleEnum.MODERATOR.name()
    };

    public static String[] getAdministrationRoles() {
        return administrationRoles;
    }

    public static RequestMatcher getAllowedPages() {
        List<RequestMatcher> matchers = Arrays.stream(allowedPages)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        return new OrRequestMatcher(matchers);
    }
}
