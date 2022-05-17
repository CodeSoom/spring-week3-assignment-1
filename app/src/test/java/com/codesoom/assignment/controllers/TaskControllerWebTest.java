package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private Task task;
    private static final String TASK_TITLE = "babo";
    private static final Long TASK_ID = 1L;
    private static final Long INVALID_TASK_ID = 1231231L;

    private static final String TASKS_DEFAULT_PATH = "/tasks";
    private static final String TASKS_VALID_ID_PATH = TASKS_DEFAULT_PATH + "/" + TASK_ID;
    private static final String TASKS_INVALID_ID_PATH = TASKS_DEFAULT_PATH + "/" + INVALID_TASK_ID;
    private static final String TASKS_NON_NUMBER_ID_PATH = TASKS_DEFAULT_PATH + "/" + "sadqlkwd@";

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle(TASK_TITLE);
        task.setId(TASK_ID);
    }


    @Nested
    @DisplayName("Get /tasks")
    class Describe_get_tasks {
        private List<Task> tasks;

        @BeforeEach
        void setUp() {
            tasks = new ArrayList<>();
            tasks.add(task);
            given(taskService.getTasks()).willReturn(tasks);
        }

        @DisplayName("tasks를 응답한다.")
        @Test
        void it_response_tasks() throws Exception {
            mockMvc.perform(get(TASKS_DEFAULT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(tasks.size()))
                    .andExpect(jsonPath("$[0].id").value(TASK_ID))
                    .andExpect(jsonPath("$[0].title").value(TASK_TITLE));
        }
    }

    @Nested
    @DisplayName("POST /tasks")
    class Describe_post_task {
        @BeforeEach
        void setUp() {
            given(taskService.createTask(any())).willReturn(task);
        }

        @DisplayName("201 status를 응답한다.")
        @Test
        void it_responses_201_status() throws Exception {
            mockMvc.perform(post(TASKS_DEFAULT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(taskToString(task)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(TASK_ID))
                    .andExpect(jsonPath("$.title").value(TASK_TITLE));
        }
    }

    @Nested
    @DisplayName("GET /tasks/{id}")
    class Describe_get_task_by_id {
        @Nested
        @DisplayName("id가 존재하면")
        class Context_when_id_does_exist {
            @BeforeEach
            void setUp() {
                given(taskService.getTask(TASK_ID)).willReturn(task);
            }

            @Test
            @DisplayName("task를 응답한다.")
            void it_responses_task() throws Exception {
                mockMvc.perform(get(TASKS_VALID_ID_PATH))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(TASK_ID))
                        .andExpect(jsonPath("$.title").value(TASK_TITLE));
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않으면")
        class Context_when_id_does_not_exist {
            @BeforeEach
            void setUp() {
                given(taskService.getTask(INVALID_TASK_ID))
                        .willThrow(new TaskNotFoundException(INVALID_TASK_ID));
            }

            @Test
            @DisplayName("404 status를 응답한다.")
            void it_responses_404_status() throws Exception {
                mockMvc.perform(get(TASKS_INVALID_ID_PATH))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("id가 숫자가 아니면")
        class Context_when_id_is_not_number {
            @Test
            @DisplayName("400 status를 응답한다.")
            void it_responses_400_status() throws Exception {
                mockMvc.perform(get(TASKS_NON_NUMBER_ID_PATH))
                        .andExpect(status().isBadRequest());
            }

        }
    }

    @Nested
    @DisplayName("PUT /tasks/{id}")
    class Describe_put_task_by_id {

        @Nested
        @DisplayName("id가 존재하면")
        class Context_when_id_does_exist{
            private static final String TASK_UPDATE_TITLE = "babo1";

            @BeforeEach
            void setUp() {
                task.setTitle(TASK_UPDATE_TITLE);
                given(taskService.updateTask(eq(TASK_ID), any())).willReturn(task);
            }

            @Test
            @DisplayName("변경된 task를 응답한다.")
            void it_responses_updated_task() throws Exception {
                mockMvc.perform(put(TASKS_VALID_ID_PATH)
                                .content(taskToString(task)) //
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title").value(TASK_UPDATE_TITLE))
                        .andExpect(jsonPath("$.id").value(TASK_ID));
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않으면")
        class Context_when_id_does_not_exist {
            @BeforeEach
            void setUp() {
                given(taskService.updateTask(eq(INVALID_TASK_ID), any()))
                        .willThrow(new TaskNotFoundException(INVALID_TASK_ID));
            }

            @Test
            @DisplayName("404 status를 응답한다.")
            void it_responses_404_status() throws Exception {
                mockMvc.perform(put(TASKS_INVALID_ID_PATH)
                                .content(taskToString(task)) //
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("id가 숫자가 아니면")
        class Context_when_id_is_not_number {
            @Test
            @DisplayName("400 status를 응답한다.")
            void it_responses_400_status() throws Exception {
                mockMvc.perform(put(TASKS_NON_NUMBER_ID_PATH))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{id}")
    class Describe_delete_task_by_id {
        @Nested
        @DisplayName("아이디가 존재하면")
        class Context_when_id_does_exist {
            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(TASK_ID)).willReturn(task);
            }

            @DisplayName("삭제 후 204 status를 응답한다.")
            @Test
            void it_responses_204_status() throws Exception {
                mockMvc.perform(delete(TASKS_VALID_ID_PATH))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("아이디가 존재하지 않으면")
        class Context_when_id_does_not_exist {
            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(INVALID_TASK_ID))
                        .willThrow(new TaskNotFoundException(INVALID_TASK_ID));
            }

            @Test
            @DisplayName("404 status를 응답한다.")
            void it_responses_404_status() throws Exception {
                mockMvc.perform(delete(TASKS_INVALID_ID_PATH))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("아이디가 숫자가 아니면")
        class Context_when_id_is_not_number {
            @Test
            @DisplayName("400 status를 응답한다.")
            void it_responses_400_status() throws Exception {
                mockMvc.perform(get(TASKS_NON_NUMBER_ID_PATH))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    private String taskToString(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
