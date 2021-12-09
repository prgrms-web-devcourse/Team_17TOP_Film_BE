package com.programmers.film.api.auth.exception;

import com.programmers.film.common.error.exception.EntityNotFoundException;

public class AuthNotFoundException extends EntityNotFoundException {

	public AuthNotFoundException(String message) {
		super(message);
	}
}
