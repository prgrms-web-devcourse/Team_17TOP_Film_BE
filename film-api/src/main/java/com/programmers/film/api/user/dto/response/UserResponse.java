package com.programmers.film.api.user.dto.response;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {

	private String nickname;

	private String profileImageUrl;
}
