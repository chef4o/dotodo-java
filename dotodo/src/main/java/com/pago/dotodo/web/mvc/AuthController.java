package com.pago.dotodo.web.mvc;

import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.service.AuthService;
import com.pago.dotodo.service.LayoutService;
import com.pago.dotodo.util.ModelAndViewParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@SessionAttributes({"bad_credentials", "username"})
public class AuthController extends BaseController {

    private static final String LOAD_MODAL = "loadModal";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";

    private final ModelAndViewParser attributeBuilder;
    private final AuthService authService;
    private final LayoutService layoutService;
    private final SecurityContextRepository securityContextRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(ModelAndViewParser attributeParser, AuthService authService,
                          LayoutService layoutService, SecurityContextRepository securityContextRepository) {
        this.attributeBuilder = attributeParser;
        this.authService = authService;
        this.layoutService = layoutService;
        this.securityContextRepository = securityContextRepository;
    }

    @GetMapping("/login")
    public ModelAndView getLogin(@ModelAttribute("bad_credentials") Boolean badCredentials,
                                 @ModelAttribute("username") String username,
                                 HttpServletRequest request,
                                 SessionStatus sessionStatus) {

        layoutService.setBackgroundPage(request);

        ModelAndView modelAndView = this.view("index", attributeBuilder.build(
                "pageName", layoutService.getBackgroundPage(),
                LOAD_MODAL, "login",
                "tasks", layoutService.getBackgroundPage()
                        .equals("home")
                        ? layoutService.getHomeItems()
                        : null,
                "bad_credentials", badCredentials,
                "username", username)
        );

        sessionStatus.setComplete();

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegister(@ModelAttribute("userRegisterInfo") UserRegisterDto userRegisterInfo,
                                    HttpServletRequest request) {

        layoutService.setBackgroundPage(request);

        return this.view("index", attributeBuilder.build(
                "pageName", layoutService.getBackgroundPage(),
                "tasks", layoutService.getBackgroundPage()
                        .equals("home")
                        ? layoutService.getHomeItems()
                        : null,
                LOAD_MODAL, "register",
                "userRegisterInfo", userRegisterInfo)
        );
    }

    @PostMapping("/register")
    public ModelAndView postRegister(@Valid @ModelAttribute UserRegisterDto userRegisterInfo,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {

        HashMap<String, String> errors = loadErrors(userRegisterInfo);

        if (!errors.isEmpty()) {
            return this.view("index", attributeBuilder.build(
                    "pageName", layoutService.getBackgroundPage(),
                    "tasks", layoutService.getBackgroundPage()
                            .equals("home")
                            ? layoutService.getHomeItems()
                            : null,
                    LOAD_MODAL, "register",
                    "errors", errors,
                    "userRegisterInfo", userRegisterInfo)
            );
        }

        try {
            this.authService.registerUser(userRegisterInfo, successfulAuth -> {
                SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
                SecurityContext context = strategy.createEmptyContext();
                context.setAuthentication(successfulAuth);
                strategy.setContext(context);
                securityContextRepository.saveContext(context, request, response);
            });
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            errors.put("register", e.getMessage());

            return this.view("index", attributeBuilder.build(
                    "pageName", layoutService.getBackgroundPage(),
                    "tasks", layoutService.getBackgroundPage()
                            .equals("home")
                            ? layoutService.getHomeItems()
                            : null,
                    LOAD_MODAL, "register",
                    "errors", errors,
                    "userRegisterInfo", userRegisterInfo)
            );
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

    private HashMap<String, String> loadErrors(UserRegisterDto userRegisterInfo) {
        HashMap<String, String> errors = new HashMap<>();

        if (userRegisterInfo.getEmail().isEmpty()) {
            errors.put("email", "Email is required");
        } else if (!EmailValidator.getInstance().isValid(userRegisterInfo.getEmail())) {
            errors.put("email", "Email address is invalid");
        }

        if (userRegisterInfo.getUsername().isEmpty()) {
            errors.put("username", "Username is required");
        } else if (userRegisterInfo.getUsername().length() < 5) {
            errors.put("username", "Username must be at least 5 characters long");
        }

        if (userRegisterInfo.getRawPassword().isEmpty()) {
            errors.put("password", "Password is required");
        } else if (!userRegisterInfo.getRawPassword().matches(PASSWORD_PATTERN)) {
            errors.put("password", "Password must be more secure");
            errors.put("passwordInfo", "Password must be at least 6 characters long " +
                    "and should contain at least one number, uppercase and lowercase letter.");
        }

        if (userRegisterInfo.getRePassword().isEmpty()) {
            errors.put("rePassword", "Field is required");
        } else if (!userRegisterInfo.getRawPassword().equals(userRegisterInfo.getRePassword())) {
            errors.put("rePassword", "Passwords do not match");
        }

        return errors;
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
}
