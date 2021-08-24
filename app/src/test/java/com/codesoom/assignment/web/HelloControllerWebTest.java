package com.codesoom.assignment.web;

import com.codesoom.assignment.controllers.HelloController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(HelloController.class)
class HelloControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Hello, World! MockMvc 테스트")
    void sayHelloMockMvc() throws Exception {
        // when
        mockMvc.perform(get("/"))

        // then
        .andExpect(content().string("Hello, World!"));
    }
}
