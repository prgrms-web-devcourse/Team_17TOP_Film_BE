package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.exception.ErrorCode;
import com.programmers.film.common.error.exception.InvalidInputValueException;

public class PostTitleException extends InvalidInputValueException {

    public PostTitleException(String message) {
        super(message, ErrorCode.TITLE_ERROR);
    }
}
