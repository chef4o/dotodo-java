package com.pago.dotodo.configuration.constraint.error;

import java.util.Map;

public class FormErrors {
    public static final String EMAIL_FIELD = "email";
    public static final String USERNAME_FIELD = "username";
    public static final String DOB_FIELD = "dob";
    public static final String DATE_FIELD = "date";

    public static final String EXISTING_EMAIL_ERROR = "Email exists on other account";
    public static final String EXISTING_USERNAME_ERROR = "Username exists on other account";
    public static final String DOB_CHANGE_ERROR = "You need to contact support to change the date of birth";
    public static final String TIME_WITHOUT_DATE = "Due time cannot be added without a date";
    public static final String PAST_DUE_DATE = "Due date must be in the future";

    public static Map<String, String> getUserProfileErrors() {
        return Map.of(
                EMAIL_FIELD, EXISTING_EMAIL_ERROR,
                USERNAME_FIELD, EXISTING_USERNAME_ERROR);
    }
}
