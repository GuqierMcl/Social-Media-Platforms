package com.thirteen.smp.exception;

public class PostNotExistException extends RuntimeException{
    public PostNotExistException() {
        super();
    }

    public PostNotExistException(String message) {
        super(message);
    }
}
