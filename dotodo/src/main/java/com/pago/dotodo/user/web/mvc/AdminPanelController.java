package com.pago.dotodo.user.web.mvc;

import com.pago.dotodo.common.error.CustomErrorHandler;
import com.pago.dotodo.common.security.CustomAuthUserDetails;
import com.pago.dotodo.common.util.ModelAndViewParser;
import com.pago.dotodo.common.web.BaseController;
import com.pago.dotodo.common.web.constraint.CommonAttribute;
import com.pago.dotodo.user.model.dto.AdminPanelUserDto;
import com.pago.dotodo.user.service.AdminPanelService;
import com.pago.dotodo.user.service.UserService;
import com.pago.dotodo.user.web.mvc.constraint.AdminPanelAttribute;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/admin-panel")
public class AdminPanelController extends BaseController {
    private final ModelAndViewParser attributeBuilder;
    private final UserService userService;
    private final AdminPanelService adminPanelService;
    private final CustomErrorHandler errorHandler;
    private final ModelMapper modelMapper;

    public AdminPanelController(ModelAndViewParser attributeBuilder,
                                UserService userService,
                                AdminPanelService adminPanelService,
                                CustomErrorHandler errorHandler,
                                ModelMapper modelMapper) {
        this.attributeBuilder = attributeBuilder;
        this.userService = userService;
        this.adminPanelService = adminPanelService;
        this.errorHandler = errorHandler;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ModelAndView getAdminPanel(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, AdminPanelAttribute.LOCAL_VIEW,
                AdminPanelAttribute.LOWER_LEVEL_USERS, userService.getLowerLevelUsers(userDetails.getId())
        ));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditAdminPanelUser(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                              @PathVariable Long id) {

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, AdminPanelAttribute.LOCAL_VIEW,
                AdminPanelAttribute.USER_TO_EDIT, userService.getUserById(id),
                AdminPanelAttribute.POSSIBLE_ROLES, adminPanelService.getPossibleRoles(userDetails.getId()),
                AdminPanelAttribute.LOWER_LEVEL_USERS, userService.getLowerLevelUsers(userDetails.getId())
        ));
    }

    @PatchMapping("/edit/{id}")
    public ModelAndView updateAdminPanelUser(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                             @PathVariable Long id,
                                             @Valid @ModelAttribute AdminPanelUserDto userToEdit,
                                             BindingResult bindingResult) {

        AdminPanelUserDto targetUser = modelMapper.map(userService.getUserById(id), AdminPanelUserDto.class);

        Map<String, String> valueErrors = errorHandler
                .loadAdminUserEditErrors(bindingResult, userToEdit, targetUser);

        if (!valueErrors.isEmpty()) {
            return this.globalView(attributeBuilder.build(
                    CommonAttribute.PAGE_NAME, AdminPanelAttribute.LOCAL_VIEW,
                    CommonAttribute.VALUE_ERRORS, valueErrors,
                    AdminPanelAttribute.USER_TO_EDIT, userToEdit,
                    AdminPanelAttribute.POSSIBLE_ROLES, adminPanelService.getPossibleRoles(userDetails.getId()),
                    AdminPanelAttribute.LOWER_LEVEL_USERS, userService.getLowerLevelUsers(userDetails.getId())
            ));
        }

        adminPanelService.editUser(userToEdit, id);
        return super.redirect("/" + AdminPanelAttribute.LOCAL_VIEW);
    }

    @ModelAttribute
    public AdminPanelUserDto userToEdit() {
        return new AdminPanelUserDto();
    }
}
