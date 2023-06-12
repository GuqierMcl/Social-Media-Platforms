package com.thirteen.smp.exception;

/**
 * 敏感词异常
 */
public class PubBannedWordsException extends RuntimeException{
    public PubBannedWordsException() {
    }

    public PubBannedWordsException(String message) {
        super(message);
    }
}
