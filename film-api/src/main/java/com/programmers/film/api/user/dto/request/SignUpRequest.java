package com.programmers.film.api.user.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpRequest {

	@NotBlank
	private String nickname;

	@NotBlank
	private String provider;

	@NotBlank
	private String providerId;
}
