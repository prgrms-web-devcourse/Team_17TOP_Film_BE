package com.programmers.film.common.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private int status;
    private String code;

    private ErrorResponse(ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
    }

    public static ErrorResponse from(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse from(MethodArgumentTypeMismatchException e) {
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE);
    }

    public static ErrorResponse of(final ErrorCode code, final String message) {
        return ErrorResponse.builder()
            .message(message)
            .status(code.getStatus())
            .code(code.getCode())
            .build();
    }
}
