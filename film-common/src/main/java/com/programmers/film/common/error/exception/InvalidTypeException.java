package com.programmers.film.common.error.exception;

public class InvalidTypeException extends BusinessException {

    public InvalidTypeException(String message) {
        super(message, ErrorCode.INVALID_TYPE_VALUE);
    }

    public InvalidTypeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
