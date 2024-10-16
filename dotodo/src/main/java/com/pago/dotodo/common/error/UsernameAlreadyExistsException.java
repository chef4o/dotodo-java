package com.pago.dotodo.common.error;

import com.pago.dotodo.common.error.constraints.ExceptionErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = ExceptionErrors.EXISTING_USERNAME)
public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException() {
        super(ExceptionErrors.EXISTING_USERNAME);
    }
}