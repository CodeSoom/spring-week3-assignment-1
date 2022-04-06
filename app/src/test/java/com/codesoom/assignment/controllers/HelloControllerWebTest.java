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
class HelloControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("sayHello 메서드는")
    class Describe_sayHello {
        @Test
        @DisplayName("응답 문구로 OK를 반환합니다")
        void sayHello() throws Exception {
            mockMvc.perform(get("/")).andExpect(status().isOk());
        }
    }
}
