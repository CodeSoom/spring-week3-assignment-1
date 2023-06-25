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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(TaskController.class)
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private ObjectMapper objectMapper;
    private List<Task> tasks;
    private Task task;
    private String TASK_TITLE = "title";
    private String NEW_TASK_TITLE = "new title";
    private Long VALID_TASK_ID = 1L;
    private Long INVALID_TASK_ID = 100L;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        tasks = new ArrayList<>();
        task = new Task();
        task.setId(VALID_TASK_ID);
        task.setTitle(TASK_TITLE);
    }

    @Nested
    @DisplayName("GET /tasks")
    class Describe_get_list_request {

        @Nested
        @DisplayName("Task가 있으면")
        class Context_with_tasks {

            @BeforeEach
            void setUp() {
                tasks.add(task);
                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("상태코드 200을 응답하고, Task List를 리턴한다")
            void It_returns_200_and_tasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TASK_TITLE)));
            }
        }

        @Nested
        @DisplayName("Task가 없으면")
        class Context_with_no_task {

            @BeforeEach
            void setUp() {
                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("상태코드 200을 응답하고, 비어있는 Task List를 리턴한다")
            void It_returns_200_and_tasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));
            }
        }

    }

    @Nested
    @DisplayName("GET /tasks/{id}")
    class Describe_get_detail_request {

        @Nested
        @DisplayName("Task를 찾을 수 있으면")
        class Context_with_valid_task_id {

            @BeforeEach
            void setup() {
                given(taskService.getTask(VALID_TASK_ID)).willReturn(task);
            }

            @Test
            @DisplayName("상태코드 200을 응답하고, id로 찾은 Task를 리턴한다")
            void it_returns_200_and_task_found_by_id() throws Exception {
                mockMvc.perform(get("/tasks/" + VALID_TASK_ID))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(VALID_TASK_ID))
                        .andExpect(jsonPath("title").value(TASK_TITLE))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("Task를 찾을 수 없으면")
        class Context_with_invalid_task_id {

            @BeforeEach
            void setup() {
                given(taskService.getTask(INVALID_TASK_ID)).willThrow(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("상태코드 404를 응답한다")
            void it_returns_404() throws Exception {
                mockMvc.perform(get("/tasks/" + INVALID_TASK_ID))
                        .andExpect(status().isNotFound());
            }
        }

    }

    @Nested
    @DisplayName("POST /tasks")
    class Describe_post_request {

        @Nested
        @DisplayName("Task가 주어지면")
        class Context_with_task {

            @BeforeEach
            void setup() {
                given(taskService.createTask(any(Task.class))).willReturn(task);
            }

            @Test
            @DisplayName("상태코드 201을 응답하고, 추가된 Task를 리턴한다")
            void it_returns_201_and_created_task() throws Exception {
                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(task)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id").value(VALID_TASK_ID))
                        .andExpect(jsonPath("title").value(TASK_TITLE));
            }

        }

    }

    @Nested
    @DisplayName("PUT /tasks/{id}")
    class Describe_put_request {

        private Task source;

        @Nested
        @DisplayName("Task를 찾을 수 있으면")
        class Context_with_valid_task_id {

            @BeforeEach
            void setup() {
                source = new Task();
                source.setTitle(NEW_TASK_TITLE);
                task.setTitle(source.getTitle());
                given(taskService.updateTask(eq(VALID_TASK_ID), any(Task.class))).willReturn(task);
            }

            @Test
            @DisplayName("상태코드 200을 응답하고, 수정된 Task를 리턴한다")
            void it_returns_200_and_updated_task() throws Exception {
                mockMvc.perform(put("/tasks/" + VALID_TASK_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(source)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(VALID_TASK_ID))
                        .andExpect(jsonPath("title").value(NEW_TASK_TITLE));
            }
        }

        @Nested
        @DisplayName("Task를 찾을 수 없으면")
        class Context_with_invalid_task_id {

            @BeforeEach
            void setup() {
                source = new Task();
                source.setTitle(NEW_TASK_TITLE);
                given(taskService.updateTask(eq(INVALID_TASK_ID), any(Task.class))).willThrow(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("상태코드 404를 응답한다")
            void it_returns_404() throws Exception {
                objectMapper = new ObjectMapper();
                mockMvc.perform(put("/tasks/" + INVALID_TASK_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(source)))
                        .andExpect(status().isNotFound());

            }

        }

    }

    @Nested
    @DisplayName("DELETE /tasks/{id}")
    class Describe_delete_request {

        @Nested
        @DisplayName("Task를 찾을 수 있으면")
        class Context_with_valid_task_id {

            @Test
            @DisplayName("상태코드 204를 응답한고, 다시 DELETE를 요청하면 404를 응답한다")
            void it_returns_204() throws Exception {
                mockMvc.perform(delete("/tasks/" + VALID_TASK_ID))
                        .andExpect(status().isNoContent());

                given(taskService.deleteTask(VALID_TASK_ID)).willThrow(TaskNotFoundException.class);

                mockMvc.perform(delete("/tasks/" + VALID_TASK_ID))
                        .andExpect(status().isNotFound());

            }

        }

        @Nested
        @DisplayName("Task를 찾을 수 없으면")
        class Context_with_invalid_task_id {

            @BeforeEach
            void setup() {
                given(taskService.deleteTask(INVALID_TASK_ID)).willThrow(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("상태코드 404를 응답한다")
            void it_returns_404() throws Exception {
                mockMvc.perform(delete("/tasks/" + INVALID_TASK_ID))
                        .andExpect(status().isNotFound());
            }

        }

    }

}

