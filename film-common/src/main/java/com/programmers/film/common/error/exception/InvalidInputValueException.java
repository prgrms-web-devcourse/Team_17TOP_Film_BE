package com.programmers.film.common.error.exception;

public class InvalidInputValueException extends BusinessException {

  public InvalidInputValueException(String message) {
    super(message, ErrorCode.INVALID_INPUT_VALUE);
  }

  public InvalidInputValueException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }
}
