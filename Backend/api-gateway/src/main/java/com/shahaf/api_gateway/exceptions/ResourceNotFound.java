package com.shahaf.api_gateway.exceptions;

public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(String msg) {
        super(msg);
    }
}