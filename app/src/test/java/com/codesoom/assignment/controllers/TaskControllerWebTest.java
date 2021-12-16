package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class TaskControllerWebTest {

    private static final String FIRST_TASK_TITLE = "test1";
    private static final String SECOND_TASK_TITLE = "test2";
    private static final String UPDATE_POSTFIX = "!!!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService.reset();

        // Fixture
        Task task = new Task();
        task.setTitle(FIRST_TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(FIRST_TASK_TITLE)));
    }

    @Test
    void createNewTask() throws Exception {
        Task task = new Task();
        task.setTitle(SECOND_TASK_TITLE);
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(post("/tasks")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(SECOND_TASK_TITLE)));
    }

    @Test
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(FIRST_TASK_TITLE)));
    }

    @Test
    void updateWithValidId() throws Exception {
        Task task = new Task();
        task.setTitle(FIRST_TASK_TITLE + UPDATE_POSTFIX);
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(put("/tasks/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(FIRST_TASK_TITLE + UPDATE_POSTFIX)));
    }

    @Test
    void updateWithInvalidId() throws Exception {
        Task task = new Task();
        task.setTitle(FIRST_TASK_TITLE + UPDATE_POSTFIX);
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(put("/tasks/2")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchWithValidId() throws Exception {
        Task task = new Task();
        task.setTitle(FIRST_TASK_TITLE + UPDATE_POSTFIX);
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(patch("/tasks/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(FIRST_TASK_TITLE + UPDATE_POSTFIX)));
    }

    @Test
    void patchWithInvalidId() throws Exception {
        Task task = new Task();
        task.setTitle(FIRST_TASK_TITLE + UPDATE_POSTFIX);
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(patch("/tasks/2")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWithValidId() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWithInvalidId() throws Exception {
        mockMvc.perform(delete("/tasks/2"))
                .andExpect(status().isNotFound());
    }
}
