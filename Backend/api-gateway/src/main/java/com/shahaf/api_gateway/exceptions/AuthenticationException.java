package com.shahaf.api_gateway.exceptions;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String msg) {
        super(msg);
    }
}