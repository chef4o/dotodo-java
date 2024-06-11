package com.pago.dotodo.web;

import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
@SessionAttributes({"bad_credentials", "username"})
public class AuthController extends BaseController {

    private static final String LOGIN_PAGE_NAME = "login";
    private static final String REGISTER_PAGE_NAME = "register";

    private final ModelAndViewParser attributeBuilder;

    public AuthController(ModelAndViewParser attributeParser) {
        this.attributeBuilder = attributeParser;
    }

    @ModelAttribute("bad_credentials")
    public Boolean badCredentials() {
        return false;
    }

    @ModelAttribute("username")
    public String username() {
        return "";
    }

    @GetMapping("/login")
    public ModelAndView getLogin(@ModelAttribute("bad_credentials") Boolean badCredentials,
                                 @ModelAttribute("username") String username,
                                 SessionStatus sessionStatus) {

        ModelAndView modelAndView = this.view("index", attributeBuilder.build(
                "pageName", LOGIN_PAGE_NAME,
                "bad_credentials", badCredentials,
                "username", username)
        );

        sessionStatus.setComplete();

        return modelAndView;
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
