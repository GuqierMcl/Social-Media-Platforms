package com.thirteen.smp.exception;

public class LikeExistException extends RuntimeException{
    public LikeExistException() {
        super();
    }

    public LikeExistException(String message) {
        super(message);
    }
}
