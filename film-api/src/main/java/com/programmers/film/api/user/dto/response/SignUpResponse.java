package com.programmers.film.api.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpResponse {

	private Long userId;
	private String nickname;
}
