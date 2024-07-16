package com.shahaf.recipe_service.exceptions;

public class BadRequestException extends IllegalArgumentException {
    public BadRequestException(String msg) {
        super(msg);
    }
}