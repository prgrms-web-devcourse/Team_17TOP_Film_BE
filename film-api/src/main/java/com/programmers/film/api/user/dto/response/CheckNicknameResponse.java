package com.programmers.film.api.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@Getter
public class CheckNicknameResponse {

    private final String nickname;

    private final Boolean isDuplicate;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("nickname", nickname)
            .append("isDuplicate", isDuplicate)
            .toString();
    }
}
