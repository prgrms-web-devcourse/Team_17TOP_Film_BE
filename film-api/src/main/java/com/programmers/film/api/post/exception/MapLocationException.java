package com.programmers.film.api.post.exception;

import com.programmers.film.common.error.ErrorCode;
import com.programmers.film.common.error.exception.InvalidTypeException;

public class MapLocationException extends InvalidTypeException {

    public MapLocationException(String message) {
        super(message, ErrorCode.LOCATION_ERROR);
    }

}
