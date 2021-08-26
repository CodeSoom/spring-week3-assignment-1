package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 100L;
    private static final String TASK_TITLE = "my first task";

    private final Task taskFixture = new Task(VALID_ID, TASK_TITLE);
    private final TaskNotFoundException taskNotFoundException = new TaskNotFoundException(INVALID_ID);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("Get Request")
    class GetRequestTest {
        @BeforeEach
        void setup() {
            List<Task> tasks = new ArrayList<>();
            tasks.add(taskFixture);

            given(taskService.getTasks()).willReturn(tasks);
            given(taskService.getTask(VALID_ID)).willReturn(taskFixture);
            given(taskService.getTask(eq(INVALID_ID))).willThrow(taskNotFoundException);
        }

        @Nested
        @DisplayName("without path variables")
        class WithoutPathVariables {
            @Test
            @DisplayName("returns a task list with http status code 200")
            void list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TASK_TITLE)));
            }
        }

        @Nested
        @DisplayName("with a valid test id")
        class WithValidId {
            @Test
            @DisplayName("returns the task with http status code 200")
            void detail() throws Exception {
                mockMvc.perform(get("/tasks/" + VALID_ID))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TASK_TITLE)));
            }
        }

        @Nested
        @DisplayName("with an invalid test id")
        class WithInvalidId {
            @Test
            @DisplayName("returns a 404 error")
            void detail() throws Exception {
                mockMvc.perform(get("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }


    @Nested
    @DisplayName("Post Request")
    class PostRequestTest {
        @BeforeEach
        void setup() {
            given(taskService.createTask(any(Task.class))).willReturn(taskFixture);
        }

        @Test
        @DisplayName("returns a created task with HTTP status code 201")
        void create() throws Exception {
            String content = objectMapper.writeValueAsString(taskFixture);

            mockMvc.perform(post("/tasks")
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(TASK_TITLE)))
                    .andExpect(content().string(containsString(VALID_ID.toString())));
        }
    }

    @Nested
    @DisplayName("Put Request")
    class PutRequestTest {
        @BeforeEach
        void setup() {
            given(taskService.updateTask(eq(VALID_ID), any(Task.class))).willReturn(taskFixture);
            given(taskService.updateTask(eq(INVALID_ID), any(Task.class))).willThrow(taskNotFoundException);
        }

        @Nested
        @DisplayName("with a valid id")
        class WithValidId {
            @Test
            @DisplayName("returns an updated task with HTTP status code 200")
            void update() throws Exception {
                String content = objectMapper.writeValueAsString(taskFixture);

                mockMvc.perform(put("/tasks/" + VALID_ID)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(VALID_ID.toString())));
            }
        }

        @Nested
        @DisplayName("with an invalid id")
        class WithInvalidId {
            @Test
            @DisplayName("returns a 404 error")
            void update() throws Exception {
                String content = objectMapper.writeValueAsString(taskFixture);

                mockMvc.perform(put("/tasks/" + INVALID_ID)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("Delete Request")
    class DeleteRequestTest {
        @BeforeEach
        void setup() {
            given(taskService.deleteTask(VALID_ID)).willReturn(taskFixture);
            given(taskService.deleteTask(eq(INVALID_ID))).willThrow(taskNotFoundException);
        }

        @Nested
        @DisplayName("with a valid id")
        class WithValidId {
            @Test
            @DisplayName("returns a deleted task with HTTP status code 204")
            void remove() throws Exception {
                mockMvc.perform(delete("/tasks/" + VALID_ID))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("with an invalid id")
        class WithInvalidId {
            @Test
            @DisplayName("returns a 404 error")
            void remove() throws Exception {
                mockMvc.perform(delete("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
