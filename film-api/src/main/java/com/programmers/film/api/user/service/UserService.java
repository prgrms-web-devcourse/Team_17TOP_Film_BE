package com.programmers.film.api.user.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;

import com.programmers.film.api.auth.dto.ProviderAttribute;
import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.CheckNicknameResponse;
import com.programmers.film.api.user.dto.response.CheckUserResponse;
import com.programmers.film.api.user.dto.response.UserResponse;
import com.programmers.film.api.user.exception.NicknameDuplicatedException;
import com.programmers.film.api.user.exception.UserIdNotFoundException;
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
	public UserResponse getUser(Long userId) {
		checkArgument(userId != null, "userId must be provided.");

		return userRepository.findById(userId)
			.map(userMapper::entityToUserResponse)
			.orElseThrow(() -> new UserIdNotFoundException("사용자를 찾을 수 없습니다."));
	}

	@Transactional(readOnly = true)
	public CheckUserResponse checkUser(ProviderAttribute provider) {
		checkArgument(provider != null, "provider must be provided.");

		boolean isUserDuplicate = userRepository.findByProviderAndProviderId(provider.getProvider(),
			provider.getProviderId()).isPresent();

		return CheckUserResponse.builder()
			.isDuplicate(isUserDuplicate)
			.build();
	}

	@Transactional(readOnly = true)
	public CheckNicknameResponse checkNickname(String nickname) {
		checkArgument(isNotEmpty(nickname), "nickname must be provided.");

		return CheckNicknameResponse.builder()
			.nickname(nickname)
			.isDuplicate(checkNicknameDuplicated(nickname))
			.build();
	}

	@Transactional
	public UserResponse signUp(SignUpRequest signUpRequest, ProviderAttribute provider) {
		checkArgument(signUpRequest != null, "signUpRequest must be provided.");
		checkArgument(provider != null, "provider must be provided.");

		if (checkNicknameDuplicated(signUpRequest.getNickname())) {
			throw new NicknameDuplicatedException("중복된 닉네임입니다");
		}

		User user = userMapper.requestToEntity(signUpRequest);
		user.updateProvider(provider.getProvider(), provider.getProviderId());

		User savedUser = userRepository.save(user);

		return userMapper.entityToUserResponse(savedUser);
	}

	public boolean checkNicknameDuplicated(String nickname) {
		return userRepository.findByNickname(nickname).isPresent();
	}
}
