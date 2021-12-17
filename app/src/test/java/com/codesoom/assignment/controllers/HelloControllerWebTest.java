package com.codesoom.assignment.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@DisplayName("HelloController 클래스")
public class HelloControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("sayHello 메소드는")
    class Describe_sayHello {

        @Nested
        @DisplayName("path = / 일 때")
        class Context_with_root_path{
            @Test
            @DisplayName("OK(200)을 응답합니다.")
            void it_returns_200_OK() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(status().isOk());
            }
        }
    }

}
