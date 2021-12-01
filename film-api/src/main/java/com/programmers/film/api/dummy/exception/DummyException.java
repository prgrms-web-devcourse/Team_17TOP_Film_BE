package com.programmers.film.api.dummy.exception;

import com.programmers.film.common.error.exception.ErrorCode;
import com.programmers.film.common.error.exception.InvalidInputValueException;

public class DummyException extends InvalidInputValueException {

  public DummyException(final Long userId) {
    super(userId.toString(), ErrorCode.NICKNAME_NULL);
  }
}
