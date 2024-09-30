package com.pago.dotodo.model.error;

import com.pago.dotodo.configuration.constraint.error.ExceptionErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = ExceptionErrors.EXISTING_USER)
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super(ExceptionErrors.EXISTING_USER);
    }
}