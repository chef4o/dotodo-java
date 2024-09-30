package com.pago.dotodo.model.error;

import com.pago.dotodo.configuration.constraint.error.ExceptionErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = ExceptionErrors.EXISTING_EMAIL)
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super(ExceptionErrors.EXISTING_EMAIL);
    }
}