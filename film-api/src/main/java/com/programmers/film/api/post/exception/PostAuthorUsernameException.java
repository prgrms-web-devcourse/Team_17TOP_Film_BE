package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.exception.ErrorCode;
import com.programmers.film.common.error.exception.InvalidInputValueException;

public class PostAuthorUsernameException extends InvalidInputValueException {

    public PostAuthorUsernameException(String message) {
        super(message, ErrorCode.AUTHOR_NICKNAME_ERROR);
    }
}
