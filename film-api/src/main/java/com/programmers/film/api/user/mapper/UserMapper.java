package com.programmers.film.api.user.mapper;

import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.SearchUserResponse;
import com.programmers.film.api.user.dto.response.UserResponse;
import com.programmers.film.domain.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(source = "profileImageUrl", target = "profileImageUrl.originalSizeUrl")
	User signUpRequestToEntity(SignUpRequest signUpRequest);

	@Mapping(source = "profileImageUrl.originalSizeUrl", target = "profileImageUrl")
	UserResponse entityToUserResponse(User user);

	@Mapping(source = "profileImageUrl.originalSizeUrl", target = "profileImageUrl")
	SearchUserResponse entityToSearchUserResponse(User user);
}
