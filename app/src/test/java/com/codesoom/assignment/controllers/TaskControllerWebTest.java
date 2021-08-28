package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;
    private static final String TEST_TITLE = "Test Title";
    private final Task task = new Task(1L, TEST_TITLE);

    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        given(taskService.getTaskList()).willReturn(tasks);
        given(taskService.getTaskById(1L)).willReturn(Optional.of(task));
    }

    @Test
    void getTaskList() throws Exception {
        System.out.println(taskService.getTaskList());
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(contains(TEST_TITLE)));
    }

    @Test
    void getTaskById() throws Exception {
        mockMvc.perform(get("/tasks/" + "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(contains(TEST_TITLE)));
    }

    @Test
    void addTask() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(task);
    }

    @Test
    void replaceTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteTask() {
    }
}