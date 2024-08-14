package com.pago.dotodo.web.mvc;

import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.model.elements.MenuItem;
import com.pago.dotodo.service.AuthService;
import com.pago.dotodo.service.LayoutService;
import com.pago.dotodo.util.ModelAndViewParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/auth")
@SessionAttributes({"bad_credentials", "username"})
public class AuthController extends BaseController {

    private static final String LOAD_MODAL = "loadModal";

    private final ModelAndViewParser attributeBuilder;
    private final AuthService authService;
    private final LayoutService layoutService;
    private final SecurityContextRepository securityContextRepository;

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
                LOAD_MODAL, "login")
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
                LOAD_MODAL, "register",
                "userRegisterInfo", userRegisterInfo)
        );
    }

    @PostMapping("/register")
    public ModelAndView postRegister(@Valid @ModelAttribute UserRegisterDto userRegisterInfo,
                                     BindingResult bindingResult,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {


        HashMap<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
        } else {
            loadCustomErrors(errors, userRegisterInfo);
        }

        if (!errors.isEmpty()) {
            return this.view("index", attributeBuilder.build(
                    "pageName", layoutService.getBackgroundPage(),
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
            errors.put("register", e.getMessage());
            return this.view("index", attributeBuilder.build(
                    "pageName", layoutService.getBackgroundPage(),
                    LOAD_MODAL, "register",
                    "errors", errors,
                    "userRegisterInfo", userRegisterInfo)
            );
        }

        return this.view("index", attributeBuilder.build(
                "pageName", "home")
        );
    }

    @GetMapping("/logout")
    public ModelAndView getLogout() {
        return this.redirect("/");
    }

    @ModelAttribute("userRegisterInfo")
    public UserRegisterDto userRegisterInfo() {
        return new UserRegisterDto();
    }

    @ModelAttribute("tasks")
    public List<MenuItem> tasks() {
        return layoutService.getBackgroundPage()
                .equals("home")
                ? layoutService.getHomeItems()
                : null;
    }

    @ModelAttribute("bad_credentials")
    public Boolean badCredentials() {
        return false;
    }

    @ModelAttribute("username")
    public String username() {
        return "";
    }

    private void loadCustomErrors(HashMap<String, String> errors, UserRegisterDto userRegisterInfo) {

        if (!userRegisterInfo.getEmail().isBlank()
                && !EmailValidator.getInstance().isValid(userRegisterInfo.getEmail())) {
            errors.put("email", "Email should be valid");
        }
    }
}
