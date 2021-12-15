package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
public class TaskControllerWebTest {

    private static final String FIRST_TASK_TITLE = "test1";
    private static final String SECOND_TASK_TITLE = "test2";
    private static final String UPDATE_POSTFIX = "!!!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();

        Task firstTask = new Task();
        firstTask.setTitle(FIRST_TASK_TITLE);
        tasks.add(firstTask);

        Task secondTask = new Task();
        secondTask.setTitle(SECOND_TASK_TITLE);
        secondTask.setId(2L);

        Task updatedTask = new Task();
        updatedTask.setTitle(FIRST_TASK_TITLE + UPDATE_POSTFIX);
        updatedTask.setId(1L);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.createTask(any(Task.class))).willReturn(secondTask);
        given(taskService.getTask(1L)).willReturn(firstTask);
        given(taskService.getTask(2L)).willThrow(TaskNotFoundException.class);
        given(taskService.updateTask(eq(1L), any(Task.class))).willReturn(updatedTask);
        given(taskService.updateTask(eq(2L), any(Task.class))).willThrow(TaskNotFoundException.class);
        given(taskService.deleteTask(1L)).willReturn(firstTask);
        given(taskService.deleteTask(2L)).willThrow(TaskNotFoundException.class);
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
    void deleteWithInvalidId() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWithValidId() throws Exception {
        mockMvc.perform(delete("/tasks/2"))
                .andExpect(status().isNotFound());
    }
}
