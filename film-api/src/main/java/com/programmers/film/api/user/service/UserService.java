package com.programmers.film.api.user.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;

import com.programmers.film.domain.user.entity.User;
import com.programmers.film.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public Optional<User> findByUsername(String username) {
		checkArgument(isNotEmpty(username), "username must be provided.");

		return userRepository.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
		checkArgument(isNotEmpty(provider), "provider must be provided.");
		checkArgument(isNotEmpty(providerId), "providerId must be provided.");

		return userRepository.findByProviderAndProviderId(provider, providerId);
	}
}
