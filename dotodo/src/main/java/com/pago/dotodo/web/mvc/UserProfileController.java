package com.pago.dotodo.web.mvc;

import com.pago.dotodo.model.view.UserProfileView;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.UserService;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/profile")
public class UserProfileController extends BaseController {
    private static final String PAGE_NAME = "profile";
    private final ModelAndViewParser attributeBuilder;
    private final UserService userService;

    public UserProfileController(ModelAndViewParser attributeBuilder, UserService userService) {
        this.attributeBuilder = attributeBuilder;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getUserProfile(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        UserProfileView profileDetails = userService.getProfileDetails(userDetails.getId());
        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "profileDetails", profileDetails
        ));
    }
}
