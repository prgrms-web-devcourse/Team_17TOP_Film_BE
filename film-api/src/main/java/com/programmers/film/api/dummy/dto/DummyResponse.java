package com.programmers.film.api.dummy.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DummyResponse {

  @NotNull
  private Long id;

  @NotNull
  private String name;
}
