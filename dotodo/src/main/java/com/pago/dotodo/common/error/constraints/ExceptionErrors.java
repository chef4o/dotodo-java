package com.pago.dotodo.common.error.constraints;

public class ExceptionErrors {
    public static final String OBJECT_DOES_NOT_EXIST = "%s with ID %s does not exist";
    public static final String INVALID_PARAMETER = "%s is not a valid parameter value";
    public static final String EXISTING_USER = "Such user already exists. Please proceed to login";
    public static final String EXISTING_EMAIL = "The email address is already registered with another username";
    public static final String EXISTING_USERNAME = "The username is already taken";
    public static final String NON_ADMIN_ACCESS_DENIED = "Access denied - allowed only to administrators";
    public static final String NON_OWNER_ACCESS_DENIED = "Access denied - permitted only to the resource owners";
    public static final String NOT_LOGGED_USER = "Access denied - permitted only to logged users";


}
