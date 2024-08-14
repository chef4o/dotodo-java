package com.pago.dotodo.model.error;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Object not found")
public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, long productId) {
        super(StringUtils.capitalize(objectName) + " with ID " + productId + " does not exist");
    }
}