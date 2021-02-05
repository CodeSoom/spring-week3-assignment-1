package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DisplayName("TaskController 클래스")
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private static final String TASK_TITLE = "test";

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;

    private List<Task> tasks;
    private Task task;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
        task = new Task();
        task.setId(EXISTING_ID);
        task.setTitle(TASK_TITLE);
    }

    @AfterEach
    void clear() {
        Mockito.reset(taskService);
    }

    @Nested
    @DisplayName("GET 요청은")
    class Describe_GET {
        @Nested
        @DisplayName("저장된 task가 있다면")
        class Context_with_tasks {
            @BeforeEach
            void setUp() {
                tasks.add(task);
                given(taskService.getTasks())
                        .willReturn(tasks);
            }

            @Test
            @DisplayName("200코드와 저장된 task를 응답한다")
            void it_replies_with_200_and_the_tasks() throws Exception {
                mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(containsString(TASK_TITLE)))
                        .andExpect(content().string(objectMapper.writeValueAsString(tasks)))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("저장된 task가 없다면")
        class Context_with_empty_tasks {
            @BeforeEach
            void setUp() {
                given(taskService.getTasks())
                        .willReturn(tasks);
            }

            @Test
            @DisplayName("200코드와 빈 task list를 응답한다")
            void it_replies_with_200_and_the_empty_tasks() throws Exception {
                mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(content().string("[]"))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_an_existing_task_id {
            @BeforeEach
            void setUp() {
                given(taskService.getTask(EXISTING_ID))
                        .willReturn(task);
            }

            @Test
            @DisplayName("200코드와 task를 응답한다")
            void it_replies_with_200_and_the_task() throws Exception {
                mockMvc.perform(get("/tasks/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_with_not_an_existing_task_id {
            @BeforeEach
            void setUp() {
                given(taskService.getTask(NOT_EXISTING_ID))
                        .willThrow(new TaskNotFoundException(NOT_EXISTING_ID));
            }

            @Test
            @DisplayName("404코드와 에러메시지를 응답한다")
            void it_replies_with_the_error_message_and_404() throws Exception {
                mockMvc.perform(get("/tasks/{id}", NOT_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("title").doesNotExist())
                        .andExpect(jsonPath("message").exists())
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST 요청은")
    class Describe_POST {
        @Nested
        @DisplayName("task가 주어진다면")
        class Context_with_task {
            @BeforeEach
            void setUp() {
                given(taskService.createTask(any(Task.class)))
                        .willReturn(task);
            }

            @Test
            @DisplayName("201코드와 생성된 task를 응답한다")
            void it_replies_with_201_and_the_created_task() throws Exception {
                mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists())
                        .andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("PUT 요청은")
    class Describe_PUT {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_an_existing_task_id {
            @BeforeEach
            void setUp() {
                given(taskService.updateTask(eq(EXISTING_ID), any(Task.class)))
                        .willReturn(task);
            }

            @Test
            @DisplayName("200코드와 수정된 task를 응답한다")
            void it_replies_with_200_and_the_updated_task() throws Exception {
                mockMvc.perform(put("/tasks/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_with_not_an_existing_task_id {
            @BeforeEach
            void setUp() {
                given(taskService.updateTask(eq(NOT_EXISTING_ID), any(Task.class)))
                        .willThrow(new TaskNotFoundException(NOT_EXISTING_ID));
            }

            @Test
            @DisplayName("404코드와 에러메시지를 응답한다")
            void it_replies_with_the_error_message_and_404() throws Exception {
                mockMvc.perform(put("/tasks/{id}", NOT_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("title").doesNotExist())
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("PATCH 요청은")
    class Describe_PATCH {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_an_existing_task_id {
            @BeforeEach
            void setUp() {
                given(taskService.updateTask(eq(EXISTING_ID), any(Task.class)))
                        .willReturn(task);
            }

            @Test
            @DisplayName("200코드와 수정된 task를 응답한다")
            void it_replies_with_200_and_the_updated_task() throws Exception {
                mockMvc.perform(patch("/tasks/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_with_not_an_existing_task_id {
            @BeforeEach
            void setUp() {
                given(taskService.updateTask(eq(NOT_EXISTING_ID), any(Task.class)))
                        .willThrow(new TaskNotFoundException(NOT_EXISTING_ID));
            }

            @Test
            @DisplayName("404코드와 에러메시지를 응답한다")
            void it_replies_with_the_error_message_and_404() throws Exception {
                mockMvc.perform(patch("/tasks/{id}", NOT_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("title").doesNotExist())
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("DELETE 요청은")
    class Describe_DELETE {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_an_existing_task_id {
            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(eq(EXISTING_ID)))
                        .willReturn(task);
            }

            @Test
            @DisplayName("204코드를 응답한다")
            void it_replies_with_204() throws Exception {
                mockMvc.perform(delete("/tasks/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("title").doesNotExist())
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_with_not_an_existing_task_id {
            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(eq(NOT_EXISTING_ID)))
                        .willThrow(new TaskNotFoundException(NOT_EXISTING_ID));
            }

            @Test
            @DisplayName("404코드와 에러메시지를 응답한다")
            void it_replies_with_the_error_message_and_404() throws Exception {
                mockMvc.perform(delete("/tasks/{id}", NOT_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("title").doesNotExist())
                        .andExpect(jsonPath("message").exists())
                        .andExpect(status().isNotFound());
            }
        }
    }

}
