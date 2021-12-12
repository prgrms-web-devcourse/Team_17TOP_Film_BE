package com.programmers.film.api.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.UserResponse;
import com.programmers.film.api.user.service.UserService;
import com.programmers.film.common.error.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = UserController.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@Mock
	private UserController userController;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
			.setControllerAdvice(GlobalExceptionHandler.class)
			.build();
	}

	@Test
	@DisplayName("올바르지 못한 닉네임으로 회원가입하는 경우")
	public void signupWithInvalidNicknameRequest() throws Exception {

		// Given
		final String nickname = "asdasd@d"; // Invalid nickname
		final SignUpRequest signUpRequest = SignUpRequest.builder()
			.nickname(nickname)
			.build();
		final UserResponse signUpResponse = UserResponse.builder()
			.nickname(nickname)
			.build();

		given(userService.signUp(any(), any())).willReturn(signUpResponse);

		// When
		final ResultActions resultActions = mockMvc.perform(post("/api/v1/users/signup")
				.content(objectMapper.writeValueAsString(signUpRequest))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// Then
		resultActions.andExpect(status().isBadRequest());
	}
}
