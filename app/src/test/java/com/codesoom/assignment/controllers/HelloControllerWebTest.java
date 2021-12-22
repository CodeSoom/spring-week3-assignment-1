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
    private MockMvc mockMvc;

    @Nested
    @DisplayName("/로 GET 요청을 보내면")
    class Describe_sayHello {


        @Test
        @DisplayName("200 OK이 돌아온다.")
        void it_returns_200_OK() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk());
        }

    }

}
