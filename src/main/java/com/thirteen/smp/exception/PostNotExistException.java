package com.thirteen.smp.exception;

/**
 * 帖子不存在异常
 */
public class PostNotExistException extends RuntimeException{
    public PostNotExistException() {
        super();
    }

    public PostNotExistException(String message) {
        super(message);
    }
}
