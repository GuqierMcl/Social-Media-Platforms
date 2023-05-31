package com.thirteen.smp.exception;

/**
 * 点赞已存在异常
 */
public class LikeExistException extends RuntimeException{
    public LikeExistException() {
        super();
    }

    public LikeExistException(String message) {
        super(message);
    }
}
