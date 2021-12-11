package com.programmers.film.common.error.exception;

import com.programmers.film.common.error.ErrorCode;

public class HandleAccessDeniedException extends BusinessException {

	public HandleAccessDeniedException(String message) {
		super(message, ErrorCode.HANDLE_ACCESS_DENIED);
	}

	public HandleAccessDeniedException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}
}
