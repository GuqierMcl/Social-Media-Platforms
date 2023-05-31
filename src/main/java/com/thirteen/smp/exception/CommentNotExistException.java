package com.thirteen.smp.exception;

/**
 * 评论不存在异常
 */
public class CommentNotExistException extends RuntimeException{
    public CommentNotExistException() {
        super();
    }

    public CommentNotExistException(String message) {
        super(message);
    }
}
