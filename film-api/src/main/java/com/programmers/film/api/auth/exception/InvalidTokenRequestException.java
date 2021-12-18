package com.programmers.film.api.auth.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.BusinessException;
import com.programmers.film.common.error.exception.HandleAccessDeniedException;

public class InvalidTokenRequestException extends HandleAccessDeniedException {

    public InvalidTokenRequestException(String message) {
        super(message, ErrorCode.INVALID_TOKEN);
    }
}
