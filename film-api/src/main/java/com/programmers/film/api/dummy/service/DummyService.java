package com.programmers.film.api.dummy.service;

import com.programmers.film.api.dummy.dto.DummyResponse;
import com.programmers.film.api.dummy.exception.DummyException;
import com.programmers.film.api.dummy.mapper.DummyMapper;
import com.programmers.film.domain.dummy.repository.DummyRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DummyService {

  private final DummyMapper dummyMapper = Mappers.getMapper(DummyMapper.class);

  private final DummyRepository dummyRepository;

  public DummyResponse findById(Long userId) {
    return dummyRepository.findById(userId)
        .map(dummyMapper::entityToResponse)
        .orElseThrow(() -> new DummyException(userId));
  }
}
