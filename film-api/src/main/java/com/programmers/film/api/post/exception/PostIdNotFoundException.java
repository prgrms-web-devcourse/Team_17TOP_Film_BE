package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.EntityNotFoundException;

public class PostIdNotFoundException extends EntityNotFoundException {
    public PostIdNotFoundException(String message) {
        super(message, ErrorCode.POST_ID_ERROR);
    }
}
