package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService = new TaskService();

    @BeforeEach
    void initTaskService() {
        taskService.clean();
    }

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

    @Test
    void deleteTask() throws Exception {
        mockMvc.perform(
                delete("http://localhost/tasks/1")
        ).andExpect(status().isNotFound());

        Task task = new Task();
        task.setTitle("Play Game.");
        taskService.createTask(task);

        mockMvc.perform(
                delete("http://localhost/tasks/1")
        ).andExpect(status().isNoContent());
    }

    @Test
    void getTask() throws Exception {
        mockMvc.perform(
                get("http://localhost/tasks/1")
        ).andExpect(status().isNotFound());

        Task task = new Task();
        task.setTitle("Play Game.");
        taskService.createTask(task);

        mockMvc.perform(
                get("http://localhost/tasks/1")
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString("Play Game.")));
    }

    @Test
    void putTask() throws Exception {
        String taskJsonString = "{\"title\": \"play game!\"}";
        mockMvc.perform(
                put("http://localhost/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJsonString)
        ).andExpect(status().isNotFound());

        Task task = new Task();
        task.setTitle("Listen Music.");
        taskService.createTask(task);

        mockMvc.perform(
                put("http://localhost/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJsonString))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("play game!")));
    }

    @Test
    void patchTask() throws Exception {
        String taskJsonString = "{\"title\": \"play game!\"}";
        mockMvc.perform(
                put("http://localhost/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJsonString)
        ).andExpect(status().isNotFound());

        Task task = new Task();
        task.setTitle("Listen Music.");
        taskService.createTask(task);

        mockMvc.perform(
                put("http://localhost/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJsonString))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("play game!")));
    }
}
