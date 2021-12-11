package com.programmers.film.api.user.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;

import com.programmers.film.api.auth.dto.ProviderAttribute;
import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.CheckNicknameResponse;
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

	@Transactional(readOnly = true)
	public boolean checkUser(Long userId) {
		checkArgument(userId != null, "userId must be provided.");

		return userRepository.findById(userId).isPresent();
	}

	@Transactional
	public SignUpResponse signUp(SignUpRequest signUpRequest, ProviderAttribute provider) {

		checkArgument(signUpRequest != null, "signUpRequest must be provided.");
		checkArgument(provider != null, "provider must be provided.");

		User user = userMapper.requestToEntity(signUpRequest);
		user.updateProvider(provider.getProvider(), provider.getProviderId());

		User savedUser = userRepository.save(user);

		return userMapper.entityToResponse(savedUser);
	}

	@Transactional(readOnly = true)
	public CheckNicknameResponse checkNickname(String nickname) {

		checkArgument(isNotEmpty(nickname), "nickname must be provided.");

		return CheckNicknameResponse.builder()
			.nickname(nickname)
			.isDuplicate(userRepository.findByNickname(nickname).isPresent())
			.build();
	}
}
