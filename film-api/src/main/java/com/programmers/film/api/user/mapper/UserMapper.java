package com.programmers.film.api.user.mapper;

import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.SignUpResponse;
import com.programmers.film.domain.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

	User requestToEntity(SignUpRequest signUpRequest);

	@Mapping(source = "id", target = "userId")
	SignUpResponse entityToResponse(User user);
}
