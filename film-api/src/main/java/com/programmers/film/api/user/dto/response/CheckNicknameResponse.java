package com.programmers.film.api.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CheckNicknameResponse {

    private final String nickname;

    private final Boolean isDuplicate;
}
