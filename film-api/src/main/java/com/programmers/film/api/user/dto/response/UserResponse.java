package com.programmers.film.api.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Builder
@Getter
public class UserResponse {

	private final String nickname;

	private final String profileImageUrl;

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("nickname", nickname)
			.append("profileImageUrl", profileImageUrl)
			.toString();
	}
}
