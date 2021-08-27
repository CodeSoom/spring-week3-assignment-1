package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("HelloController 클래스")
public class HelloControllerWebTest {
    @Autowired
    MockMvc mockMvc;

    @Nested
    @DisplayName("sayHello 메소드는")
    class Describe_sayHello {

        @Nested
        @DisplayName("path = / 일 때")
        class Context_path_is_slash {

            @Test
            @DisplayName("OK(200)을 응답합니다")
            void it_return_sayHello_200_OK() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(status().isOk());
            }
        }
    }
}
