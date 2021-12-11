package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.InvalidInputValueException;

public class PostAuthorityException extends InvalidInputValueException {

    public PostAuthorityException(String message) {
        super(message, ErrorCode.AUTHORITY_ERROR);
    }
}
