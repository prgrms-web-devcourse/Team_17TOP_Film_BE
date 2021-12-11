package com.programmers.film.api.user.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.EntityNotFoundException;

public class UserIdNotFoundExceoption extends EntityNotFoundException {
    public UserIdNotFoundExceoption(String message) {
        super(message, ErrorCode.USER_NOT_EXIST);
    }
}
