package com.pago.dotodo.web.mvc;

import com.pago.dotodo.configuration.constraint.modelAttribute.AdminPanelAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.CommonAttribute;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.security.CustomSecurityConfig;
import com.pago.dotodo.service.UserService;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin-panel")
public class AdminPanelController extends BaseController {
    private final ModelAndViewParser attributeBuilder;
    private final CustomSecurityConfig securityConfig;
    private final UserService userService;

    public AdminPanelController(ModelAndViewParser attributeBuilder,
                                CustomSecurityConfig securityConfig,
                                UserService userService) {
        this.attributeBuilder = attributeBuilder;
        this.securityConfig = securityConfig;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getAdminPanel(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, AdminPanelAttribute.LOCAL_VIEW,
                AdminPanelAttribute.LOWER_LEVEL_USERS, userService.getLowerLevelUsers(userDetails.getId()),
                AdminPanelAttribute.ADMIN_ROLES, String.join(",", securityConfig.getAdministrationRoles())
        ));
    }

}
