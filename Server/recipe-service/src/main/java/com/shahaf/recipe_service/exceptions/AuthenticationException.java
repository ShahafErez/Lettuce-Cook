package com.shahaf.recipe_service.exceptions;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String msg) {
        super(msg);
    }
}