package com.programmers.film.api.user.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.service.UserService;
import com.programmers.film.domain.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Test
    void signUpRequestToEntity() {
        // Given
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);

        SignUpRequest signUpRequest = SignUpRequest.builder()
            .nickname("iyj")
            .profileImageUrl("dummy.com")
            .build();

        // When
        User user = userMapper.signUpRequestToEntity(signUpRequest);

        // Then
        System.out.println(user);
        assertThat(user).isNotNull();
        assertThat(user.getNickname()).isEqualTo(signUpRequest.getNickname());
        assertThat(user.getProfileImageUrl().getOriginalSizeUrl()).isEqualTo(signUpRequest.getProfileImageUrl());
    }
}