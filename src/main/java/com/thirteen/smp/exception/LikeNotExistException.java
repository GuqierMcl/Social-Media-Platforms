package com.thirteen.smp.exception;

public class LikeNotExistException extends RuntimeException{
    public LikeNotExistException() {
        super();
    }

    public LikeNotExistException(String message) {
        super(message);
    }

}
