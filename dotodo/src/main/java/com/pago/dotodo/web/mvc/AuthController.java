package com.pago.dotodo.web.mvc;

import com.pago.dotodo.model.dto.UserAuthDto;
import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.service.AuthService;
import com.pago.dotodo.util.ModelAndViewParser;
import jakarta.validation.Valid;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@RestController
@RequestMapping("/auth")
@SessionAttributes({"bad_credentials", "username"})
public class AuthController extends BaseController {

    private static final String LOGIN_PAGE_NAME = "login";
    private static final String REGISTER_PAGE_NAME = "register";

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";

    private final ModelAndViewParser attributeBuilder;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(ModelAndViewParser attributeParser, AuthService authService,
                          AuthenticationManager authenticationManager) {
        this.attributeBuilder = attributeParser;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
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
    public ModelAndView postRegister(@Valid @ModelAttribute UserRegisterDto userRegisterInfo) {

        ArrayList<String> errors = new ArrayList<>();

        if (userRegisterInfo.getRawPassword().isEmpty() ||
                userRegisterInfo.getRePassword().isEmpty() ||
                userRegisterInfo.getUsername().isEmpty() ||
                userRegisterInfo.getEmail().isEmpty()) {
            errors.add("All fields are required");
        }

        if (!EmailValidator.getInstance().isValid(userRegisterInfo.getEmail())) {
            errors.add("Email address is invalid");
        }

        if (userRegisterInfo.getUsername().length() < 5) {
            errors.add("Username must be at least 5 characters long");
        }

        if (!userRegisterInfo.getRawPassword().matches(PASSWORD_PATTERN)) {
            errors.add("Password must be at least 6 characters long and should contain at least one number, uppercase and lowercase letter.");
        }

        if (!userRegisterInfo.getRawPassword().equals(userRegisterInfo.getRePassword())) {
            errors.add("Passwords do not match");
        }

        if (!errors.isEmpty()) {
            return this.view("index", attributeBuilder.build(
                    "pageName", REGISTER_PAGE_NAME,
                    "errors", String.join("\n", errors),
                    "username", userRegisterInfo.getUsername(),
                    "email", userRegisterInfo.getEmail())
            );
        }

        try {
            UserAuthDto registeredUser = this.authService.registerUser(userRegisterInfo);
            authenticateUserAndSetSession(registeredUser, userRegisterInfo.getRawPassword());
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            errors.add(e.getMessage());
            return this.view("index", attributeBuilder.build(
                    "pageName", REGISTER_PAGE_NAME,
                    "errors", String.join("\n", errors),
                    "username", userRegisterInfo.getUsername(),
                    "email", userRegisterInfo.getEmail())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this.view("index", attributeBuilder.build(
                "pageName", "home",
                "userRegisterInfo", userRegisterInfo)
        );
    }

    @GetMapping("/logout")
    public ModelAndView getLogout() {
        return this.redirect("/");
    }

    private void authenticateUserAndSetSession(UserAuthDto registeredUser, String rawPassword) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                registeredUser.getUsername(), rawPassword);

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
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
}
