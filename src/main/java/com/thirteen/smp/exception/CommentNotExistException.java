package com.thirteen.smp.exception;

public class CommentNotExistException extends RuntimeException{
    public CommentNotExistException() {
        super();
    }

    public CommentNotExistException(String message) {
        super(message);
    }
}
