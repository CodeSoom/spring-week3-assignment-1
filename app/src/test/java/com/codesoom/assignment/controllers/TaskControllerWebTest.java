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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * TaskController 에서 MockMVC를 사용하여 테스트를 진행
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    private static final String TASK_TITLE = "TaskTitle";

    @Autowired
    private MockMvc mockMvc;

    private TaskService service;
    private TaskController controller;

    @BeforeEach
    void setUp() {
        this.service = new TaskService();
        this.controller = new TaskController(this.service);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.controller)
                .setControllerAdvice(TaskErrorAdvice.class)
                .alwaysExpect(content().contentType(APPLICATION_JSON))
                .build();
    }

    @Nested
    @DisplayName("모든 Task 객체 조회 API는")
    class Describe_list_of_task {

        @Nested
        @DisplayName("Task 객체가 존재하지 않을 경우")
        class Context_with_empty_tasks {
            final long createTasksSize = 0L;

            @BeforeEach
            void setUp() {
                for (int i = 0; i < createTasksSize; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);
                    controller.create(task);
                }
            }

            @Test
            @DisplayName("빈 배열로 응답 한다")
            void it_returns_empty_list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("[]")));
            }
        }

        @Nested
        @DisplayName("Task 객체가 존재할 경우")
        class Context_with_tasks {
            final long createTasksSize = 5L;
            final String responseTask = "[{\"id\":1,\"title\":\"" + TASK_TITLE + "\"}";

            @BeforeEach
            void setUp() {
                for (int i = 0; i < createTasksSize; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);
                    service.createTask(task);
                }
            }

            @Test
            @DisplayName("Task 객체가 있는 배열로 응답한다")
            void it_returns_task_list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(responseTask)));
            }
        }
    }

    @Nested
    @DisplayName("Task 객체 상세 조회 API는")
    class Describe_detail_of_task {

        @Nested
        @DisplayName("조회하는 ID에 맞는 Task 객체가 존재하지 않을 경우")
        class Context_with_invalid_id {
            final long createTaskSize = 0;
            final long requestTaskId = 1L;

            @BeforeEach
            void setUp() {
                for (int i = 0; i < createTaskSize; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);
                    service.createTask(task);
                }
            }

            @Test
            @DisplayName("상태코드 404로 응답합니다.")
            void it_throw_taskNotFoundException() throws Exception {
                mockMvc.perform(get("/tasks/" + requestTaskId))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("조회하는 ID에 맞는 Task 객체가 존재할 경우")
        class Context_with_valid_id {
            final long createTaskSize = 3L;
            final long requestTaskId = 1L;
            final String responseTask = "{\"id\":1,\"title\":\"" + TASK_TITLE + "\"}";

            @BeforeEach
            void setUp() {
                for (int i = 0; i < createTaskSize; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);
                    service.createTask(task);
                }
            }

            @Test
            @DisplayName("상태코드 200과 Task 객체와 함께 응답합니다")
            void it_return_with_task() throws Exception {
                mockMvc.perform(get("/tasks/" + requestTaskId))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(responseTask)));

            }
        }
    }
}
