package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.exception.EntityNotFoundException;
import com.programmers.film.common.error.exception.ErrorCode;

public class PostCanNotOpenException extends EntityNotFoundException {

    public PostCanNotOpenException(String message) {
        super(message, ErrorCode.HANDLE_ACCESS_DENIED);
    }
}
