package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.InvalidInputValueException;

public class PostOpenableAtException extends InvalidInputValueException {

    public PostOpenableAtException(String message) {
        super(message, ErrorCode.OPENABLE_AT_ERROR);
    }
}
