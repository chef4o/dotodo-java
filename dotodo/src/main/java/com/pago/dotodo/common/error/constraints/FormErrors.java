package com.pago.dotodo.common.error.constraints;

import com.pago.dotodo.common.web.constraint.CommonAttribute;

import java.util.Map;

public class FormErrors {

    public static final String EXISTING_EMAIL_ERROR = "Email exists on other account";
    public static final String EXISTING_USERNAME_ERROR = "Username exists on other account";
    public static final String TEXT_TOO_SHORT = "The text you have entered is too short";
    public static final String TIME_WITHOUT_DATE = "Due time cannot be added without a date";
    public static final String PAST_DUE_DATE = "Due date must be in the future";
    public static final String MISSING_HEADER_OR_CONTENT = "Header and content are both mandatory";
    public static final String MISSING_PHONE_OR_EMAIL = "We need either phone or email to be able to contact you back";
    public static final String INVALID_EMAIL = "Email should be valid";

    public static Map<String, String> getUserProfileErrors() {
        return Map.of(
                CommonAttribute.EMAIL_FIELD, EXISTING_EMAIL_ERROR,
                CommonAttribute.USERNAME_FIELD, EXISTING_USERNAME_ERROR);
    }
}
