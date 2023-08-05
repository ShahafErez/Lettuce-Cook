package com.shahaf.lettucecook.exceptions;

public class UserDetailsAlreadyExistsException extends RuntimeException {

    public UserDetailsAlreadyExistsException(String msg) {
        super(msg);
    }
}