package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.exception.BusinessException;
import com.programmers.film.common.error.exception.EntityNotFoundException;
import com.programmers.film.common.error.exception.ErrorCode;

public class PostIdNotFoundException extends EntityNotFoundException {
    public PostIdNotFoundException(String message) {
        super(message, ErrorCode.POST_ID_ERROR);
    }
}
