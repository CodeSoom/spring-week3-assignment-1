package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @DisplayName("GET /tasks 요청은")
    class Describe_GET_list {

        @Nested
        @DisplayName("path에 id가 없고 저장소에 task가 있다면")
        class Context_with_tasks {

            @BeforeEach
            void setUp() {
                given(taskService.getTasks()).willReturn(tasks);
                tasks.add(task);
                tasks.add(task);
            }

            RequestBuilder subject() {
                return get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
            }

            @Test
            @DisplayName("모든 task들과 상태코드 200을 응답한다")
            void list() throws Exception {
                mockMvc.perform(subject())
                        .andExpect(content().string(containsString(TASK_TITLE)))
                        .andExpect(content().string(objectMapper.writeValueAsString(tasks)))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("path에 id가 없고 저장소에 task가 없다면")
        class Context_without_tasks {

            @BeforeEach
            void setUp() {
                given(taskService.getTasks()).willReturn(tasks);
            }

            RequestBuilder subject() {
                return get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
            }

            @Test
            @DisplayName("빈 ArrayList와 상태코드 200을 응답한다")
            void emptyList() throws Exception {
                mockMvc.perform(subject())
                        .andExpect(content().string(objectMapper.writeValueAsString(tasks)))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("GET /tasks/{id} 요청은")
    class Describe_GET_detail {

        @Nested
        @DisplayName("path에 id가 있고 저장소에 task가 있다면")
        class Context_with_an_existing_task_id {

            @BeforeEach
            void setUp() {
                given(taskService.getTask(EXISTING_ID)).willReturn(task);
            }

            RequestBuilder subject() {
                return get("/tasks/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
            }

            @Test
            @DisplayName("task와 상태코드 200을 응답한다")
            void detail() throws Exception {
                mockMvc.perform(subject())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("path에 저장소에 없는 id가 주어진다면")
        class Context_with_not_an_existing_task_id {

            @BeforeEach
            void setUp() {
                given(taskService.getTask(NOT_EXISTING_ID))
                        .willThrow(new TaskNotFoundException(NOT_EXISTING_ID));
            }

            RequestBuilder subject() {
                return get("/tasks/{id}", NOT_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
            }

            @Test
            @DisplayName("에러메시지와 상태코드 404를 응답한다")
            void detailWithNotAnExistingId() throws Exception {
                mockMvc.perform(subject())
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("title").doesNotExist())
                        .andExpect(jsonPath("message").exists())
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST /tasks 요청은")
    class Describe_POST {

        @Nested
        @DisplayName("task가 주어진다면")
        class Context_with_task {

            @BeforeEach
            void setUp() {
                given(taskService.createTask(any(Task.class)))
                        .willReturn(task);
            }

            RequestBuilder subject() throws JsonProcessingException {
                return post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task));
            }

            @Test
            @DisplayName("task와 상태코드 201을 응답한다")
            void create() throws Exception {
                mockMvc.perform(subject())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists())
                        .andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("PUT /tasks/{id} 요청은")
    class Describe_PUT {

        @Nested
        @DisplayName("path에 존재하는 id가 주어진다면")
        class Context_with_existing_task_id {

            @BeforeEach
            void setUp() {
                given(taskService.updateTask(eq(EXISTING_ID), any(Task.class)))
                        .willReturn(task);
            }

            RequestBuilder subject() throws Exception {
                return put("/tasks/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task));
            }

            @Test
            @DisplayName("업데이트한 task와 상태코드 200을 응답한다")
            void update() throws Exception {
                mockMvc.perform(subject()).andExpect(jsonPath("id").exists())
                       .andExpect(jsonPath("title").exists())
                       .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("path에 존재하지 않는 id가 주어진다면")
        class Context_with_not_existing_task_id {

            @BeforeEach
            void setUp() {
                given(taskService.updateTask(eq(NOT_EXISTING_ID), any(Task.class)))
                        .willThrow(new TaskNotFoundException(NOT_EXISTING_ID));
            }

            RequestBuilder subject() throws JsonProcessingException {
                return put("/task/{id}", NOT_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task));
            }

            @Test
            @DisplayName("에러메시지와 상태코드 404를 응답한다")
            void updateWithNotExistingId() throws Exception {
                mockMvc.perform(subject())
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("title").doesNotExist())
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("PATCH /tasks/{id} 요청은")
    class Describe_PATCH {

        @Nested
        @DisplayName("path에 존재하는 id가 주어진다면")
        class Context_with_existing_task_id {

            @BeforeEach
            void setUp() {
                given(taskService.updateTask(eq(EXISTING_ID), any(Task.class)))
                        .willReturn(task);
            }

            RequestBuilder subject() throws JsonProcessingException {
                return patch("/tasks/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task));
            }

            @Test
            @DisplayName("업데이트한 task와 상태코드 200을 응답한다")
            void update() throws Exception {
                mockMvc.perform(subject())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("path에 존재하지 않는 id가 주어진다면")
        class Context_with_not_existing_task_id {

            @BeforeEach
            void setUp() {
                given(taskService.updateTask(eq(NOT_EXISTING_ID), any(Task.class)))
                        .willThrow(new TaskNotFoundException(NOT_EXISTING_ID));
            }

            RequestBuilder subject() throws JsonProcessingException {
                return patch("/task/{id}", NOT_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task));
            }

            @Test
            @DisplayName("에러메시지와 상태코드 404를 응답한다")
            void updateWithNotExistingId() throws Exception {
                mockMvc.perform(subject())
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("title").doesNotExist())
                        .andExpect(status().isNotFound());

            }
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{id} 요청은")
    class Describe_DELETE {

        @Nested
        @DisplayName("path에 존재하는 id가 주어진다면")
        class Context_with_existing_task_id {

            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(eq(EXISTING_ID)))
                        .willReturn(task);
            }

            RequestBuilder subject() {
                return delete("/tasks/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
            }

            @Test
            @DisplayName("상태코드 204를 응답한다")
            void deleteTask() throws Exception {
                mockMvc.perform(subject())
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("title").doesNotExist())
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("path에 존재하지 않는 id가 주어진다면")
        class Context_with_not_existing_task_id {

            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(eq(NOT_EXISTING_ID)))
                        .willThrow(new TaskNotFoundException(NOT_EXISTING_ID));
            }

            RequestBuilder subject() {
                return delete("/tasks/{id}", NOT_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
            }

            @Test
            @DisplayName("에러메시지와 상태코드 404를 응답한다")
            void deleteWithNoExistingId() throws Exception {
                mockMvc.perform(subject())
                        .andExpect(jsonPath("id").doesNotExist())
                        .andExpect(jsonPath("title").doesNotExist())
                        .andExpect(jsonPath("message").exists())
                        .andExpect(status().isNotFound());
            }
        }
    }
}
