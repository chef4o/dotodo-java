package com.pago.dotodo.common.error;

import com.pago.dotodo.auth.model.dto.UserRegisterDto;
import com.pago.dotodo.common.error.constraints.FormErrors;
import com.pago.dotodo.common.model.Date;
import com.pago.dotodo.common.util.DateTimeUtil;
import com.pago.dotodo.common.web.constraint.CommonAttribute;
import com.pago.dotodo.main.model.dto.ContactFormDto;
import com.pago.dotodo.main.web.mvc.constraint.ContactAttribute;
import com.pago.dotodo.news.model.dto.ArticleDto;
import com.pago.dotodo.user.model.dto.AdminPanelUserDto;
import com.pago.dotodo.user.model.dto.EditUserProfile;
import com.pago.dotodo.user.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomErrorHandler {

    private final UserService userService;
    private final DateTimeUtil dateTimeUtil;

    public CustomErrorHandler(UserService userService, DateTimeUtil dateTimeUtil) {
        this.userService = userService;
        this.dateTimeUtil = dateTimeUtil;
    }

    public Map<String, String> loadNewsErrors(BindingResult bindingResult,
                                              ArticleDto articleDto) {

        Map<String, String> valueErrors = new HashMap<>();

        if (articleDto.getHeader().isBlank() || articleDto.getContent().isBlank()) {
            valueErrors.put(CommonAttribute.ALL_FIELDS, FormErrors.MISSING_HEADER_OR_CONTENT);
        } else {
            valueErrors = loadBindingErrors(bindingResult);
        }

        return valueErrors;
    }

    public Map<String, String> loadContactErrors(BindingResult bindingResult,
                                                 ContactFormDto contactFormDto) {

        Map<String, String> valueErrors = loadBindingErrors(bindingResult);

        if (contactFormDto.getEmail().isBlank() && contactFormDto.getPhone().isBlank()) {
            valueErrors.put(ContactAttribute.NO_CONTACT_INFO, FormErrors.MISSING_PHONE_OR_EMAIL);
        }

        return valueErrors;
    }

    public Map<String, String> loadRegisterErrors(BindingResult bindingResult,
                                                  UserRegisterDto userRegisterInfo) {

        Map<String, String> valueErrors = loadBindingErrors(bindingResult);

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                valueErrors.put(error.getField(), error.getDefaultMessage());
            });
        } else {
            if (!userRegisterInfo.getEmail().isBlank()
                    && !EmailValidator.getInstance().isValid(userRegisterInfo.getEmail())) {
                valueErrors.put(CommonAttribute.EMAIL_FIELD, FormErrors.INVALID_EMAIL);
            }
        }

        return valueErrors;
    }

    public Map<String, String> loadNoteErrors(BindingResult bindingResult,
                                              Date noteDate) {
        Map<String, String> valueErrors = loadBindingErrors(bindingResult);

        if (valueErrors.get(CommonAttribute.DATE_FIELD) == null) {
            if (noteDate.getDueDate() == null || noteDate.getDueDate().isBlank()
                    && (noteDate.getDueTime() != null & !noteDate.getDueTime().isBlank())) {

                valueErrors.put(CommonAttribute.DATE_FIELD, FormErrors.TIME_WITHOUT_DATE);
            } else if (noteDate.getDueDate() != null && !noteDate.getDueDate().isBlank()
                    && !dateTimeUtil.isInFuture(noteDate.getDueDate(), noteDate.getDueTime())) {

                valueErrors.put(CommonAttribute.DATE_FIELD, FormErrors.PAST_DUE_DATE);
            }
        }

        return valueErrors;
    }

    public Map<String, String> loadUserProfileErrors(BindingResult bindingResult,
                                                     EditUserProfile profileEditDetails, Long userId) {

        Map<String, String> valueErrors = loadBindingErrors(bindingResult);
        checkForExistingAccount(CommonAttribute.EMAIL_FIELD, valueErrors,
                profileEditDetails.getEmail(), userId);
        checkForExistingAccount(CommonAttribute.USERNAME_FIELD, valueErrors,
                profileEditDetails.getUsername(), userId);

        return valueErrors;
    }

    public static Map<String, String> loadBindingErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "",
                        (existingValue, newValue) -> existingValue + "; " + newValue
                ));
    }

    private void checkForExistingAccount(String field,
                                         Map<String, String> valueErrors,
                                         String parameter,
                                         Long userId) {
        if (userService.existsOnOtherAccount(field, parameter, userId)) {
            valueErrors.put(field, FormErrors.getUserProfileErrors().get(field));
        }
    }

    public Map<String, String> loadAdminUserEditErrors(BindingResult bindingResult,
                                                       AdminPanelUserDto editUserDto,
                                                       AdminPanelUserDto targetUser) {

        Map<String, String> valueErrors = loadBindingErrors(bindingResult);

        checkForExistingAccount(CommonAttribute.EMAIL_FIELD, valueErrors,
                editUserDto.getEmail(), targetUser.getId());
        checkForExistingAccount(CommonAttribute.USERNAME_FIELD, valueErrors,
                editUserDto.getUsername(), targetUser.getId());

        return valueErrors;
    }
}
