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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController")
public class TaskControllerWebTest {

    private static final String TASK_TITLE = "test";
    private static final String TASK_NOT_FOUND = "Task not found";
    private static final String TASK_CREATE_PREFIX = "new";
    private static final String TASK_UPDATE_PREFIX = "fix";
    private static final Long EXIST_ID = 1L;
    private static final Long WRONG_ID = -1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Nested
    @DisplayName("GET /tasks 는")
    class GetTasks {

        @BeforeEach
        void setUp() {
            List<Task> taskList = new ArrayList<>();
            Task task = new Task();
            task.setTitle(TASK_TITLE);
            taskList.add(task);

            given(taskService.getTasks()).willReturn(taskList);
        }

        @Test
        @DisplayName("모든 할 일 목록과 OK (200 status) 응답한다")
        void thenReturnTaskList() throws Exception {
            mockMvc.perform(get("/tasks"))
                   .andExpect(status().isOk())
                   .andExpect(content().string(containsString(TASK_TITLE)));
        }
    }

    @Nested
    @DisplayName("GET /tasks/{taskId} 는")
    class GetTask {

        @BeforeEach
        void setUp() {
            Task task = new Task();
            task.setTitle(TASK_TITLE);

            given(taskService.getTask(EXIST_ID)).willReturn(task);
        }

        @Test
        @DisplayName("할 일 상세정보와 OK (200 status) 응답한다")
        void thenReturnTask() throws Exception {
            mockMvc.perform(get("/tasks/" + EXIST_ID))
                   .andExpect(status().isOk())
                   .andExpect(content().string(containsString(TASK_TITLE)));
        }

        @Test
        @DisplayName("TaskNotFoundException 예외를 던진다")
        void thenReturnTaskNotFoundException() throws Exception {
            given(taskService.getTask(WRONG_ID)).willThrow(new TaskNotFoundException(WRONG_ID));

            mockMvc.perform(get("/tasks/" + null))
                   .andExpect(status().is4xxClientError());
            mockMvc.perform(get("/tasks/" + WRONG_ID))
                   .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /tasks 는")
    class postTask {

        @Test
        @DisplayName("새 Task 를 저장하고 OK (200 status) 응답한다")
        void thenReturnNewTask() throws Exception {
            Task task = new Task();
            task.setTitle(TASK_CREATE_PREFIX + TASK_TITLE);
            String taskContent = objectMapper.writeValueAsString(task);

            when(taskService.createTask(any(Task.class))).thenReturn(task);

            mockMvc.perform(post("/tasks")
                   .content(taskContent)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isCreated())
                   .andExpect(content().string(containsString(TASK_CREATE_PREFIX + TASK_TITLE)));
        }

        @Test
        @DisplayName("NullPointException 예외를 던진다")
        void thenReturnNullPointException() {
                    /* TODO
                        Add Service logic
                    */
        }

        @Test
        @DisplayName("Task 를 저장하지 않고 (4xx status) 응답한다")
        void thenReturnNothing() {
                    /* TODO
                        Add Service logic
                    */
        }
    }

    @Nested
    @DisplayName("PUT & PATCH /tasks/{taskId} 는")
    class putAndPatchTask {

        @BeforeEach
        void setUp() {
            Task task = new Task();
            task.setTitle(TASK_TITLE);

            given(taskService.getTask(EXIST_ID)).willReturn(task);
        }

        @Test
        @DisplayName("Task 를 수정하고 OK (200 status) 응답한다")
        void thenReturnModifiedTask() throws Exception {
            Task requestTask = new Task();
            requestTask.setTitle(TASK_UPDATE_PREFIX + TASK_TITLE);
            String taskContent = objectMapper.writeValueAsString(requestTask);

            when(taskService.updateTask(eq(EXIST_ID), any(Task.class))).thenReturn(requestTask);

            mockMvc.perform(put("/tasks/" + EXIST_ID)
                   .content(taskContent)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk())
                   .andExpect(content().string(containsString(TASK_UPDATE_PREFIX + TASK_TITLE)));

            requestTask.setTitle(TASK_TITLE);
            taskContent = objectMapper.writeValueAsString(requestTask);

            mockMvc.perform(patch("/tasks/" + EXIST_ID)
                   .content(taskContent)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk())
                   .andExpect(content().string(containsString(TASK_TITLE)));
        }

        @Test
        @DisplayName("TaskNotFoundException 예외를 던진다")
        void thenReturnTaskNotFoundException() throws Exception {
            Task requestTask = new Task();
            requestTask.setTitle(TASK_UPDATE_PREFIX + TASK_TITLE);
            String taskContent = objectMapper.writeValueAsString(requestTask);

            when(taskService.updateTask(eq(WRONG_ID), any(Task.class))).thenThrow(new TaskNotFoundException(WRONG_ID));

            mockMvc.perform(put("/tasks/" + WRONG_ID)
                   .content(taskContent)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isNotFound())
                   .andExpect(content().string(containsString(TASK_NOT_FOUND)));
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{taskId} 는")
    class deleteTask {

        @BeforeEach
        void setUp() {
            Task task = new Task();
            task.setTitle(TASK_TITLE);

            given(taskService.getTask(EXIST_ID)).willReturn(task);
        }

        @Test
        @DisplayName("Task 를 삭제하고 No Content (204 status) 응답한다")
        void thenReturnNoContent() throws Exception {
            Task task = new Task();
            when(taskService.deleteTask(eq(EXIST_ID))).thenReturn(task);

            mockMvc.perform(delete("/tasks/" + EXIST_ID))
                   .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("TaskNotFoundException 예외를 던진다")
        void thenReturnTaskNotFoundException() throws Exception {
            when(taskService.deleteTask(eq(WRONG_ID))).thenThrow(new TaskNotFoundException(WRONG_ID));

            mockMvc.perform(delete("/tasks/" + WRONG_ID))
                   .andExpect(status().isNotFound())
                   .andExpect(content().string(containsString(TASK_NOT_FOUND)));
        }
    }
}
