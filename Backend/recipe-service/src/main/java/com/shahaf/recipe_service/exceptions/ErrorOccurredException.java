package com.shahaf.recipe_service.exceptions;

public class ErrorOccurredException extends RuntimeException {
    public ErrorOccurredException(String msg) {
        super(msg);
    }
}
