package com.programmers.film.api.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchUserResponse {

    private final Long id;

    private final String nickname;

    private final String profileImageUrl;
}
