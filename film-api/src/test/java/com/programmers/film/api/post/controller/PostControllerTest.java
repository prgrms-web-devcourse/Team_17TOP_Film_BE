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
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
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
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시물 ID"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                        fieldWithPath("previewText").type(JsonFieldType.STRING).description("엿보기 문구"),
                        fieldWithPath("authorNickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                        fieldWithPath("availableAt").type(JsonFieldType.STRING).description("열 수 있는 시간"),
                        fieldWithPath("state").type(JsonFieldType.STRING).description("게시물 상태"),
                        fieldWithPath("location").type(JsonFieldType.OBJECT).description("위치"),
                        fieldWithPath("location.latitude").type(JsonFieldType.STRING).description("위도"),
                        fieldWithPath("location.longitude").type(JsonFieldType.STRING).description("경도"),
                        fieldWithPath("authorityCount").type(JsonFieldType.NUMBER).description("열람가능 인원 수"),
                        fieldWithPath("authorityImageList").type(JsonFieldType.ARRAY).description("열람가능 인원 리스트"),
                        fieldWithPath("authorityImageList.[].imageOrder").type(JsonFieldType.NUMBER).description("이미지 순서"),
                        fieldWithPath("authorityImageList.[].authorityId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                        fieldWithPath("authorityImageList.[].imageUrl").type(JsonFieldType.STRING).description("사용자 프로필 사진")
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
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시물 ID")
                    )
                )
            );
    }
}
