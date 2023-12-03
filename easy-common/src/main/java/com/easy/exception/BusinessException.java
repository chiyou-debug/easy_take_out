package com.easy.exception;

/**
 * Business exception
 */
public class BusinessException extends BaseException {
    public BusinessException() {
    }

    public BusinessException(String msg) {
        super(msg);
    }
}
