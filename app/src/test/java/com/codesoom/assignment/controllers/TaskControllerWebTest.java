package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService service;

    private final List<Task> tasks = new LinkedList<>();
    private final String TASK_TITLE = "Test Task";
    private final Long TASK_ID = 1L;
    private final int FIRST = 0;
    private final Long TASK_ID_NOT_EXISTING = 10L;
    private final String DEFAULT_PATH = "/tasks";

    @BeforeEach
    void setUp() {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
        tasks.add(task);
    }

    @AfterEach
    void clear() {
        tasks.clear();
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @BeforeEach
        void setUp() {
            given(service.getTasks()).willReturn(tasks);
        }

        @Test
        @DisplayName("HTTP Status Code 200 OK 응답한다")
        void it_responds_with_200_ok() throws Exception {
            mockMvc.perform(get(DEFAULT_PATH))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("만약 기본 생성된 Task를 상세조회한다면")
        class Context_with_default_task {
            @BeforeEach
            void setUp() {
                final Task task = tasks.get(FIRST);
                given(service.getTask(TASK_ID)).willReturn(task);
            }

            @Test
            @DisplayName("HTTP Status Code 200 OK 응답한다")
            void it_responds_with_200_ok() throws Exception {
                mockMvc.perform(get(DEFAULT_PATH + "/" + TASK_ID))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 상세조회한다면")
        class Context_without_existing_task {
            @BeforeEach
            void setUp() {
                given(service.getTask(TASK_ID_NOT_EXISTING))
                        .willThrow(new TaskNotFoundException(TASK_ID_NOT_EXISTING));
            }

            @Test
            @DisplayName("HTTP Status Code 404 NOT FOUND 응답한다")
            void it_responds_with_200_ok() throws Exception {
                mockMvc.perform(get(DEFAULT_PATH + "/" + TASK_ID_NOT_EXISTING))
                        .andExpect(status().isNotFound());
            }

        }
    }

}
