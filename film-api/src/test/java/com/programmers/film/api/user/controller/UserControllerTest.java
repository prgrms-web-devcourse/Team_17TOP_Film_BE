package com.programmers.film.api.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.CheckNicknameResponse;
import com.programmers.film.api.user.dto.response.CheckUserResponse;
import com.programmers.film.api.user.dto.response.SearchUserResponse;
import com.programmers.film.api.user.dto.response.UserResponse;
import com.programmers.film.api.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
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

	@DisplayName("토큰으로 유저 정보 가져오는 API test")
	@Test
	public void getUserWithToken() throws Exception {
		// Given
		final UserResponse userResponse = UserResponse.builder()
			.nickname("iyj6707")
			.profileImageUrl("http://dummy")
			.build();
		given(userService.getUser(any())).willReturn(userResponse);

		// When
		final ResultActions resultActions = mockMvc.perform(
			get("/api/v1/users/me")
				.header("Authorization", "Bearer ${token_value}")
		).andDo(print());

		// Then
		resultActions.andExpect(status().isOk())
			.andDo(
				document(
					"users/get-user",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					requestHeaders(
						headerWithName("Authorization").description("jwt 액세스 토큰")
					),
					responseFields(
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
						fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
							.description("프로필 이미지 URL")
					)
				)
			);
	}

	@DisplayName("회원가입 여부 검사")
	@Test
	public void checkUserDuplicate() throws Exception {
		// Given
		final CheckUserResponse checkUserResponse = CheckUserResponse.builder()
			.isDuplicate(true)
			.nickname("iyj")
			.profileImageUrl("http://dummy")
			.build();
		given(userService.checkUser(any())).willReturn(checkUserResponse);

		// When
		final ResultActions resultActions = mockMvc.perform(
			get("/api/v1/users/duplicate")
				.header("Authorization", "Bearer ${token_value}")
		).andDo(print());

		// Then
		resultActions.andExpect(status().isOk())
			.andDo(
				document(
					"users/duplicate-user",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					requestHeaders(
						headerWithName("Authorization").description("jwt 액세스 토큰")
					),
					responseFields(
						fieldWithPath("isDuplicate").type(JsonFieldType.BOOLEAN)
							.description("중복 여부"),
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").optional(),
						fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL").optional()
					)
				)
			);
	}

	@DisplayName("중복 닉네임 검사")
	@Test
	public void checkNicknameDuplicate() throws Exception {
		// Given
		final String nickname = "iyj";
		final CheckNicknameResponse response = CheckNicknameResponse.builder()
			.nickname(nickname)
			.isDuplicate(false)
			.build();

		given(userService.checkNickname(anyString())).willReturn(response);

		// When
		final ResultActions resultActions = mockMvc.perform(
			get("/api/v1/users/{nickname}", nickname)
		).andDo(print());
    
		// Then
		resultActions.andExpect(status().isOk())
			.andDo(
				document(
					"users/duplicate-nickname",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					pathParameters(
						parameterWithName("nickname").description("닉네임")
					),
					responseFields(
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
						fieldWithPath("isDuplicate").type(JsonFieldType.BOOLEAN)
							.description("중복 여부")
					)
				)
			);
	}

	@DisplayName("회원가입")
	@Test
	public void signup() throws Exception {
		// Given
		final String nickname = "iyj";
		final SignUpRequest request = SignUpRequest.builder()
			.nickname(nickname)
			.profileImageUrl("http://dummy.com")
			.build();

		final UserResponse response = UserResponse.builder()
			.nickname(nickname)
			.profileImageUrl("http://dummy.com")
			.build();

		given(userService.signUp(any(), any())).willReturn(response);

		// When
		final ResultActions resultActions = mockMvc.perform(
			post("/api/v1/users/signup")
				.header("Authorization", "Bearer ${token_value}")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		).andDo(print());

		// Then
		resultActions.andExpect(status().isOk())
			.andDo(
				document(
					"users/signup",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					requestHeaders(
						headerWithName("Authorization").description("jwt 액세스 토큰")
					),
					requestFields(
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
						fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL")
					),
					responseFields(
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
						fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
							.description("프로필 이미지 URL")
					)
				)
			);
	}

	@DisplayName("유저 리스트 가져오기")
	@Test
	public void getUserList() throws Exception {
		// Given
		final SearchUserResponse response1 = SearchUserResponse.builder()
			.id(1L)
			.nickname("abd")
			.profileImageUrl("www.dummy.com")
			.build();

		final SearchUserResponse response2 = SearchUserResponse.builder()
			.id(2L)
			.nickname("abcd")
			.profileImageUrl("www.dummy.com")
			.build();

		final SearchUserResponse response3 = SearchUserResponse.builder()
			.id(3L)
			.nickname("abcde")
			.profileImageUrl("www.dummy.com")
			.build();

		List<SearchUserResponse> responses = new ArrayList<>();
		responses.add(response1);
		responses.add(response2);
		responses.add(response3);

		given(userService.getUsersByKeyword(anyString(), anyString(), anyInt())).willReturn(responses);

		// When
		final ResultActions resultActions = mockMvc.perform(
			get("/api/v1/users")
				.header("Authorization", "Bearer ${token_value}")
				.param("keyword", "ab")
				.param("lastNickname", "abc")
				.param("size", "5")
		).andDo(print());

		// Then
		resultActions.andExpect(status().isOk())
			.andDo(
				document(
					"users/get-users",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					requestHeaders(
						headerWithName("Authorization").description("jwt 액세스 토큰")
					),
					requestParameters(
						parameterWithName("keyword").description("검색어"),
						parameterWithName("lastNickname").description("마지막 유저 닉네임"),
						parameterWithName("size").description("페이지 사이즈")
					),
					responseFields(
						fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("유저 아이디"),
						fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("닉네임"),
						fieldWithPath("[].profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL").optional()
					)
				)
			);
	}

	@DisplayName("올바르지 못한 닉네임으로 회원가입하는 경우")
	@Test
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
		).andDo(print());

		// Then
		resultActions.andExpect(status().isBadRequest())
			.andDo(print());
	}
}
