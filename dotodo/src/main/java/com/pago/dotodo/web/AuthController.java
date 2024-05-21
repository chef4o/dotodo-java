package com.pago.dotodo.web;

import com.pago.dotodo.model.dto.binding.UserTokenDto;
import com.pago.dotodo.service.SkeletonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private static final String LOGIN_PAGE_NAME = "login";
    private static final String REGISTER_PAGE_NAME = "register";
    private final UserTokenDto loggedUser;

    private final SkeletonService skeletonService;

    public AuthController(UserTokenDto loggedUser, SkeletonService skeletonService) {
        this.loggedUser = loggedUser;
        this.skeletonService = skeletonService;
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        return this.view("index",
                "pageName", LOGIN_PAGE_NAME,
                "tasks", skeletonService.getHomeItems(),
                "topbarNavItems", skeletonService.getTopbarNavItems(),
                "sidebarNavItems", skeletonService.getSidebarNavItems(),
                "bottombarNavItems", skeletonService.getBottombarNavItems(),
                "connectNavItems", skeletonService.getConnectNavItems()
        );
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return this.view("index",
                "pageName", REGISTER_PAGE_NAME,
                "tasks", skeletonService.getHomeItems(),
                "topbarNavItems", skeletonService.getTopbarNavItems(),
                "sidebarNavItems", skeletonService.getSidebarNavItems(),
                "bottombarNavItems", skeletonService.getBottombarNavItems(),
                "connectNavItems", skeletonService.getConnectNavItems()
        );
    }
}
