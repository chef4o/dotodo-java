package com.pago.dotodo.model.error;

import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ModelAndViewParser attributeBuilder;

    public GlobalExceptionHandler(ModelAndViewParser attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleObjectNotFoundException(NoResourceFoundException e) {
        return new ModelAndView("index", attributeBuilder.build(
                "pageName", "err",
                "errorCode", "404",
                "serverError", e.getMessage()));
    }
}
