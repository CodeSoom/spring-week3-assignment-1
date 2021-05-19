package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("HelloController 클래스 Web")
class HelloControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("'/' URL로")
    class Describe_of_root_url {


        @Nested
        @DisplayName("만약 GET 요청을 보내면")
        class Context_of_get {

            @Test
            @DisplayName("200 OK 상태코드와 'Hello, world!' body를 응답한다")
            public void it_returns_ok_with_valid_string() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("Hello, world!")));
            }
        }
    }
}