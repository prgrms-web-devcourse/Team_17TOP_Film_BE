package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.EntityNotFoundException;

public class PostCanNotOpenException extends EntityNotFoundException {

    public PostCanNotOpenException(String message) {
        super(message, ErrorCode.HANDLE_ACCESS_DENIED);
    }
}
