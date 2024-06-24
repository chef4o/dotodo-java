package com.pago.dotodo.web.mvc;

import com.pago.dotodo.model.dto.UserDto;
import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.service.AuthService;
import com.pago.dotodo.util.ModelAndViewParser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/auth")
@SessionAttributes({"bad_credentials", "username"})
public class AuthController extends BaseController {

    private static final String LOGIN_PAGE_NAME = "login";
    private static final String REGISTER_PAGE_NAME = "register";

    private final ModelAndViewParser attributeBuilder;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(ModelAndViewParser attributeParser, AuthService authService, AuthenticationManager authenticationManager) {
        this.attributeBuilder = attributeParser;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @ModelAttribute("userRegisterInfo")
    public UserRegisterDto userRegisterInfo() {
        return new UserRegisterDto();
    }

    @ModelAttribute("bad_credentials")
    public Boolean badCredentials() {
        return false;
    }

    @ModelAttribute("username")
    public String username() {
        return "";
    }

    @ModelAttribute("email")
    public String email() {
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
    public ModelAndView getRegister(@ModelAttribute("userRegisterInfo")
                                    UserRegisterDto userRegisterInfo) {

        return this.view("index", attributeBuilder.build(
                "pageName", REGISTER_PAGE_NAME,
                "userRegisterInfo", userRegisterInfo)
        );
    }

    @PostMapping("/register")
    public ModelAndView postRegister(HttpSession session,
                                     @Valid @ModelAttribute UserRegisterDto userRegisterInfo,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userRegisterInfo", userRegisterInfo)
                    .addFlashAttribute("org.springframework.validation.BindingResult." + "userRegisterInfo",
                            bindingResult);

            return super.redirect("/auth/register");
        }

        UserDto registeredUser = this.authService.registerUser(userRegisterInfo);
        authenticateUserAndSetSession(registeredUser, userRegisterInfo.getPassword());


        return this.view("index", attributeBuilder.build(
                "pageName", "home",
                "userRegisterInfo", userRegisterInfo)
        );
    }

    @GetMapping("/logout")
    public ModelAndView getLogout() {
        return this.redirect("/");
    }

    private void authenticateUserAndSetSession(UserDto registeredUser, String rawPassword) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                registeredUser.getUsername(), rawPassword);

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }

    }
}
