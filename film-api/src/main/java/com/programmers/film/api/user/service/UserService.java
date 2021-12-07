package com.programmers.film.api.user.service;

import static org.assertj.core.util.Preconditions.checkArgument;

import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.SignUpResponse;
import com.programmers.film.api.user.mapper.UserMapper;
import com.programmers.film.domain.user.domain.User;
import com.programmers.film.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

	@Transactional
	public SignUpResponse signUp(SignUpRequest signUpRequest) {

		checkArgument(signUpRequest != null, "signUpRequest must be provided.");

		String nickname = signUpRequest.getNickname();
		String providerId = signUpRequest.getProviderId();
		User user = User.of(nickname, providerId);
		User savedUser = userRepository.save(user);

		return userMapper.entityToResponse(savedUser);
	}
}
