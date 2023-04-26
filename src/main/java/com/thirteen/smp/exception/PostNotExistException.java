package com.thirteen.smp.exception;

public class PostNotExistException extends Exception{
    public PostNotExistException() {
        super();
    }

    public PostNotExistException(String message) {
        super(message);
    }
}
