package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.exception.ErrorCode;
import com.programmers.film.common.error.exception.InvalidInputValueException;

public class PostContentException extends InvalidInputValueException {

    public PostContentException(String message) {
        super(message, ErrorCode.CONTENT_ERROR);
    }
}
