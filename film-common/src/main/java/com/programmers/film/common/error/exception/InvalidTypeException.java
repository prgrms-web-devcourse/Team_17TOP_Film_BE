package com.programmers.film.common.error.exception;

import com.programmers.film.common.error.ErrorCode;

public class InvalidTypeException extends BusinessException {

	public InvalidTypeException(String message) {
		super(message, ErrorCode.INVALID_TYPE_VALUE);
	}

	public InvalidTypeException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}
}
