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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@DisplayName("TaskController는")
@SpringBootTest
@AutoConfigureMockMvc
public class Describe_TaskController {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Nested
    @DisplayName("할 일 목록을 요청받은 경우")
    class Context_with_tasks_request {

        @Nested
        @DisplayName("할 일이 있을떄")
        class Context_with_task {

            private Task task;

            @BeforeEach
            void setUp() {
                List<Task> tasks = new ArrayList<>();
                task = new Task();
                task.setTitle("test");
                tasks.add(task);

                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("할 일 목록을 반환한다")
            void It_returns_task_list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(task.getTitle())));
            }
        }
    }

    @Nested
    @DisplayName("할 일의 세부사항을 요청받은 경우")
    class Context_with_task_detail_request {

        @Nested
        @DisplayName("요청받은 id의 할 일이 존재할 때")
        class Context_with_task {

            @BeforeEach
            void setUp() {
                Task task = new Task();
                task.setTitle("test");
                given(taskService.getTask(1L)).willReturn(task);
            }

            @Test
            @DisplayName("OK status를 반환한다")
            void It_responses_200() throws Exception {
                mockMvc.perform(get("/tasks/1"))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("요청받은 id의 할 일이 존재하지 않을 때")
        class Context_without_task {

            @BeforeEach
            void setUp() {
                given(taskService.getTask(100L))
                        .willThrow(new TaskNotFoundException(100L));
            }

            @Test
            @DisplayName("NotFound status를 반환한다")
            void It_responses_404() throws Exception {
                mockMvc.perform(get("/tasks/100"))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("할 일 생성을 요청받은 경우")
    class Context_with_request_creating_task {

        private Map<String, String> data;

        @Nested
        @DisplayName("요청받은 할 일이 타당하다면")
        class Context_with_task {

            @BeforeEach
            void setUp() {
                data = new HashMap<>();
                data.put("title", "test");
            }

            @Test
            @DisplayName("Created status를 반환한다")
            void It_responses_204() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                mockMvc.perform(post("/tasks")
                        .content(objectMapper.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());
            }
        }

        @Nested
        @DisplayName("요청받은 할 일이 null이라면")
        class Context_with_null {

            @BeforeEach
            void setUp() {
                data = null;
            }

            @Test
            @DisplayName("BadRequest status를 반환한다")
            void It_response_400() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                mockMvc.perform(post("/tasks")
                        .content(objectMapper.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("할 일 수정을 요청받은 경우")
    class Context_with_request_updating_task {

        @Nested
        @DisplayName("요청받은 id에 해당하는 할 일이 있다면")
        class Context_with_task {

            private ObjectMapper objectMapper;

            @BeforeEach
            void setUp() throws Exception {
                objectMapper = new ObjectMapper();
                Task task = new Task();
                task.setTitle("new task test");
                given(taskService.updateTask(1L, task)).willReturn(task);
            }

            @Test
            @DisplayName("할 일을 수정하고 OK status를 반환한다")
            void It_response_200_against_put() throws Exception {
                Map<String, String> newData = new HashMap<>();
                newData.put("title", "new task test");
                mockMvc.perform(put("/tasks/1")
                        .content(objectMapper.writeValueAsString(newData))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("할 일을 부분적으로 수정하고 OK status를 반환한다")
            void It_response_200_against_patch() throws Exception {
                Map<String, String> newData = new HashMap<>();
                newData.put("title", "new task test");
                mockMvc.perform(patch("/tasks/1")
                        .content(objectMapper.writeValueAsString(newData))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("요청받은 id에 해당하는 할 일이 없다면")
        class Context_without_task {

            @BeforeEach
            void setUp() {
                Task task = new Task();
                task.setTitle("new task test");
                given(taskService.updateTask(100L, task))
                        .willThrow(new TaskNotFoundException(100L));
            }

            @Test
            @DisplayName("NotFound status를 반환한다")
            void It_responses_404() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> newData = new HashMap<>();
                newData.put("title", "new task test");
                mockMvc.perform(put("/tasks/100")
                        .content(objectMapper.writeValueAsString(newData))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("할 일 삭제를 요청받은 경우")
    class Context_with_request_deleting_task {

        private Task task;

        @BeforeEach
        void setUp() {
            Task task = new Task();
            task.setTitle("test");
        }

        @Nested
        @DisplayName("요청받은 id에 해당하는 할 일이 있다면")
        class Context_with_task {

            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(1L)).willReturn(task);
            }

            @Test
            @DisplayName("할 일을 삭제하고 NoContent status를 반환한다")
            void It_response_204() throws Exception {
                mockMvc.perform(delete("/tasks/1"))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("요청받은 id에 해당하는 할 일이 없다면")
        class Context_without_task {

            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(100L))
                        .willThrow(new TaskNotFoundException(100L));
            }

            @Test
            @DisplayName("NotFound status를 반환한다")
            void It_response_404() throws Exception {
                mockMvc.perform(delete("/tasks/100"))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
