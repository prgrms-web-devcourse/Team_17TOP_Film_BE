package com.programmers.film.api.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.film.api.post.dto.common.AuthorityImageDto;
import com.programmers.film.api.post.dto.common.OrderImageUrlDto;
import com.programmers.film.api.post.dto.common.PointDto;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.GetPostDetailResponse;
import com.programmers.film.api.post.service.PostService;
import com.programmers.film.img.S3Service;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;


import java.util.List;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

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

    @DisplayName("생성")
    @Test
    public void createPost() throws Exception {
        final String req = "{\n"
            + " \"title\": \"TEST 제목\",\n"
            + " \"previewText\": \"미리보기 TEST\",\n"
            + " \"latitude\": \"31.12312\",\n"
            + " \"longitude\": \"144.4444\",\n"
            + " \"availableAt\": \"2021-12-31\",\n"
            + " \"content\": \"내용 TEST\"\n"
            + "}\n";

        final AuthorityImageDto dto = AuthorityImageDto.builder()
            .imageOrder(0)
            .authorityId(1L)
            .imageUrl("testUrl.com")
            .build();

        final List<AuthorityImageDto> authorityImages = new ArrayList<>();
        authorityImages.add(dto);

        MockMultipartFile file = new MockMultipartFile("file","test.txt" , "text/plain" , "hello file".getBytes());

        final CreatePostResponse response = CreatePostResponse.builder()
            .postId(1L)
            .title("TEST 제목")
            .previewText("미리보기 TEST")
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
                    headerWithName("Authorization").description("jwt 액세스 토큰")
                ),
                requestParts(
                    partWithName("com").
                        description("json형식으로 string"),
                    partWithName("files").description("첨부 이미지 리스트")
                )
                ,
                responseFields(
                    fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시물 ID"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                    fieldWithPath("previewText").type(JsonFieldType.STRING)
                        .description("엿보기 문구"),
                    fieldWithPath("availableAt").type(JsonFieldType.STRING)
                        .description("열람 가능 날"),
                    fieldWithPath("state").type(JsonFieldType.STRING)
                        .description("게시물 상태"),
                    fieldWithPath("location").type(JsonFieldType.OBJECT).description("위치"),
                    fieldWithPath("location.latitude").type(JsonFieldType.STRING)
                        .description("위도"),
                    fieldWithPath("location.longitude").type(JsonFieldType.STRING)
                        .description("경도"),
                    fieldWithPath("authorityImageList").type(JsonFieldType.ARRAY)
                        .description("열람가능 인원 리스트"),
                    fieldWithPath("authorityImageList.[].imageOrder").type(JsonFieldType.NUMBER)
                        .description("이미지 순서"),
                    fieldWithPath("authorityImageList.[].authorityId").type(
                        JsonFieldType.NUMBER).description("사용자 ID"),
                    fieldWithPath("authorityImageList.[].imageUrl").type(JsonFieldType.STRING)
                        .description("사용자 프로필 사진"),
                    fieldWithPath("authorityCount").type(JsonFieldType.NUMBER)
                        .description("권한 인원 수")
                )

            ));
    }


    @DisplayName("게시물 보기")
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
            .title("TEST 제목")
            .previewText("미리보기 TEST")
            .authorNickname("test man")
            .authorImageUrl("testUrl.com")
            .content("Test 내용")
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
                        parameterWithName("postId").description("게시물 ID")
                    ),
                    requestHeaders(headerWithName("Authorization").description("jwt 액세스 토큰"))
                    ,
                    responseFields(
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시물 ID"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용"),
                        fieldWithPath("imageUrls").type(JsonFieldType.ARRAY)
                            .description("게시물 사진 url 리스트"),
                        fieldWithPath("imageUrls.[].imageOrder").type(JsonFieldType.NUMBER)
                            .description("게시물 사진 순서"),
                        fieldWithPath("imageUrls.[].imageUrl").type(JsonFieldType.STRING)
                            .description("게시물 사진 url"),
                        fieldWithPath("previewText").type(JsonFieldType.STRING)
                            .description("엿보기 문구"),
                        fieldWithPath("authorNickname").type(JsonFieldType.STRING)
                            .description("작성자 닉네임"),
                        fieldWithPath("authorImageUrl").type(JsonFieldType.STRING)
                            .description("작성자 사진"),
                        fieldWithPath("createdAt").type(JsonFieldType.STRING)
                            .description("게시물 생성 시간"),
                        fieldWithPath("location").type(JsonFieldType.OBJECT).description("위치"),
                        fieldWithPath("location.latitude").type(JsonFieldType.STRING)
                            .description("위도"),
                        fieldWithPath("location.longitude").type(JsonFieldType.STRING)
                            .description("경도"),
                        fieldWithPath("authorityImageList").type(JsonFieldType.ARRAY)
                            .description("열람가능 인원 리스트"),
                        fieldWithPath("authorityImageList.[].imageOrder").type(JsonFieldType.NUMBER)
                            .description("이미지 순서"),
                        fieldWithPath("authorityImageList.[].authorityId").type(
                            JsonFieldType.NUMBER).description("사용자 ID"),
                        fieldWithPath("authorityImageList.[].imageUrl").type(JsonFieldType.STRING)
                            .description("사용자 프로필 사진"),
                        fieldWithPath("openerNickname").type(JsonFieldType.STRING)
                            .description("열람자 닉네임"),
                        fieldWithPath("openerImageUrl").type(JsonFieldType.STRING)
                            .description("열람자 프로필 사진"),
                        fieldWithPath("isOpened").type(JsonFieldType.BOOLEAN)
                            .description("열림 확인 여부"),
                        fieldWithPath("openedAt").type(JsonFieldType.STRING).description("열람 시간")
                    )
                )
            );
    }
}
