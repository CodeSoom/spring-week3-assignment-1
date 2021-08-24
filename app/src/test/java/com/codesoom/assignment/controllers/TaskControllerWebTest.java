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

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    final private Long VALID_ID = 1L;
    final private Long INVALID_ID = 100L;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        task1 = new Task(1L, "title1");
        task2 = new Task(2L, "title2");
        tasks = Arrays.asList(task1, task2);
    }

    @Test
    void list() throws Exception {
        given(taskService.getTasks()).willReturn(tasks);

        String expectedContent = objectMapper.writeValueAsString(tasks);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    void detailWithValidId() throws Exception {
        given(taskService.getTask(VALID_ID)).willReturn(task1);

        String expectedContent = objectMapper.writeValueAsString(task1);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    void detailWithInvalidId() throws Exception {
        given(taskService.getTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));

        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        given(taskService.createTask(task1)).willReturn(task1);

        String expectedContent = objectMapper.writeValueAsString(task1);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedContent))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedContent));
    }

    @Test
    void updateWithValidId() throws Exception {
        Task updatedTask = new Task(VALID_ID, task2.getTitle());
        given(taskService.updateTask(VALID_ID, task2)).willReturn(updatedTask);

        String updateContent = objectMapper.writeValueAsString(task2);
        String expectedContent = objectMapper.writeValueAsString(updatedTask);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateContent))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    void updateWithInvalidId() throws Exception {
        given(taskService.updateTask(INVALID_ID, task2)).willThrow(new TaskNotFoundException(INVALID_ID));

        String updateContent = objectMapper.writeValueAsString(task2);

        mockMvc.perform(put("/tasks/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateContent))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchWithValidId() throws Exception {
        Task updatedTask = new Task(VALID_ID, task2.getTitle());
        given(taskService.updateTask(VALID_ID, task2)).willReturn(updatedTask);

        String updateContent = objectMapper.writeValueAsString(task2);
        String expectedContent = objectMapper.writeValueAsString(updatedTask);

        mockMvc.perform(patch("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateContent))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    void patchWithInvalidId() throws Exception {
        given(taskService.updateTask(INVALID_ID, task2)).willThrow(new TaskNotFoundException(INVALID_ID));

        String updateContent = objectMapper.writeValueAsString(task2);

        mockMvc.perform(patch("/tasks/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateContent))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWithValidId() throws Exception {
        doNothing().doThrow(new RuntimeException())
                .when(taskService)
                .deleteTask(VALID_ID);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteWithInvalidId() throws Exception {
        given(taskService.deleteTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));

        mockMvc.perform(delete("/tasks/100"))
                .andExpect(status().isNotFound());
    }
}
