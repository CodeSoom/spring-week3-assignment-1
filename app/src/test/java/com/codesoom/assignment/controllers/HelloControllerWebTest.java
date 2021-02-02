package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO:
// 1. 정상적으로 맵핑 된 주소로 호출을 했을 때
// 2. 정상적이지 않은 주소로 호출을 했을 때
// 3. get, post, put, delete 메서드를 이용하여 호출 하였을 때

@WebMvcTest(HelloController.class)
public class HelloControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sayHelloWithRightUrl() throws Exception {
        this.mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Hello, World!")));
    }

    @Test
    void sayHelloWithWrongUrl() throws Exception {
        this.mockMvc.perform(get("/wrongUrl"))
            .andExpect(status().isNotFound());
    }

    @Test
    void sayHelloWithGetMethod() throws Exception {
        this.mockMvc.perform(get("/"))
            .andExpect(status().isOk());
    }

    @Test
    void sayHelloWithPostMethod() throws Exception {
        this.mockMvc.perform(post("/"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void sayHelloWithPutMethod() throws Exception {
        this.mockMvc.perform(put("/"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void sayHelloWithPatchMethod() throws Exception {
        this.mockMvc.perform(post("/"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void sayHelloWithDeleteMethod() throws Exception {
        this.mockMvc.perform(delete("/"))
            .andExpect(status().isMethodNotAllowed());
    }
}
