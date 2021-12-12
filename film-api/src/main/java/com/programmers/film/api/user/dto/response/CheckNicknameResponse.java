package com.programmers.film.api.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CheckNicknameResponse {

    private String nickname;

    private Boolean isDuplicate;
}
