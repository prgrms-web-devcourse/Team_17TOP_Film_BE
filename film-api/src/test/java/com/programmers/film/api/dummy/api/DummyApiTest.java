package com.programmers.film.api.dummy.api;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.programmers.film.domain.dummy.domain.DummyEntity;
import com.programmers.film.domain.dummy.repository.DummyRepository;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class DummyApiTest {

    @Autowired
    protected DummyRepository dummyRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        DummyEntity dummyEntity = new DummyEntity(1L, "abc");
        dummyRepository.save(dummyEntity);
    }

    @Test
    @DisplayName("dummy success test")
    public void findByIdSuccess() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("/api/v1/dummy/{dummyId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());
    }

    @Test
    @DisplayName("dummy fail test")
    public void findByIdFail() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("/api/v1/dummy/{dummyId}", 2L)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());
    }
}