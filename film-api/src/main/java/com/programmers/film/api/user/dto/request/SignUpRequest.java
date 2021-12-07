package com.programmers.film.api.user.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor		// for json with single field class
@AllArgsConstructor		// for json with single field class
public class SignUpRequest {

	@NotBlank
	private String nickname;
}
