package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("HelloController 클래스 - WebTest")
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("GET - '/'는")
    @Nested
    class Describe_sayHello {

        @DisplayName("항상")
        @Nested
        class Context_always {

            @DisplayName("HTTP status ok를 반환한다.")
            @Test
            void it_returns_status_ok() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(status().isOk());
            }
        }
    }
}
