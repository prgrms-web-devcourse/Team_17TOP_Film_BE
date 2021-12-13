package com.programmers.film.api.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckUserResponse {

	private final Boolean isDuplicate;
}
