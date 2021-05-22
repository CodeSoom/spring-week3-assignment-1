package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("HelloController")
public class HelloControllerWebTest {
    @Autowired
    MockMvc mockMvc;


    @Nested
    @DisplayName("sayHello()는")
    class describe_sayhello {
        @Nested
        @DisplayName("정상적으로 호출이 된다면")
        class Context_valid_sayhello {
            @Test
            @DisplayName("Http Status는 200이다.")
            void sayhello_response_status_ok() throws Exception {

                ResultActions resultActions = mockMvc
                        .perform(get("/")
                                .contentType(MediaType.APPLICATION_JSON));

                resultActions
                        .andExpect(status().isOk())
                        .andDo(print());
            }

            @Test
            @DisplayName("Hello, World 값을 응답한다.")
            void sayhello_response_data() throws Exception {

                ResultActions resultActions = mockMvc
                        .perform(get("/")
                                .contentType(MediaType.APPLICATION_JSON));

                resultActions
                        .andExpect(content().string(containsString("Hello, World")))
                        .andDo(print());
            }
        }

    }
}

