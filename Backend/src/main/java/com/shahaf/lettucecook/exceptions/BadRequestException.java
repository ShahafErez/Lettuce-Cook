package com.shahaf.lettucecook.exceptions;

public class BadRequestException extends IllegalArgumentException {
    public BadRequestException(String msg) {
        super(msg);
    }
}