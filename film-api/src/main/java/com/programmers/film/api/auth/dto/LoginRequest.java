package com.programmers.film.api.auth.dto;

import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@RequiredArgsConstructor
public class LoginRequest {

	@NotBlank
	public final String token;

	@NotBlank
	public final String username;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("token", token)
			.append("username", username)
			.toString();
	}
}
