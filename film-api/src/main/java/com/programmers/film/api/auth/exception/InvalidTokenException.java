package com.programmers.film.api.auth.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.BusinessException;
import com.programmers.film.common.error.exception.HandleAccessDeniedException;

public class InvalidTokenException extends HandleAccessDeniedException {

    public InvalidTokenException(String message) {
        super(message, ErrorCode.INVALID_TOKEN);
    }
}
