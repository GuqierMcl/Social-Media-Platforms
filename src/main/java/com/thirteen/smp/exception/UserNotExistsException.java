package com.thirteen.smp.exception;

/**
 * 用户不存在异常
 */
public class UserNotExistsException extends RuntimeException{
    public UserNotExistsException() {
    }

    public UserNotExistsException(String message) {
        super(message);
    }
}
