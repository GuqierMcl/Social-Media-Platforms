package com.thirteen.smp.exception;

public class HistoryNotExistException extends RuntimeException{
    public HistoryNotExistException() {
        super();
    }

    public HistoryNotExistException(String message) {
        super(message);
    }
}
