package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.InvalidInputValueException;

public class PostPreviewTextException extends InvalidInputValueException {

    public PostPreviewTextException(String message) {
        super(message, ErrorCode.PREVIEW_TEXT_ERROR);
    }
}
