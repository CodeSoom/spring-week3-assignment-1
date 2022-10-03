package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Task testTask;
    private Task createRequestTask;
    private Task updateRequestTask;
    private Task createResponseTask;
    private Task updateResponseTask;

    private final Long INVALID_TASK_ID = -1L;

    @BeforeEach
    void setUp() {
        ArrayList<Task> tasks = new ArrayList<>();
        testTask = new Task(1L, "test1");
        tasks.add(testTask);
        tasks.add(new Task(2L, "test2"));

        createRequestTask = new Task(null, "test1");
        updateRequestTask = new Task(null, "updateTest");
        createResponseTask = new Task(1L, "test1");
        updateResponseTask = new Task(1L, "updateTest");

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(1L)).willReturn(testTask);

        given(taskService.createTask(any(Task.class))).willReturn(createResponseTask);

        given(taskService.updateTask(eq(testTask.getId()), any(Task.class))).willReturn(updateResponseTask);

        given(taskService.updateTask(eq(INVALID_TASK_ID), any(Task.class))).willThrow(new TaskNotFoundException(INVALID_TASK_ID));

        given(taskService.getTask(INVALID_TASK_ID)).willThrow(new TaskNotFoundException(INVALID_TASK_ID));

        given(taskService.deleteTask(INVALID_TASK_ID)).willThrow(new TaskNotFoundException(INVALID_TASK_ID));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test1")));
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/" + testTask.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/" + INVALID_TASK_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        String requestBody = objectMapper.writeValueAsString(createRequestTask);
        mockMvc.perform(post("/tasks")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(createRequestTask.getTitle())));
    }

    @Test
    void updateWithValidId() throws Exception {
        String requestBody = objectMapper.writeValueAsString(updateRequestTask);
        mockMvc.perform(put("/tasks/" + testTask.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(updateRequestTask.getTitle())));
    }

    @Test
    void updateWithInvalidId() throws Exception {
        String requestBody = objectMapper.writeValueAsString(updateRequestTask);
        mockMvc.perform(put("/tasks/" + INVALID_TASK_ID)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void patchWithValidId() throws Exception {
        String requestBody = objectMapper.writeValueAsString(updateRequestTask);
        mockMvc.perform(patch("/tasks/" + testTask.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(updateRequestTask.getTitle())));
    }

    @Test
    void patchWithInvalidId() throws Exception {
        String requestBody = objectMapper.writeValueAsString(updateRequestTask);
        mockMvc.perform(patch("/tasks/" + INVALID_TASK_ID)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWithValidId() throws Exception {
        mockMvc.perform(delete("/tasks/" + testTask.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWithInvalidId() throws Exception {
        mockMvc.perform(delete("/tasks/" + INVALID_TASK_ID))
                .andExpect(status().isNotFound());
    }
}