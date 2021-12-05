package com.programmers.film.api.auth.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;

import com.programmers.film.domain.auth.domain.Auth;
import com.programmers.film.domain.auth.domain.Group;
import com.programmers.film.domain.auth.repository.AuthRepository;
import com.programmers.film.domain.auth.repository.GroupRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService extends DefaultOAuth2UserService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final AuthRepository authRepository;
	private final GroupRepository groupRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		return super.loadUser(userRequest);
	}

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
	public Auth join(OAuth2User oAuth2User, String authorizedClientRegistrationId) {

		checkArgument(oAuth2User != null, "oauth2User must be provided.");
		checkArgument(isNotEmpty(authorizedClientRegistrationId),
			"authorizedClientRegistrationId must be provided.");

		String providerId = oAuth2User.getName();
		return findByProviderAndProviderId(authorizedClientRegistrationId, providerId)
			.map(user -> {
				log.warn("Already exists: {} for (provider: {}, providerId: {})", user,
					authorizedClientRegistrationId, providerId);
				return user;
			})
			.orElseGet(() -> {
				Map<String, Object> attributes = oAuth2User.getAttributes();
				@SuppressWarnings("unchecked")
				Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
				checkArgument(properties != null, "OAuth2User properties is empty");

				String nickname = (String) properties.get("nickname");
				String profileImage = (String) properties.get("profile_image");
				Group group = groupRepository.findByName("USER_GROUP")
					.orElseThrow(
						() -> new IllegalStateException("Could not found group for USER_GROUP"));
				return authRepository.save(
					new Auth(nickname, authorizedClientRegistrationId, providerId, profileImage,
						group)
				);
			});
	}
}
