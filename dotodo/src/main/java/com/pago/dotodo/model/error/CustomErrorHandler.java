package com.pago.dotodo.model.error;

import com.pago.dotodo.configuration.constraint.error.FormErrors;
import com.pago.dotodo.model.dto.Date;
import com.pago.dotodo.model.view.UserProfileView;
import com.pago.dotodo.service.UserService;
import com.pago.dotodo.util.DateTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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

    public Map<String, String> loadNoteErrors(BindingResult bindingResult,
                                              Date noteDto) {
        Map<String, String> valueErrors = loadBindingErrors(bindingResult);

        if (valueErrors.get(FormErrors.DATE_FIELD) == null) {
            if (noteDto.getDueDate() == null || noteDto.getDueDate().isBlank()
                    && (noteDto.getDueTime() != null & !noteDto.getDueTime().isBlank())) {

                valueErrors.put(FormErrors.DATE_FIELD, FormErrors.TIME_WITHOUT_DATE);
            } else if (noteDto.getDueDate() != null && !noteDto.getDueDate().isBlank()
                    && !dateTimeUtil.isInFuture(noteDto.getDueDate(), noteDto.getDueTime())) {

                valueErrors.put(FormErrors.DATE_FIELD, FormErrors.PAST_DUE_DATE);
            }
        }

        return valueErrors;
    }

    public Map<String, String> loadUserProfileErrors(BindingResult bindingResult,
                                                     UserProfileView profileEditDetails, Long userId) {

        Map<String, String> valueErrors = loadBindingErrors(bindingResult);
        checkForExistingAccount(FormErrors.EMAIL_FIELD, valueErrors, profileEditDetails, userId);
        checkForExistingAccount(FormErrors.USERNAME_FIELD, valueErrors, profileEditDetails, userId);

        if (userService.dateOfBirthMismatch(profileEditDetails, userId)) {
            valueErrors.put(FormErrors.DOB_FIELD, FormErrors.DOB_CHANGE_ERROR);
        }

        return valueErrors;
    }

    public static Map<String, String> loadBindingErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getField() + " " + error.getDefaultMessage()
                ));
    }

    private void checkForExistingAccount(String field,
                                         Map<String, String> valueErrors,
                                         UserProfileView profileEditDetails,
                                         Long userId) {
        if (userService.existsOnOtherAccount(field, profileEditDetails, userId)) {
            valueErrors.put(field, FormErrors.getUserProfileErrors().get(field));
        }
    }
}
