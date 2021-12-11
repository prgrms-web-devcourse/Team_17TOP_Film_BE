package com.programmers.film.common.error.exception;

import com.programmers.film.common.error.ErrorCode;

public class InternalServerErrorException extends BusinessException {

	public InternalServerErrorException(String message) {
		super(message, ErrorCode.INTERNAL_SERVER_ERROR);
	}

	public InternalServerErrorException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}
}
