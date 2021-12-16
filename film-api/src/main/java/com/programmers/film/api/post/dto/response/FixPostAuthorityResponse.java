package com.programmers.film.api.post.dto.response;

import com.programmers.film.api.post.dto.common.SimpleAuthorityDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FixPostAuthorityResponse {
    private Long postId;
    private List<SimpleAuthorityDto> authorityList;
}
