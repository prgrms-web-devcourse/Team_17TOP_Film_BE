package com.programmers.film.api.auth.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;

import com.programmers.film.domain.auth.entity.Auth;
import com.programmers.film.domain.auth.entity.Group;
import com.programmers.film.domain.auth.repository.AuthRepository;
import com.programmers.film.domain.auth.repository.GroupRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final AuthRepository authRepository;
	private final GroupRepository groupRepository;

	@Transactional(readOnly = true)
	public Optional<Auth> findByUsername(String username) {
		checkArgument(isNotEmpty(username), "username must be provided.");

		return authRepository.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public Optional<Auth> findByProviderAndProviderId(String provider, String providerId) {
		checkArgument(isNotEmpty(provider), "provider must be provided.");
		checkArgument(isNotEmpty(providerId), "providerId must be provided.");

		return authRepository.findByProviderAndProviderId(provider, providerId);
	}

	@Transactional
	public Auth join(OAuth2User oauth2User, String authorizedClientRegistrationId) {
		checkArgument(oauth2User != null, "oauth2User must be provided.");
		checkArgument(isNotEmpty(authorizedClientRegistrationId),
			"authorizedClientRegistrationId must be provided.");

		String providerId = oauth2User.getName();
		return findByProviderAndProviderId(authorizedClientRegistrationId, providerId)
			.map(auth -> {
				log.warn("Already exists: {} for (provider: {}, providerId: {})",
					auth, authorizedClientRegistrationId, providerId);
				return auth;
			})
			.orElseGet(() -> {
				Group group = groupRepository.findByName("USER_GROUP")
					.orElseThrow(
						() -> new IllegalStateException("Could not found group for USER_GROUP"));

				Map<String, Object> attributes = oauth2User.getAttributes();
				@SuppressWarnings("unchecked")
				Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
				checkArgument(properties != null, "OAuth2User properties is empty");

				String nickname = (String) properties.get("nickname");
				String profileImage = (String) properties.get("profile_image");

				return authRepository.save(
					new Auth(nickname, authorizedClientRegistrationId, providerId, profileImage,
						group)
				);
			});
	}
}
