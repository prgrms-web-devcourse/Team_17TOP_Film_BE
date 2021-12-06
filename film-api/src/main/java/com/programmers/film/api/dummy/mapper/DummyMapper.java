package com.programmers.film.api.dummy.mapper;

import com.programmers.film.api.dummy.dto.DummyResponse;
import com.programmers.film.domain.dummy.domain.DummyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DummyMapper {

  DummyResponse entityToResponse(DummyEntity dummyEntity);
}

@Mapping(
)
@Mapping(

)
dt