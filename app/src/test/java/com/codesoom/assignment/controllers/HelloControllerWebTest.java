package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    private final String helloWorld = "Hello, world!";

    @Nested
    @DisplayName("요청을 받은 HelloController는")
    class Describe_TaskController_with_request {
        @Nested
        @DisplayName("요청이 GET / 이면,")
        class Context_when_request_is_get_only_slash {
            private RequestBuilder requestBuilder;

            @BeforeEach
            void setRequest() {
                requestBuilder = get("/");
            }

            @Test
            @DisplayName("status 200 Ok와 hello world를 응답한다.")
            void it_respond_200_ok_and_hello_world() throws Exception {
                mockMvc.perform(requestBuilder)
                        .andExpect(status().isOk())
                        .andExpect(content().string(helloWorld));
            }
        }
    }
}
