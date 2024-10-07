package com.pago.dotodo.model.error;

import com.pago.dotodo.configuration.constraint.error.FormErrors;
import com.pago.dotodo.configuration.constraint.modelAttribute.CommonAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.ContactAttribute;
import com.pago.dotodo.model.dto.*;
import com.pago.dotodo.model.view.UserProfileView;
import com.pago.dotodo.service.UserService;
import com.pago.dotodo.util.DateTimeUtil;
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
                                              Date noteDto) {
        Map<String, String> valueErrors = loadBindingErrors(bindingResult);

        if (valueErrors.get(CommonAttribute.DATE_FIELD) == null) {
            if (noteDto.getDueDate() == null || noteDto.getDueDate().isBlank()
                    && (noteDto.getDueTime() != null & !noteDto.getDueTime().isBlank())) {

                valueErrors.put(CommonAttribute.DATE_FIELD, FormErrors.TIME_WITHOUT_DATE);
            } else if (noteDto.getDueDate() != null && !noteDto.getDueDate().isBlank()
                    && !dateTimeUtil.isInFuture(noteDto.getDueDate(), noteDto.getDueTime())) {

                valueErrors.put(CommonAttribute.DATE_FIELD, FormErrors.PAST_DUE_DATE);
            }
        }

        return valueErrors;
    }

    public Map<String, String> loadUserProfileErrors(BindingResult bindingResult,
                                                     EditUserProfile profileEditDetails, Long userId) {

        Map<String, String> valueErrors = loadBindingErrors(bindingResult);
        checkForExistingAccount(CommonAttribute.EMAIL_FIELD, valueErrors, profileEditDetails, userId);
        checkForExistingAccount(CommonAttribute.USERNAME_FIELD, valueErrors, profileEditDetails, userId);

        return valueErrors;
    }

    public static Map<String, String> loadBindingErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : ""
                ));
    }

    private void checkForExistingAccount(String field,
                                         Map<String, String> valueErrors,
                                         EditUserProfile profileEditDetails,
                                         Long userId) {
        if (userService.existsOnOtherAccount(field, profileEditDetails, userId)) {
            valueErrors.put(field, FormErrors.getUserProfileErrors().get(field));
        }
    }
}
