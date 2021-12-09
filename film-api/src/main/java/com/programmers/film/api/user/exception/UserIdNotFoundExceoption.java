package com.programmers.film.api.user.exception;

import com.programmers.film.common.error.exception.EntityNotFoundException;
import com.programmers.film.common.error.exception.ErrorCode;

public class UserIdNotFoundExceoption extends EntityNotFoundException {
    public UserIdNotFoundExceoption(String message) {
        super(message, ErrorCode.USER_NOT_EXIST);
    }
}
