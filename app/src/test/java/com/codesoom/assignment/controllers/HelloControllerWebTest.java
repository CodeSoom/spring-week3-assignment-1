package com.codesoom.assignment.controllers;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("HelloController Web Layer")
@WebMvcTest(HelloController.class)
public class HelloControllerWebTest {
    @Autowired
    private MockMvc mockMvc;
    private final String helloControllerPath = "/hello";

    @Nested
    @DisplayName("루트(/) 경로는")
    class Describe_root_path {
        private final String rootPath = "/";

        @Nested
        @DisplayName("GET 요청을 받는다면")
        class Context_with_get_request {
            private final ResultActions getRequest;
            private final String helloWorld = "Hello, world!";

            Context_with_get_request() throws Exception {
                this.getRequest = mockMvc.perform(get(helloControllerPath + rootPath));
            }

            @Test
            @DisplayName("상태코드 200 OK 로 응답한다.")
            void it_responses_200_OK() throws Exception {
                getRequest.andExpect(status().isOk());
            }

            @Test
            @DisplayName(helloWorld + " 문자열을 리턴한다.")
            void it_returns_string() throws Exception {
                getRequest.andExpect(content().string(containsString(helloWorld)));
            }
        }
    }
}
