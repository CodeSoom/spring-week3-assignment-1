package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService = new TaskService();

    @Test
    void getAllTasks() throws Exception {
        mockMvc.perform(get("http://localhost/tasks"))
            .andExpect(status().isOk());
    }

    @Test
    void createTask() throws Exception {
        String taskJsonString = "{\"title\": \"play game!\"}";

        mockMvc.perform(
            post("http://localhost/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJsonString)
        ).andExpect(status().isCreated());
    }
}
