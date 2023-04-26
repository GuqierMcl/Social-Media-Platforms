package com.thirteen.smp.exception;

public class UserNotExistsException extends RuntimeException{
    public UserNotExistsException() {
    }

    public UserNotExistsException(String message) {
        super(message);
    }
}
