package com.programmers.film.api.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.film.api.post.dto.common.AuthorityImageDto;
import com.programmers.film.api.post.dto.common.OrderImageUrlDto;
import com.programmers.film.api.post.dto.common.PointDto;
import com.programmers.film.api.post.dto.common.SimpleAuthorityDto;
import com.programmers.film.api.post.dto.common.SimpleFixAuthorityDto;
import com.programmers.film.api.post.dto.request.FixPostAuthorityRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.FixPostAuthorityResponse;
import com.programmers.film.api.post.dto.response.GetPostDetailResponse;
import com.programmers.film.api.post.service.PostService;
import com.programmers.film.img.S3Service;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import com.programmers.film.api.post.converter.PointConverter;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;

import com.programmers.film.domain.common.domain.Point;
import com.programmers.film.domain.post.domain.PostStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = PostController.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private S3Service s3Service;

    @DisplayName("??????")
    @Test
    public void createPost() throws Exception {
        final String req = "{\n"
            + " \"title\": \"TEST ??????\",\n"
            + " \"previewText\": \"???????????? TEST\",\n"
            + " \"latitude\": \"31.12312\",\n"
            + " \"longitude\": \"144.4444\",\n"
            + " \"availableAt\": \"2021-12-31\",\n"
            + " \"content\": \"?????? TEST\"\n"
            + "}\n";

        final AuthorityImageDto dto = AuthorityImageDto.builder()
            .imageOrder(0)
            .authorityId(1L)
            .imageUrl("testUrl.com")
            .build();

        final List<AuthorityImageDto> authorityImages = new ArrayList<>();
        authorityImages.add(dto);

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain",
            "hello file".getBytes());

        final CreatePostResponse response = CreatePostResponse.builder()
            .postId(1L)
            .title("TEST ??????")
            .previewText("???????????? TEST")
            .availableAt(LocalDate.parse("2021-12-31", DateTimeFormatter.ISO_DATE))
            .state("0")
            .location(PointDto.builder()
                .latitude("31.12312")
                .longitude("144.4444")
                .build())
            .authorityCount(1)
            .authorityImageList(authorityImages)
            .build();

        given(postService.createPost(any(), any())).willReturn(response);

        final ResultActions resultActions = mockMvc.perform(
            multipart("/api/v1/posts")
                .part(new MockPart("files", "files".getBytes(StandardCharsets.UTF_8)))
                .part(new MockPart("com", req.getBytes(StandardCharsets.UTF_8)))
                .header("Authorization", "Bearer ${token_value}")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        resultActions.andExpect(status().isCreated())
            .andDo(print())
            .andDo(document(
                "posts/create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("jwt ????????? ??????")
                ),
                requestParts(
                    partWithName("com").
                        description("json???????????? string"),
                    partWithName("files").description("?????? ????????? ?????????")
                )
                ,
                responseFields(
                    fieldWithPath("postId").type(JsonFieldType.NUMBER).description("????????? ID"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("previewText").type(JsonFieldType.STRING)
                        .description("????????? ??????"),
                    fieldWithPath("availableAt").type(JsonFieldType.STRING)
                        .description("?????? ?????? ???"),
                    fieldWithPath("state").type(JsonFieldType.STRING)
                        .description("????????? ??????"),
                    fieldWithPath("location").type(JsonFieldType.OBJECT).description("??????"),
                    fieldWithPath("location.latitude").type(JsonFieldType.STRING)
                        .description("??????"),
                    fieldWithPath("location.longitude").type(JsonFieldType.STRING)
                        .description("??????"),
                    fieldWithPath("authorityImageList").type(JsonFieldType.ARRAY)
                        .description("???????????? ?????? ?????????"),
                    fieldWithPath("authorityImageList.[].imageOrder").type(JsonFieldType.NUMBER)
                        .description("????????? ??????"),
                    fieldWithPath("authorityImageList.[].authorityId").type(
                        JsonFieldType.NUMBER).description("????????? ID"),
                    fieldWithPath("authorityImageList.[].imageUrl").type(JsonFieldType.STRING)
                        .description("????????? ????????? ??????"),
                    fieldWithPath("authorityCount").type(JsonFieldType.NUMBER)
                        .description("?????? ?????? ???")
                )

            ));
    }


    @DisplayName("????????? ??????")
    @Test
    public void getPostdetail() throws Exception {
        //Given
        List<AuthorityImageDto> authorityImageDtoList = new ArrayList<>();
        authorityImageDtoList.add(AuthorityImageDto.builder()
            .imageOrder(0)
            .authorityId(1L)
            .imageUrl("testURL.com")
            .build());
        List<OrderImageUrlDto> imgUrls = new ArrayList<>();
        imgUrls.add(OrderImageUrlDto.builder()
            .imageOrder(0)
            .imageUrl("testURL.com")
            .build());

        GetPostDetailResponse response = GetPostDetailResponse.builder()
            .postId(1L)
            .title("TEST ??????")
            .previewText("???????????? TEST")
            .authorNickname("test man")
            .authorImageUrl("testUrl.com")
            .content("Test ??????")
            .imageUrls(imgUrls)
            .createdAt(LocalDate.now())
            .authorityImageList(authorityImageDtoList)
            .location(PointDto.builder()
                .latitude("31.12312")
                .longitude("144.4444")
                .build())
            .openerNickname("test man")
            .isOpened(true)
            .openedAt(LocalDate.now())
            .openerImageUrl("testUrl.com")
            .availableAt(LocalDate.now())
            .build();

        given(postService.getPostDetail(any(), any())).willReturn(response);

        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/posts/detail/{postId}", 1)
                .header("Authorization", "Bearer ${token_value}")
        );

        // Then

        resultActions.andExpect((status().isOk()))
            .andDo(print())
            .andDo(
                document("posts/get-detail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("postId").description("????????? ID")
                    ),
                    requestHeaders(headerWithName("Authorization").description("jwt ????????? ??????"))
                    ,
                    responseFields(
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("????????? ID"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("imageUrls").type(JsonFieldType.ARRAY)
                            .description("????????? ????????? url ?????????"),
                        fieldWithPath("imageUrls.[].imageOrder").type(JsonFieldType.NUMBER)
                            .description("????????? ????????? ??????"),
                        fieldWithPath("imageUrls.[].imageUrl").type(JsonFieldType.STRING)
                            .description("????????? ????????? url"),
                        fieldWithPath("previewText").type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                        fieldWithPath("authorNickname").type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                        fieldWithPath("authorImageUrl").type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                        fieldWithPath("createdAt").type(JsonFieldType.STRING)
                            .description("????????? ?????? ??????"),
                        fieldWithPath("location").type(JsonFieldType.OBJECT).description("??????"),
                        fieldWithPath("location.latitude").type(JsonFieldType.STRING)
                            .description("??????"),
                        fieldWithPath("location.longitude").type(JsonFieldType.STRING)
                            .description("??????"),
                        fieldWithPath("authorityImageList").type(JsonFieldType.ARRAY)
                            .description("???????????? ?????? ??????"),
                        fieldWithPath("authorityImageList.[].imageOrder").type(JsonFieldType.NUMBER)
                            .description("?????? ?????? ?????? ??????"),
                        fieldWithPath("authorityImageList.[].authorityId").type(
                            JsonFieldType.NUMBER).description("????????? ID"),
                        fieldWithPath("authorityImageList.[].imageUrl").type(JsonFieldType.STRING)
                            .description("????????? ????????? ????????? ??????"),
                        fieldWithPath("openerNickname").type(JsonFieldType.STRING)
                            .description("?????? ????????? ?????????"),
                        fieldWithPath("openerImageUrl").type(JsonFieldType.STRING)
                            .description("?????? ????????? ????????? ?????????"),
                        fieldWithPath("isOpened").type(JsonFieldType.BOOLEAN)
                            .description("????????? ?????? ?????? ?????? true = ??????, false = ???????????????"),
                        fieldWithPath("openedAt").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("availableAt").type(JsonFieldType.STRING)
                            .description("???????????? ??????")
                    )
                )
            );
    }

    @DisplayName("????????? ?????????")
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
            .location(pointConverter.doublePointToStringPoint(
                new Point(37.491837217869616, 127.02959879978368)))
            .authorityCount(authorityImageDtoList.size())
            .authorityImageList(authorityImageDtoList)
            .build();
        given(postService.getPreview(any(), any())).willReturn(response);
        // When
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/posts/{postId}", 1)
                .header("Authorization", "Bearer ${token_value}")
        ).andDo(
            document("posts/get-preview",
                preprocessRequest(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("????????? ID")
                ),
                requestHeaders(headerWithName("Authorization").description("jwt ????????? ??????"))
            )
        );
        // Then
        resultActions.andExpect(status().isOk())
            .andDo(
                document(
                    "posts/get-preview",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("????????? ID"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("previewText").type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                        fieldWithPath("authorNickname").type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                        fieldWithPath("availableAt").type(JsonFieldType.STRING)
                            .description("??? ??? ?????? ??????"),
                        fieldWithPath("state").type(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("location").type(JsonFieldType.OBJECT).description("??????"),
                        fieldWithPath("location.latitude").type(JsonFieldType.STRING)
                            .description("??????"),
                        fieldWithPath("location.longitude").type(JsonFieldType.STRING)
                            .description("??????"),
                        fieldWithPath("authorityCount").type(JsonFieldType.NUMBER)
                            .description("???????????? ?????? ???"),
                        fieldWithPath("authorityImageList").type(JsonFieldType.ARRAY)
                            .description("???????????? ?????? ??????"),
                        fieldWithPath("authorityImageList.[].imageOrder").type(JsonFieldType.NUMBER)
                            .description("???????????? ??????"),
                        fieldWithPath("authorityImageList.[].authorityId").type(
                            JsonFieldType.NUMBER).description("????????? ID"),
                        fieldWithPath("authorityImageList.[].imageUrl").type(JsonFieldType.STRING)
                            .description("????????? ????????? ????????? ??????")
                    )
                )
            );
    }

    @DisplayName("????????? ??????")
    @Test
    void deletePost() throws Exception {
        // Given
        DeletePostResponse response = DeletePostResponse.builder().postId(1L).build();
        given(postService.removePost(any(), any())).willReturn(response);

        // When
        ResultActions resultActions = mockMvc.perform(
            delete("/api/v1/posts/{postId}", 1)
                .header("Authorization", "Bearer ${token_value}")
        ).andDo(
            document("posts/delete-posts",
                preprocessRequest(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("????????? ID")
                ),
                requestHeaders(headerWithName("Authorization").description("jwt ????????? ??????"))
            )
        );

        // Then
        resultActions.andExpect(status().isOk())
            .andDo(
                document(
                    "posts/delete-posts",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("????????? ID")
                    )
                )
            );
    }

    @DisplayName("????????? ???????????? ??????")
    @Test
    void fixPostAuthority() throws Exception {
        // Given
        // set request
        List<SimpleFixAuthorityDto> fixAuthorityDtos = new ArrayList<>();
        fixAuthorityDtos.add(
            SimpleFixAuthorityDto.builder()
            .userId(2L)
            .build()
        );
        FixPostAuthorityRequest request = FixPostAuthorityRequest.builder()
                .fixAuthorityList(fixAuthorityDtos)
                .build();

        // set response
        List<SimpleAuthorityDto> authorityDtos = new ArrayList<>();
        authorityDtos.add(
            SimpleAuthorityDto.builder()
            .imageOrder(0)
            .authorityId(1L)
            .authorityNickName("testNickName1")
            .imageUrl("testUrl1.com")
            .build()
        );
        authorityDtos.add(
            SimpleAuthorityDto.builder()
                .imageOrder(1)
                .authorityId(2L)
                .authorityNickName("testNickName2")
                .imageUrl("testUrl2.com")
                .build()
        );
        FixPostAuthorityResponse response = FixPostAuthorityResponse.builder()
            .postId(1L)
            .authorityList(authorityDtos)
            .build();

        given(postService.fixPostAuthority(any(), any(), any())).willReturn(response);

        // When
        ResultActions resultActions = mockMvc.perform(
            patch("/api/v1/posts/authority/{postId}", 1)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .header("Authorization", "Bearer ${token_value}")
        ).andDo(
            document("posts/patch-authority",
                preprocessRequest(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("????????? ID")
                ),
                requestHeaders(headerWithName("Authorization").description("jwt ????????? ??????")),
                requestFields(
                    fieldWithPath("fixAuthorityList").type(JsonFieldType.ARRAY)
                        .description("?????? ?????? ??????"),
                    fieldWithPath("fixAuthorityList.[].userId").type(JsonFieldType.NUMBER)
                        .description("????????? ID")
                )
            )
        );

        // Then
        resultActions.andExpect(status().isOk())
            .andDo(
                document(
                    "posts/patch-authority",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("????????? ID"),
                        fieldWithPath("authorityList").type(JsonFieldType.ARRAY)
                            .description("???????????? ?????? ??????"),
                        fieldWithPath("authorityList.[].imageOrder").type(JsonFieldType.NUMBER)
                            .description("?????? ?????? ?????? ??????"),
                        fieldWithPath("authorityList.[].authorityId").type(JsonFieldType.NUMBER)
                            .description("????????? ID"),
                        fieldWithPath("authorityList.[].authorityNickName").type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                        fieldWithPath("authorityList.[].imageUrl").type(JsonFieldType.STRING)
                            .description("????????? ????????? ????????? ??????")
                    )
                )
            );
    }
}
