package com.shahaf.lettucecook.exceptions;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {
    private final ErrorMessages errorMessages;
    private final String errorMessage;

    public ResourceAlreadyExistsException(ErrorMessages errorMessages) {
        this.errorMessages = errorMessages;
        this.errorMessage = null;
    }

    public ResourceAlreadyExistsException(String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorMessages = null;
    }
}
