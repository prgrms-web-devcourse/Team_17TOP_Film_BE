package com.programmers.film.api.auth.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@RequiredArgsConstructor
public class JwtRequest {

	@NotBlank
	public final String token;

	@NotBlank
	public final String provider;

	@NotBlank
	public final String providerId;

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("token", token)
			.append("provider", provider)
			.append("providerId", providerId)
			.toString();
	}
}
