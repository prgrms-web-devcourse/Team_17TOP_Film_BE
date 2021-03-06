package com.programmers.film.api.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.film.api.post.dto.common.AuthorityImageDto;
import com.programmers.film.api.post.dto.common.PointDto;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.service.MyPageService;
import com.programmers.film.domain.post.domain.PostStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = MyPageController.class)
@WebMvcTest(MyPageController.class)
public class MyPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private MyPageService myPageService;

    @DisplayName("??????????????? ????????? ??????")
    @Test
    void previewPost() throws Exception {
        List<PreviewPostResponse> response = new ArrayList<>();

        // Given
        List<AuthorityImageDto> authorityImageDtoList = new ArrayList<>();
        authorityImageDtoList.add(AuthorityImageDto.builder()
            .imageOrder(0)
            .authorityId(1L)
            .imageUrl("testURL.com")
            .build());
        PreviewPostResponse previewPostResponse = PreviewPostResponse.builder()
            .postId(1L)
            .title("test title")
            .previewText("test preview text")
            .authorNickname("nickname")
            .availableAt(LocalDate.now())
            .state(PostStatus.OPENABLE.toString())
            .location(
                PointDto.builder()
                    .longitude("13.4445")
                    .latitude("414.1313")
                    .build()
            )
            .authorityCount(authorityImageDtoList.size())
            .authorityImageList(authorityImageDtoList)
            .build();
        response.add(previewPostResponse);

        given(myPageService.getMyPosts(any())).willReturn(response);

        // When
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/mypage/posts")
                .header("Authorization", "Bearer ${token_value}")
        );

        // Then
        resultActions.andExpect((status().isOk()))
            .andDo(print())
            .andDo(document(
                    "mypage/posts",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(headerWithName("Authorization").description("jwt ????????? ??????")),
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("????????? ?????????"),
                        fieldWithPath("[].postId").type(JsonFieldType.NUMBER).description("????????? ID"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("[].previewText").type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                        fieldWithPath("[].authorNickname").type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                        fieldWithPath("[].availableAt").type(JsonFieldType.STRING)
                            .description("??? ??? ?????? ??????"),
                        fieldWithPath("[].state").type(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("[].location").type(JsonFieldType.OBJECT).description("??????"),
                        fieldWithPath("[].location.latitude").type(JsonFieldType.STRING)
                            .description("??????"),
                        fieldWithPath("[].location.longitude").type(JsonFieldType.STRING)
                            .description("??????"),
                        fieldWithPath("[].authorityCount").type(JsonFieldType.NUMBER)
                            .description("???????????? ?????? ???"),
                        fieldWithPath("[].authorityImageList").type(JsonFieldType.ARRAY)
                            .description("???????????? ?????? ?????????"),
                        fieldWithPath("[].authorityImageList.[].imageOrder").type(JsonFieldType.NUMBER)
                            .description("????????? ??????"),
                        fieldWithPath("[].authorityImageList.[].authorityId").type(
                            JsonFieldType.NUMBER).description("????????? ID"),
                        fieldWithPath("[].authorityImageList.[].imageUrl").type(JsonFieldType.STRING)
                            .description("????????? ????????? ??????")
                    )
                )
            );

    }

}
