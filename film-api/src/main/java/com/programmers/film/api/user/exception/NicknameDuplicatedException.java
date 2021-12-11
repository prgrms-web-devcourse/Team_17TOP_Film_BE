package com.programmers.film.api.user.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.InvalidInputValueException;

public class NicknameDuplicatedException extends InvalidInputValueException {

	public NicknameDuplicatedException(String message) {
		super(message, ErrorCode.NICKNAME_DUPLICATED);
	}
}
