package com.thirteen.smp.exception;

public class UserNotExistsException extends Exception{
    public UserNotExistsException() {
    }

    public UserNotExistsException(String message) {
        super(message);
    }
}
