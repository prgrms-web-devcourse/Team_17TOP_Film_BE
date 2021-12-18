package com.programmers.film.api.auth.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.HandleAccessDeniedException;

public class EmptyTokenException extends HandleAccessDeniedException {

    public EmptyTokenException(String message) {
        super(message, ErrorCode.EMPTY_TOKEN);
    }
}
