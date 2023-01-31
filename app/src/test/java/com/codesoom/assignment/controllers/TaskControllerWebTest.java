package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class TaskControllerWebTest {

    private static final String TASK_TITLE = "Test1";
    private String taskContent = "{\"title\":\"Test2\"}";

    private Task task = new Task();
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        Task task = new Task();

        task.setId(1L);
        task.setTitle(TASK_TITLE);
    }

    @Test
    void getTask() throws Exception {
        mockMvc.perform(get("/tasks/" + 1L))
                .andExpect(status().isOk());
    }

    @Test
    void createTask() throws Exception {
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskContent))
                .andExpect(status().isCreated());
    }

    @Test
    void updateTask() throws Exception {
        mockMvc.perform(put("/tasks/" + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskContent))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTaskNothing() throws Exception {
        mockMvc.perform(delete("/tasks/" + 1L))
                .andExpect(status().isNoContent());
    }
}
