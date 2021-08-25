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

@Nested
@DisplayName("HelloController 웹테스트")
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Get ")
    class Get {

        @Nested
        @DisplayName("path \'/\' 호출하면,")
        class Path_root {

            @Test
            @DisplayName("response(status: ok, content: hello)를 반환합니다.")
            void response_ok() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("Hello")));
            }
        }
    }
}
