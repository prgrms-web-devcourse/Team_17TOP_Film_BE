package com.programmers.film.api.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.programmers.film.api.auth.dto.ProviderAttribute;
import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.UserResponse;
import com.programmers.film.domain.auth.domain.Auth;
import com.programmers.film.domain.auth.domain.Group;
import com.programmers.film.domain.auth.repository.AuthRepository;
import com.programmers.film.domain.common.domain.ImageUrl;
import com.programmers.film.domain.user.domain.User;
import com.programmers.film.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @DisplayName("회원가입 테스트")
    @Test
    void signUp() {
        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
            .nickname("iyj6707")
            .profileImageUrl("www.dummy.com")
            .build();

        ImageUrl imageUrl = ImageUrl.builder()
            .originalSizeUrl("www.dummy.com")
            .build();

        User user = User.builder()
            .nickname("iyj6707")
            .profileImageUrl(imageUrl)
            .build();

        Group group = new Group();

        Auth auth = new Auth("iyj6707", "kakao", "12345", "url", group);

        ProviderAttribute providerAttribute = ProviderAttribute.builder()
            .provider("kakao")
            .providerId("12345")
            .build();

        given(authRepository.findByProviderAndProviderId(anyString(), anyString()))
            .willReturn(Optional.of(auth));
        given(userRepository.save(any())).willReturn(user);

        // When
        UserResponse userResponse = userService.signUp(signUpRequest, providerAttribute);

        // Then
        System.out.println(userResponse);
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userResponse.getProfileImageUrl()).isEqualTo(user.getProfileImageUrl().getOriginalSizeUrl());
    }
}