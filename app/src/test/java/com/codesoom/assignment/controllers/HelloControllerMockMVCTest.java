package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerMockMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Say Hello method")
    class Describe_say_hello {

        private String hello = "Hello, world!!!";

        @Nested
        @DisplayName("when mockmvc performs a get request to /")
        class Context_default {
            @Test
            @DisplayName("returns Hello, world!!!")
            void it_returns_a_valid_complex() throws Exception {
                mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("Hello, world!!!")));
            }
        }
    }
}
