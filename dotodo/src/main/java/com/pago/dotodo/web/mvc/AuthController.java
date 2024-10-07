package com.pago.dotodo.web.mvc;

import com.pago.dotodo.configuration.constraint.modelAttribute.AuthAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.CommonAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.HomeAttribute;
import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.model.elements.MenuItem;
import com.pago.dotodo.model.error.CustomErrorHandler;
import com.pago.dotodo.service.AuthService;
import com.pago.dotodo.service.LayoutService;
import com.pago.dotodo.util.ModelAndViewParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing authentication-related actions, such as login, registration, and logout.
 * Provides endpoints to display login and registration forms, handle user authentication,
 * and manage user sessions.
 */
@Controller
@RequestMapping("/auth")
@SessionAttributes({AuthAttribute.BAD_CREDENTIALS, CommonAttribute.USERNAME_FIELD})
public class AuthController extends BaseController {

    private final ModelAndViewParser attributeBuilder;
    private final AuthService authService;
    private final LayoutService layoutService;
    private final CustomErrorHandler errorHandler;

    /**
     * Constructs an AuthController with the required services and utilities.
     *
     * @param attributeParser Utility for building model attributes to pass to views.
     * @param authService     Service for handling user authentication and registration.
     * @param layoutService   Service for handling layout and menu items.
     * @param errorHandler    Custom error handler for validation errors.
     */
    @Autowired
    public AuthController(ModelAndViewParser attributeParser,
                          AuthService authService,
                          LayoutService layoutService,
                          CustomErrorHandler errorHandler) {
        this.attributeBuilder = attributeParser;
        this.authService = authService;
        this.layoutService = layoutService;
        this.errorHandler = errorHandler;
    }

    /**
     * Displays the login page with a login modal.
     * It also resets session attributes after login attempts, such as clearing "bad credentials" status.
     *
     * @param badCredentials Indicates whether the previous login attempt failed due to incorrect credentials.
     * @param username       The username used during the previous login attempt.
     * @param request        The current HTTP request used to set the background page.
     * @param sessionStatus  The session status used to reset session attributes.
     * @return A {@link ModelAndView} representing the login page.
     */
    @GetMapping("/login")
    public ModelAndView getLogin(@ModelAttribute(AuthAttribute.BAD_CREDENTIALS) Boolean badCredentials,
                                 @ModelAttribute(CommonAttribute.USERNAME_FIELD) String username,
                                 HttpServletRequest request,
                                 SessionStatus sessionStatus) {

        layoutService.setBackgroundPage(request);

        ModelAndView modelAndView = this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, layoutService.getBackgroundPage(),
                AuthAttribute.LOAD_MODAL, AuthAttribute.LOGIN)
        );

        sessionStatus.setComplete();

        return modelAndView;
    }

    /**
     * Displays the registration page.
     * It loads the registration modal and initializes the {@link UserRegisterDto} for user registration.
     *
     * @param userRegisterInfo The DTO used to capture user registration details.
     * @param request          The current HTTP request used to set the background page.
     * @return A {@link ModelAndView} representing the registration page with a preloaded registration form.
     */
    @GetMapping("/register")
    public ModelAndView getRegister(@ModelAttribute(AuthAttribute.USER_REGISTER_INFO) UserRegisterDto userRegisterInfo,
                                    HttpServletRequest request) {

        layoutService.setBackgroundPage(request);

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, layoutService.getBackgroundPage(),
                AuthAttribute.LOAD_MODAL, AuthAttribute.REGISTER,
                AuthAttribute.USER_REGISTER_INFO, userRegisterInfo)
        );
    }

    /**
     * Processes user registration requests.
     * It validates the registration form data, registers the user if validation is successful,
     * and logs them in by setting the security context. In case of validation errors, the form is redisplayed.
     *
     * @param userRegisterInfo The DTO containing the user's registration details.
     * @param bindingResult    The result of the form validation.
     * @param request          The current HTTP request used to initialize the security context.
     * @param response         The current HTTP response used to save the security context.
     * @return A {@link ModelAndView} that either displays validation errors or redirects to the home page on success.
     */
    @PostMapping("/register")
    public ModelAndView postRegister(@Valid @ModelAttribute UserRegisterDto userRegisterInfo,
                                     BindingResult bindingResult,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {

        Map<String, String> valueErrors = errorHandler.loadRegisterErrors(bindingResult, userRegisterInfo);

        Map<String, Object> attributes = attributeBuilder.build(
                CommonAttribute.PAGE_NAME, layoutService.getBackgroundPage(),
                AuthAttribute.LOAD_MODAL, AuthAttribute.REGISTER,
                CommonAttribute.VALUE_ERRORS, valueErrors,
                AuthAttribute.USER_REGISTER_INFO, userRegisterInfo);

        if (!valueErrors.isEmpty()) {
            return this.globalView(attributes);
        }

        try {
            this.authService.registerUser(userRegisterInfo,
                    successfulAuth -> authService.initializeSecurityContext(successfulAuth, request, response));
        } catch (RuntimeException e) {
            valueErrors.put(AuthAttribute.REGISTER, e.getMessage());
            return this.globalView(attributes);
        }

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, HomeAttribute.LOCAL_VIEW)
        );
    }

    /**
     * Handles user logout and redirects to the home page.
     *
     * @return A {@link ModelAndView} that redirects the user to the home page.
     */
    @GetMapping("/logout")
    public ModelAndView getLogout() {
        return this.redirect("/");
    }

    /**
     * Provides a new instance of the {@link UserRegisterDto} to be used for user registration.
     *
     * @return A new {@link UserRegisterDto} object.
     */
    @ModelAttribute(AuthAttribute.USER_REGISTER_INFO)
    public UserRegisterDto userRegisterInfo() {
        return new UserRegisterDto();
    }

    /**
     * Provides a list of tasks for the home page.
     * If the current page is the home page, it returns the tasks, otherwise returns an empty list.
     *
     * @return A {@link List} of {@link MenuItem} objects representing tasks for the home page.
     */
    @ModelAttribute(HomeAttribute.TASKS)
    public List<MenuItem> tasks() {
        return layoutService.getBackgroundPage()
                .equals(HomeAttribute.LOCAL_VIEW)
                ? layoutService.getHomeItems()
                : Collections.emptyList();
    }

    /**
     * Provides a default "bad credentials" attribute for login attempts.
     * Initially returns {@code false}, indicating no bad credentials.
     *
     * @return {@code false} by default.
     */
    @ModelAttribute(AuthAttribute.BAD_CREDENTIALS)
    public Boolean badCredentials() {
        return false;
    }

    /**
     * Provides an empty username field for login attempts.
     * Initially returns an empty string, representing no username input.
     *
     * @return An empty {@link String} by default.
     */
    @ModelAttribute(CommonAttribute.USERNAME_FIELD)
    public String username() {
        return "";
    }
}