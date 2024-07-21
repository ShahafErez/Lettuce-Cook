package com.shahaf.recipe_service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResourceAlreadyExistsException extends RuntimeException {
    private final Object errorMessage;

}
