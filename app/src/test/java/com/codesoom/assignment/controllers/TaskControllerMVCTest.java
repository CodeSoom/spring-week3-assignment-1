package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("TaskController 테스트")
class TaskControllerMVCTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final String TASK_TITLE = "test";
    private List<Task> tasks;
    private Task task;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();

        task = new Task();
        task.setId(1L);
        task.setTitle(TASK_TITLE);
    }

    @Nested
    @DisplayName("GET /tasks가 호출되면")
    class Describe_get {

        @Nested
        @DisplayName("할 일이 목록에 있는 경우")
        class Context_with_tasks {

            @BeforeEach
            void setUp() {
                tasks.add(task);

                given(taskService.getTasks())
                        .willReturn(tasks);
            }

            @Test
            @DisplayName("200코드와 할 일 목록을 응답한다")
            void it_responses_200_and_tasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(content().string(containsString(TASK_TITLE)))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("할 일이 목록에 없는 경우")
        class Context_without_tasks {

            @Test
            @DisplayName("200코드와 빈 목록을 응답한다")
            void it_responses_200_and_empty_list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(content().string(containsString("[]")))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("GET /tasks/{id} 가 호출되면")
    class Describe_get_task {
        @Nested
        @DisplayName("할 일이 목록에 있는 경우")
        class Context_with_task {

            @Test
            @DisplayName("200코드와 할 일을 응답한다")
            void it_responses_200_and_task() throws Exception {
                //TODO

            }
        }
    }

    @Test
    void detail() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void patch() {
    }

    @Test
    void delete() {
    }
}
