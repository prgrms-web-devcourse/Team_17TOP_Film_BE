package com.programmers.film.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ProviderAttribute {

    private final String provider;
    private final String providerId;
}
