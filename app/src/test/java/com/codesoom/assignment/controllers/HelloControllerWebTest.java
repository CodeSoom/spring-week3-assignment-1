package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DisplayName("HelloController에")
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Nested
    @DisplayName("'/' URL로")
    class Describe_root_path {

        @Nested
        @DisplayName("만약 GET 요청을 하면")
        class Context_of_get {
            private ResultActions performGetRoot;

            @BeforeEach
            void request_get() throws Exception {
                performGetRoot = mockMvc.perform(get("/"));
            }

            @Test
            @DisplayName("200 OK 상태 코드와 인사말을 응답한다")
            void It_returns_ok_with_greeting() throws Exception {
                performGetRoot
                        .andExpect(
                                content().string(
                                        containsString("Hello, world!")));
            }
        }
    }
}
