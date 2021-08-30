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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@DisplayName("TaskController 웹 테스트")
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private ObjectMapper objectMapper;

    private Task task1;
    private Task task2;
    private List<Task> tasks;
    private String contentTask;
    private String contentTasks;

    final private Long VALID_ID = 1L;
    final private Long INVALID_ID = 100L;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();

        task1 = new Task(1L, "title1");
        task2 = new Task(2L, "title2");
        tasks = Arrays.asList(task1, task2);

        contentTask = objectMapper.writeValueAsString(task1);
        contentTasks = objectMapper.writeValueAsString(tasks);
    }

    @Nested
    @DisplayName("GET")
    class Get {

        @BeforeEach
        void setUp() {
            given(taskService.getTasks()).willReturn(tasks);
            given(taskService.getTask(VALID_ID)).willReturn(task1);
            willThrow(new TaskNotFoundException(INVALID_ID)).given(taskService).getTask(INVALID_ID);
        }

        @Nested
        @DisplayName("path \'/tasks\' 호출하면")
        class Path_tasks {

            @Test
            @DisplayName("성공시 response(status: ok, content: json tasks)를 반환합니다.")
            void response_ok() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(contentTasks));
            }
        }

        @Nested
        @DisplayName("path \'/tasks/{id}\' 호출하면")
        class Path_tasks_id {

            @Test
            @DisplayName("성공시 response(status: ok, content: task)를 반환합니다.")
            void response_ok() throws Exception {
                mockMvc.perform(get("/tasks/1"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(contentTask));
            }

            @Test
            @DisplayName("Task가 없으시 response(status: not_found)를 반환합니다.")
            void response_not_found() throws Exception {
                mockMvc.perform(get("/tasks/100"))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST")
    class Post {

        @BeforeEach
        void setUp() {
            given(taskService.createTask(any(Task.class))).willReturn(task1);
        }

        @Nested
        @DisplayName("path \'/tasks\' 호출하면")
        class Path_tasks {

            @Test
            @DisplayName("성공시 response(status: created, content: json tasks)를 반환합니다.")
            void response_created() throws Exception {
                mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentTask))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(contentTask));
            }
        }
    }

    @Nested
    @DisplayName("PUT")
    class Put {

        @BeforeEach
        void setUp() {
            given(taskService.updateTask(eq(VALID_ID), any(Task.class))).willReturn(task1);
            willThrow(new TaskNotFoundException(INVALID_ID)).given(taskService).updateTask(eq(INVALID_ID), any(Task.class));
        }

        @Nested
        @DisplayName("path \'/tasks/{id}\' 호출하면")
        class Path_tasks_id {

            @Test
            @DisplayName("성공시 response(status: ok, content: task)를 반환합니다.")
            void response_ok() throws Exception {
                mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentTask))
                        .andExpect(status().isOk())
                        .andExpect(content().string(contentTask));            }

            @Test
            @DisplayName("Task가 없으시 response(status: not_found)를 반환합니다.")
            void response_not_found() throws Exception {
                mockMvc.perform(put("/tasks/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentTask))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("PATCH")
    class Patch {

        @BeforeEach
        void setUp() {
            given(taskService.updateTask(eq(VALID_ID), any(Task.class))).willReturn(task1);
            willThrow(new TaskNotFoundException(INVALID_ID)).given(taskService).updateTask(eq(INVALID_ID), any(Task.class));
        }

        @Nested
        @DisplayName("path \'/tasks/{id}\' 호출하면")
        class Path_tasks_id {

            @Test
            @DisplayName("성공시 response(status: ok, content: task)를 반환합니다.")
            void response_ok() throws Exception {
                mockMvc.perform(patch("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentTask))
                        .andExpect(status().isOk())
                        .andExpect(content().string(contentTask));            }

            @Test
            @DisplayName("Task가 없으시 response(status: not_found)를 반환합니다.")
            void response_not_found() throws Exception {
                mockMvc.perform(patch("/tasks/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentTask))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("DELETE")
    class Delete {

        @BeforeEach
        void setUp() {
            willThrow(new TaskNotFoundException(INVALID_ID)).given(taskService).deleteTask(INVALID_ID);
        }

        @Nested
        @DisplayName("path \'/tasks/{id}\' 호출하면")
        class Path_tasks_id {

            @Test
            @DisplayName("성공시 response(status: no_content)를 반환합니다.")
            void response_no_content() throws Exception {
                mockMvc.perform(delete("/tasks/1"))
                        .andExpect(status().isNoContent());
            }

            @Test
            @DisplayName("Task가 없으시 response(status: not_found)를 반환합니다.")
            void response_not_found() throws Exception {
                mockMvc.perform(delete("/tasks/100"))
                        .andExpect(status().isNotFound());
            }
        }
    }
}

