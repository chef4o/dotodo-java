package com.pago.dotodo.common.error;

import com.pago.dotodo.common.error.constraints.ExceptionErrors;
import com.pago.dotodo.common.web.constraint.CommonAttribute;
import com.pago.dotodo.common.web.constraint.ErrorPageAttribute;
import com.pago.dotodo.common.util.ModelAndViewParser;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ModelAndViewParser attributeBuilder;

    public GlobalExceptionHandler(ModelAndViewParser attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
    }

    private ModelAndView buildErrorResponse(String errorMessage, HttpStatus status) {
        ModelAndView modelAndView = new ModelAndView(CommonAttribute.GLOBAL_VIEW,
                attributeBuilder.build(
                        CommonAttribute.PAGE_NAME, ErrorPageAttribute.LOCAL_VIEW,
                        ErrorPageAttribute.ERROR_CODE, status.value(),
                        ErrorPageAttribute.SERVER_ERROR, errorMessage)
        );
        modelAndView.setStatus(status);
        return modelAndView;
    }

    @ExceptionHandler({BadRequestException.class, MethodArgumentTypeMismatchException.class})
    public ModelAndView handleBadRequestException(Exception e) {
        String errorMessage;
        if (e instanceof MethodArgumentTypeMismatchException ex) {
            errorMessage = ex.getValue() != null
                    ? String.format(ExceptionErrors.INVALID_PARAMETER, ex.getValue().toString())
                    : String.format(ExceptionErrors.INVALID_PARAMETER, "Null");
        } else {
            errorMessage = e.getMessage();
        }

        return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessForbiddenException(AccessDeniedException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NoResourceFoundException.class, ObjectNotFoundException.class})
    public ModelAndView handleObjectNotFoundException(Exception e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
