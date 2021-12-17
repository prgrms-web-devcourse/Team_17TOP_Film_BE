package com.programmers.film.api.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Builder
public class CheckUserResponse {

	private final Boolean isDuplicate;

	private final String nickname;

	private final String profileImageUrl;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("isDuplicate", isDuplicate)
			.append("nickname", nickname)
			.append("profileImageUrl", profileImageUrl)
			.toString();
	}
}
