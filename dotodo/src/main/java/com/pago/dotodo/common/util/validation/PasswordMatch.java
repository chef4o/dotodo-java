package com.pago.dotodo.common.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordMatcher.class)
public @interface PasswordMatch {
    String rawPassword();

    String rePassword();

    String message() default "The repeated password should match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
