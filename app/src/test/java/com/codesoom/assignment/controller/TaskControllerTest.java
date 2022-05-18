package com.codesoom.assignment.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DisplayName("TaskController Web Test")
class TaskControllerTest {

    private MockMvc mockMvc;

    public TaskControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Nested
    @DisplayName("GET /tasks")
    class list {

        @Nested
        @DisplayName("할일 목록이 존재하면")
        class when_list_is_not_empty {

            @Test
            @DisplayName("할일 목록과 200 OK를 반환합니다.")
            void list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk());
            }
        }
    }
}