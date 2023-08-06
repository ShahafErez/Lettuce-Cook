package com.shahaf.lettucecook.exceptions;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ErrorMessages implements Serializable {
    private Map<String, String> errors;
}
