package com.programmers.film.api.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.CheckNicknameResponse;
import com.programmers.film.api.user.dto.response.CheckUserResponse;
import com.programmers.film.api.user.dto.response.UserResponse;
import com.programmers.film.api.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
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
		final ResultActions resultActions = mockMvc.perform(
			post("/api/v1/users/signup")
				.content(objectMapper.writeValueAsString(signUpRequest))
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		resultActions.andExpect(status().isBadRequest())
			.andDo(print());
	}
}
