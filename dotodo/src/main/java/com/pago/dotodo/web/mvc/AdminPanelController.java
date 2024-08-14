package com.pago.dotodo.web.mvc;

import com.pago.dotodo.security.CustomSecurityConfig;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
@RequestMapping("/admin-panel")
public class AdminPanelController extends BaseController {
    private static final String PAGE_NAME = "admin-panel";
    private final ModelAndViewParser attributeBuilder;
    private static final String[] administrationRoles = CustomSecurityConfig.getAdministrationRoles();

    public AdminPanelController(ModelAndViewParser attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ModelAndView getAdminPanel() {
        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "hasAccessPermission", hasAdminPanelAccess(),
                "lowerLevelUsers", null //TODO: get actual users with lower level than current user
        ));
    }

    private boolean hasAdminPanelAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return Arrays.stream(administrationRoles)
                .anyMatch(role -> authentication.getAuthorities().contains(new SimpleGrantedAuthority(role)));
    }
}
