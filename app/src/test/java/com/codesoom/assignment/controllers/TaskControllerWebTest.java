package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

    private Task task;
    private Task newTask;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 100L;
    private static final String TASK_TITLE = "my first task";
    private static final String NEW_TASK_TITLE = "my new task";

    @BeforeEach
    void setup() {
        List<Task> tasks = new ArrayList<>();

        task = new Task();
        task.setTitle(TASK_TITLE);

        tasks.add(task);

        newTask = new Task();
        newTask.setTitle(NEW_TASK_TITLE);

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(VALID_ID)).willReturn(task);
        given(taskService.getTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));

        given(taskService.createTask(newTask)).willReturn(newTask);

        given(taskService.updateTask(VALID_ID, newTask)).willReturn(newTask);
        given(taskService.updateTask(INVALID_ID, newTask)).willThrow(new TaskNotFoundException(INVALID_ID));

        given(taskService.deleteTask(VALID_ID)).willReturn(task);
        given(taskService.deleteTask(INVALID_ID)).willThrow(new TaskNotFoundException((INVALID_ID)));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TASK_TITLE)));
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/" + VALID_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TASK_TITLE)));
    }

    @Test
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/" + INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        String content = objectMapper.writeValueAsString(newTask);

        mockMvc.perform(post("/tasks")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void updateWithValidId() throws Exception {
        String content = objectMapper.writeValueAsString(newTask);

        mockMvc.perform(put("/tasks/" + VALID_ID)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateWithInvalidId() throws Exception {
        // TODO: 테스트 실패 - 수정하기
        String content = objectMapper.writeValueAsString(newTask);

        mockMvc.perform(put("/tasks/" + INVALID_ID)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWithValidId() throws Exception {
        mockMvc.perform(delete("/tasks/" + VALID_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWithInvalidId() throws Exception {
        mockMvc.perform(delete("/tasks/" + INVALID_ID))
                .andExpect(status().isNotFound());
    }
}