package com.pago.dotodo.model.error;

import com.pago.dotodo.configuration.constraint.error.ExceptionErrors;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Object not found")
public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, long productId) {
        super(String.format(ExceptionErrors.OBJECT_DOES_NOT_EXIST, StringUtils.capitalize(objectName), productId));

    }
}