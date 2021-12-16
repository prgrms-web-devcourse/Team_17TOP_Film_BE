package com.programmers.film.api.post.dto.request;

import com.programmers.film.api.post.dto.common.SimpleFixAuthorityDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixPostAuthorityRequest {
    private List<SimpleFixAuthorityDto> fixAuthorityList;
}
