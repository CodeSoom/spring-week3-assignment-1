package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class)
@DisplayName("TaskController 클래스의")
class TaskControllerWebTest {
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(taskService)).build();
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("빈 작업 목록이 주어졌을 때")
        class Context_with_empty_list {
            @Test
            @DisplayName("빈 배열과 상태코드 200을 보낸다")
            void It_return_empty_list_and_status_ok() throws Exception {
                given(taskService.getTasks()).willReturn(new ArrayList<>());

                mockMvc.perform(get("/tasks"))
                        .andExpect(jsonPath("$").isEmpty())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("작업을 가진 목록이 주어지면")
        class Context_with_list {
            @Test
            @DisplayName("작업 목록과 상태코드 200을 보낸다")
            void It_returns_list_and_ok() throws Exception {
                given(taskService.getTasks())
                        .willReturn(Arrays.asList(
                                new Task(0L, "Todo1"),
                                new Task(1L, "Todo2")));

                mockMvc.perform(get("/tasks"))
                        .andExpect(jsonPath("$.[0].title").value("Todo1"))
                        .andExpect(jsonPath("$.[1].title").value("Todo2"))
                        .andExpect(status().isOk());
            }
        }
    }
}
