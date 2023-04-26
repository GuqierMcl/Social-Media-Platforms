package com.thirteen.smp.exception;

public class CommentNotExistException extends Exception{
    public CommentNotExistException() {
        super();
    }

    public CommentNotExistException(String message) {
        super(message);
    }
}
