package com.programmers.film.api.user.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@Getter
public class SignUpRequest {

	@Pattern(regexp = "^[A-Za-z0-9+]{2,20}$", message = "빈값이나 공백이 들어갈 수 없습니다.")
	@NotBlank
	private final String nickname;

	private final String profileImageUrl;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("nickname", nickname)
			.append("profileImageUrl", profileImageUrl)
			.toString();
	}
}
