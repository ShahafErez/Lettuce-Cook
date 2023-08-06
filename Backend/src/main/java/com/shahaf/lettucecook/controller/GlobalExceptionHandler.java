package com.shahaf.lettucecook.controller;

import com.shahaf.lettucecook.dto.response.ErrorResponse;
import com.shahaf.lettucecook.exceptions.AuthenticationException;
import com.shahaf.lettucecook.exceptions.ResourceAlreadyExistsException;
import com.shahaf.lettucecook.exceptions.ResourceNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(AuthenticationException ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler(value = ResourceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFound ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(value = ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        Object errorResponse = ex.getErrorMessages() != null
                ? ex.getErrorMessages().getErrors()
                : ex.getErrorMessage();

        return new ErrorResponse(HttpStatus.CONFLICT.value(), errorResponse);
    }

}
