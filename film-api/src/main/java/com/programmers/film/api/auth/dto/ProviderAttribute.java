package com.programmers.film.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProviderAttribute {

    private String provider;
    private String providerId;
}
