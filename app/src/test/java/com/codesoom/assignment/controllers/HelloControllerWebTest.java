package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
public class HelloControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sayHelloWithValidRequest() throws Exception {
        this.mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Hello, World!")));
    }

    @Test
    void sayHelloWithInvalidRequest() throws Exception {
        this.mockMvc.perform(get("/invalid"))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().string(""));
    }

    @Test
    void sayHelloWithInvalidMethod() throws Exception {
        this.mockMvc.perform(post("/"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed())
            .andExpect(content().string(""));

        this.mockMvc.perform(put("/"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed())
            .andExpect(content().string(""));

        this.mockMvc.perform(patch("/"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed())
            .andExpect(content().string(""));

        this.mockMvc.perform(delete("/"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed())
            .andExpect(content().string(""));
    }
}
