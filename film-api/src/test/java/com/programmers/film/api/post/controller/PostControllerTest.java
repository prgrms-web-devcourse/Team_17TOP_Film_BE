package com.programmers.film.api.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.film.api.post.converter.PointConverter;
import com.programmers.film.api.post.dto.common.AuthorityImageDto;
import com.programmers.film.api.post.dto.common.SimplePostDto;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.GetMapResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.service.MapService;
import com.programmers.film.api.post.service.PostService;
import com.programmers.film.domain.common.domain.Point;
import com.programmers.film.domain.post.domain.PostStatus;
import com.programmers.film.img.S3Service;
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
@ContextConfiguration(classes = PostController.class)
@WebMvcTest(PostControllerTest.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    S3Service s3Service;

    @DisplayName("게시물 엿보기")
    @Test
    void previewPost() throws Exception {
        // Given
        PointConverter pointConverter = new PointConverter();
        List<AuthorityImageDto> authorityImageDtoList = new ArrayList<>();
        authorityImageDtoList.add(AuthorityImageDto.builder()
                .imageOrder(0)
                .authorityId(1L)
                .imageUrl("testURL.com")
            .build());
        PreviewPostResponse response = PreviewPostResponse.builder()
            .postId(1L)
            .title("test title")
            .previewText("test preview text")
            .authorNickname("nickname")
            .availableAt(LocalDate.now())
            .state(PostStatus.OPENABLE.toString())
            .location(pointConverter.doublePointToStringPoint(new Point(37.491837217869616,127.02959879978368)))
            .authorityCount(authorityImageDtoList.size())
            .authorityImageList(authorityImageDtoList)
            .build();
        given(postService.getPreview(any(), any())).willReturn(response);
        // When
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/posts/{postId}",1)
                .header("Authorization", "Bearer ${token_value}")
        ).andDo(print());
        // Then
        resultActions.andExpect(status().isOk())
            .andDo(
                document(
                    "posts/get-preview",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("jwt 액세스 토큰")
                    ),
                    responseFields(
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("postId"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
                        fieldWithPath("previewText").type(JsonFieldType.STRING).description("previewText"),
                        fieldWithPath("authorNickname").type(JsonFieldType.STRING).description("authorNickname"),
                        fieldWithPath("availableAt").type(JsonFieldType.STRING).description("availableAt"),
                        fieldWithPath("state").type(JsonFieldType.STRING).description("state"),
                        fieldWithPath("location").type(JsonFieldType.OBJECT).description("location"),
                        fieldWithPath("location.latitude").type(JsonFieldType.STRING).description("latitude"),
                        fieldWithPath("location.longitude").type(JsonFieldType.STRING).description("longitude"),
                        fieldWithPath("authorityCount").type(JsonFieldType.NUMBER).description("authorityCount"),
                        fieldWithPath("authorityImageList").type(JsonFieldType.ARRAY).description("authorityImageList"),
                        fieldWithPath("authorityImageList.[].imageOrder").type(JsonFieldType.NUMBER).description("imageOrder"),
                        fieldWithPath("authorityImageList.[].authorityId").type(JsonFieldType.NUMBER).description("authorityId"),
                        fieldWithPath("authorityImageList.[].imageUrl").type(JsonFieldType.STRING).description("imageUrl")
                    )
                )
            );
    }

    @DisplayName("게시물 삭제")
    @Test
    void deletePost() throws Exception {
        // Given
        DeletePostResponse response = DeletePostResponse.builder().postId(1L).build();
        given(postService.removePost(any(), any())).willReturn(response);

        // When
        ResultActions resultActions = mockMvc.perform(
            delete("/api/v1/posts/{postId}",1)
                .header("Authorization", "Bearer ${token_value}")
        ).andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
            .andDo(
                document(
                    "posts/delete-posts",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("jwt 액세스 토큰")
                    ),
                    responseFields(
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("postId")
                    )
                )
            );
    }
}