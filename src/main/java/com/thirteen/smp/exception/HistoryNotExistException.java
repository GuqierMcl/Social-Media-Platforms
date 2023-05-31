package com.thirteen.smp.exception;

/**
 * 历史记录不存在异常
 */
public class HistoryNotExistException extends RuntimeException{
    public HistoryNotExistException() {
        super();
    }

    public HistoryNotExistException(String message) {
        super(message);
    }
}
