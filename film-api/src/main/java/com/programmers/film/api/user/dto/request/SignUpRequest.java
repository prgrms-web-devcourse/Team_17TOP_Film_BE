package com.programmers.film.api.user.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor        // for json parsing with single field class
@NoArgsConstructor        // for json parsing with single field class
public class SignUpRequest {

	@Pattern(regexp = "^[A-Za-z0-9+]{2,20}$", message = "빈값이나 공백이 들어갈 수 없습니다.")
	@NotBlank
	private String nickname;
}
