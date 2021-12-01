package com.programmers.film.api.dummy.api;

import com.programmers.film.api.dummy.dto.DummyResponse;
import com.programmers.film.api.dummy.service.DummyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/dummy")
@RequiredArgsConstructor
@RestController
public class DummyApi {

  private final DummyService dummyService;

  @GetMapping("/{dummyId}")
  public DummyResponse findById(@PathVariable final Long dummyId) {
    return dummyService.findById(dummyId);
  }
}
