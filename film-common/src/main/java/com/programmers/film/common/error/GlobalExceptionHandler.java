package com.programmers.film.common.error;

import com.programmers.film.common.error.exception.BusinessException;
import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	// @Valid error
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e
	) {
		log.error("handleMethodArgumentNotValidException", e);

		String message = e.getBindingResult().getFieldErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining("\n"));

		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.REQUEST_INVALID, message);

		return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.REQUEST_INVALID.getStatus()));
	}

	// @RequestParam enum type mismatch error
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException e) {
		log.error("handleMethodArgumentTypeMismatchException", e);
		ErrorResponse errorResponse = ErrorResponse.from(e);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 지원하지 않는 HTTP Method error
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e) {
		log.error("handleAccessDeniedException", e);
		ErrorResponse errorResponse = ErrorResponse.from(ErrorCode.METHOD_NOT_ALLOWED);

		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
		log.error("handleAccessDeniedException", e);
		final ErrorResponse errorResponse = ErrorResponse.from(ErrorCode.HANDLE_ACCESS_DENIED);

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	}

	// Business error
	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		log.error("handleBusinessException", e);
		final ErrorCode errorCode = e.getErrorCode();
		final ErrorResponse errorResponse = ErrorResponse.of(errorCode, e.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));
	}

	// other Exception
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("handleException", e);
		final ErrorResponse errorResponse = ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR);

		return ResponseEntity.internalServerError().body(errorResponse);
	}
}
