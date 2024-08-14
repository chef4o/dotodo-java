package com.pago.dotodo.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class PasswordMatcher implements ConstraintValidator<PasswordMatch, Object> {

    private String rawPassword;
    private String rePassword;
    private String message;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        rawPassword = constraintAnnotation.rawPassword();
        rePassword = constraintAnnotation.rePassword();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
        Object rawPasswordValue = beanWrapper.getPropertyValue(rawPassword);
        Object rePasswordValue = beanWrapper.getPropertyValue(rePassword);

        if (rawPasswordValue != null && rawPasswordValue.equals(rePasswordValue)) {
            return true;
        }

        constraintValidatorContext
                .buildConstraintViolationWithTemplate(message)
                .addPropertyNode(rePassword)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }
}
