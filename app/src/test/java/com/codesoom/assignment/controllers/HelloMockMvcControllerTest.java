package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
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
class HelloMockMvcControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("서버가 정상적으로 작동하면 200 상태코드, OK 상태를 응답한다")
    @Test
    void health_check() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
