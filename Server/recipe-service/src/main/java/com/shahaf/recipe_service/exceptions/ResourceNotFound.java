package com.shahaf.recipe_service.exceptions;

public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(String msg) {
        super(msg);
    }
}