package com.pago.dotodo.web;

import com.pago.dotodo.util.ModelAndViewParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private static final String LOGIN_PAGE_NAME = "login";
    private static final String REGISTER_PAGE_NAME = "register";

    private final ModelAndViewParser attributeBuilder;

    public AuthController(ModelAndViewParser attributeParser) {
        this.attributeBuilder = attributeParser;
    }

    @GetMapping("/login")
    public ModelAndView getLogin(HttpServletRequest request) {
        boolean badCredentials = Boolean.TRUE.equals(request.getSession().getAttribute("bad_credentials"));
        request.getSession().removeAttribute("bad_credentials");

        return this.view("index", attributeBuilder.build(
                "pageName", LOGIN_PAGE_NAME,
                "bad_credentials", badCredentials)
        );
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return this.view("index", attributeBuilder.build(
                "pageName", REGISTER_PAGE_NAME)
        );
    }

    @GetMapping("/logout")
    public ModelAndView getLogout() {
        return this.redirect("/");
    }
}
