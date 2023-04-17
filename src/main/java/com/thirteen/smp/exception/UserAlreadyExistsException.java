package com.thirteen.smp.exception;

/**
 * 注册时同用户名用户已存在异常
 */
public class UserAlreadyExistsException extends Exception{

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
