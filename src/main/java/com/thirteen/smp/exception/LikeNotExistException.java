package com.thirteen.smp.exception;

/**
 * 点赞不存在异常
 */
public class LikeNotExistException extends RuntimeException{
    public LikeNotExistException() {
        super();
    }

    public LikeNotExistException(String message) {
        super(message);
    }

}
