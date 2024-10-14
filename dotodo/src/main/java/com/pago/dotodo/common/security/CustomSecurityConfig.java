package com.pago.dotodo.common.security;

import com.pago.dotodo.user.model.enums.RoleEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CustomSecurityConfig {
    private static final String[] allowedPages = {
            "/",
            "/home",
            "/contacts",
            "/contacts/send",
            "/news",
            "/news/view/*",
            "/about",
            "/auth/login",
            "/auth/register",
            "/auth/logout"
    };

    private static final String[] adminOnlyPages = {
            "/news/new",
            "/news/edit",
            "/news/edit/**",
            "/news/delete",
            "/news/delete/**",
            "/admin-panel",
            "/admin-panel/**",
    };

    private static final String[] administrationRoles = {
            RoleEnum.SUPER_ADMIN.name(),
            RoleEnum.ADMIN.name(),
            RoleEnum.MODERATOR.name()
    };

    public String[] getAdministrationRoles() {
        return administrationRoles;
    }

    public String[] getAdminAccessPages() {
        return adminOnlyPages;
    }

    public static RequestMatcher getAllowedPages() {
        List<RequestMatcher> matchers = Arrays.stream(allowedPages)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        return new OrRequestMatcher(matchers);
    }
}
