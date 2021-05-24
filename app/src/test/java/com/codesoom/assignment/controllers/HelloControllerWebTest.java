package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("HelloController web")
class HelloControllerWebTest {
    private final String HELLO = "Hello, world!";

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Requested GET /")
    class AccessHttpGetHome {
        @Test
        @DisplayName("It returns the contents \"Hello, world!\", expected ok")
        void sayHello() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(HELLO));
        }
    }
}
