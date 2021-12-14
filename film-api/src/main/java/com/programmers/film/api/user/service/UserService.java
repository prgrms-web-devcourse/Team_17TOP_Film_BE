package com.programmers.film.api.user.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;

import com.programmers.film.api.auth.dto.ProviderAttribute;
import com.programmers.film.api.auth.exception.AuthNotFoundException;
import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.CheckNicknameResponse;
import com.programmers.film.api.user.dto.response.CheckUserResponse;
import com.programmers.film.api.user.dto.response.UserResponse;
import com.programmers.film.api.user.exception.NicknameDuplicatedException;
import com.programmers.film.api.user.exception.UserIdNotFoundException;
import com.programmers.film.api.user.mapper.UserMapper;
import com.programmers.film.domain.auth.domain.Auth;
import com.programmers.film.domain.auth.repository.AuthRepository;
import com.programmers.film.domain.user.domain.User;
import com.programmers.film.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final AuthRepository authRepository;
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

		UserResponse userResponse = userRepository.findByProviderAndProviderId(provider.getProvider(),
			provider.getProviderId())
			.map(userMapper::entityToUserResponse)
			.orElse(null);

		if (userResponse == null) {
			return CheckUserResponse.builder()
				.isDuplicate(false)
				.build();
		}

		return CheckUserResponse.builder()
			.isDuplicate(true)
			.nickname(userResponse.getNickname())
			.profileImageUrl(userResponse.getProfileImageUrl())
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
	public UserResponse signUp(SignUpRequest signUpRequest, ProviderAttribute providerAttribute) {
		checkArgument(signUpRequest != null, "signUpRequest must be provided.");
		checkArgument(providerAttribute != null, "provider must be provided.");

		if (checkNicknameDuplicated(signUpRequest.getNickname())) {
			throw new NicknameDuplicatedException("중복된 닉네임입니다");
		}

		String provider = providerAttribute.getProvider();
		String providerId = providerAttribute.getProviderId();

		Auth auth = authRepository.findByProviderAndProviderId(provider, providerId)
			.orElseThrow(() -> new AuthNotFoundException("사용자를 찾을 수 없습니다."));

		User user = userMapper.requestToEntity(signUpRequest);
		user.setProvider(provider, providerId);

		auth.setUser(user);
		User savedUser = userRepository.save(user);

		return userMapper.entityToUserResponse(savedUser);
	}

	public boolean checkNicknameDuplicated(String nickname) {
		return userRepository.findByNickname(nickname).isPresent();
	}
}
