package com.programmers.film.api.post.controller;

import static org.junit.jupiter.api.Assertions.*;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.film.api.post.converter.PointConverter;
import com.programmers.film.api.post.dto.common.SimplePostDto;
import com.programmers.film.api.post.dto.response.GetMapResponse;
import com.programmers.film.api.post.service.MapService;
import com.programmers.film.domain.common.domain.Point;
import com.programmers.film.domain.post.domain.PostStatus;
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
@ContextConfiguration(classes = MapController.class)
@WebMvcTest(MapControllerTest.class)
class MapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private MapService mapService;

    @DisplayName("홈화면 지도 데이터 전송")
    @Test
    void getMap() throws Exception {
        // Given
        PointConverter pointConverter = new PointConverter();
        SimplePostDto simplePostDto =
            new SimplePostDto(
                1L,
                PostStatus.OPENABLE.toString(),
                pointConverter.doublePointToStringPoint(new Point(37.491837217869616,127.02959879978368))
            );
        List<SimplePostDto> list = new ArrayList<>();
        list.add(simplePostDto);
        GetMapResponse response = new GetMapResponse(list);
        given(mapService.getMapData(any())).willReturn(response);

        // When
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/maps")
                .header("Authorization", "Bearer ${token_value}")
        ).andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
            .andDo(
                document(
                    "maps/get-maps",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("jwt 액세스 토큰")
                    ),
                    responseFields(
                        fieldWithPath("posts").type(JsonFieldType.ARRAY).description("게시물 리스트"),
                        fieldWithPath("posts.[].postId").type(JsonFieldType.NUMBER).description("게시물 ID"),
                        fieldWithPath("posts.[].state").type(JsonFieldType.STRING).description("게시물 상태"),
                        fieldWithPath("posts.[].location").type(JsonFieldType.OBJECT).description("위치"),
                        fieldWithPath("posts.[].location.latitude").type(JsonFieldType.STRING).description("위도"),
                        fieldWithPath("posts.[].location.longitude").type(JsonFieldType.STRING).description("경도")
                    )
                )
            );
    }
}
