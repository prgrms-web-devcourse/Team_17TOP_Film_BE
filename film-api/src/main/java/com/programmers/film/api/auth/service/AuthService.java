package com.programmers.film.api.auth.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;

import com.programmers.film.api.user.service.UserService;
import com.programmers.film.domain.user.entity.Group;
import com.programmers.film.domain.user.entity.User;
import com.programmers.film.domain.user.repository.GroupRepository;
import com.programmers.film.domain.user.repository.UserRepository;
import java.util.Map;
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

	private final UserRepository userRepository;
	private final GroupRepository groupRepository;

	private final UserService userService;

	@Transactional
	public User join(OAuth2User oauth2User, String authorizedClientRegistrationId) {
		checkArgument(oauth2User != null, "oauth2User must be provided.");
		checkArgument(isNotEmpty(authorizedClientRegistrationId),
			"authorizedClientRegistrationId must be provided.");

		String providerId = oauth2User.getName();
		return userService.findByProviderAndProviderId(authorizedClientRegistrationId, providerId)
			.map(user -> {
				log.warn("Already exists: {} for (provider: {}, providerId: {})",
					user, authorizedClientRegistrationId, providerId);
				return user;
			})
			.orElseGet(() -> {
				Map<String, Object> attributes = oauth2User.getAttributes();
				@SuppressWarnings("unchecked")
				Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
				checkArgument(properties != null, "OAuth2User properties is empty");

				String nickname = (String) properties.get("nickname");
				String profileImage = (String) properties.get("profile_image");
				Group group = groupRepository.findByName("USER_GROUP")
					.orElseThrow(
						() -> new IllegalStateException("Could not found group for USER_GROUP"));
				return userRepository.save(
					new User(nickname, authorizedClientRegistrationId, providerId, profileImage,
						group)
				);
			});
	}
}
