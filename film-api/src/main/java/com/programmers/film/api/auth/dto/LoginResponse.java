package com.programmers.film.api.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@RequiredArgsConstructor
public class LoginResponse {

	private final String token;

	private final String username;

	private final String group;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("token", token)
			.append("username", username)
			.append("group", group)
			.toString();
	}
}
