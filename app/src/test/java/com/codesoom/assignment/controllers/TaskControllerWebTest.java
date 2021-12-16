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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    ObjectMapper objectMapper;

    private final static String NEW_TITLE = "new task";
    private final static String TITLE_POSTFIX = " spring";

    @BeforeEach
    void setUp() {
        Long taskId = 1L;
        Long wrongId = 100L;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle(NEW_TITLE);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(taskId)).willReturn(task);
        given(taskService.getTask(wrongId)).willThrow(TaskNotFoundException.class);
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(NEW_TITLE)));
    }

    @Test
    void detail_ok() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(NEW_TITLE)));
    }

    @Test
    void detail_fail() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Task source = new Task();
        source.setTitle(NEW_TITLE);
        String content = objectMapper.writeValueAsString(source);

        given(taskService.createTask(any(Task.class))).willReturn(source);

        mockMvc.perform(post("/tasks")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(NEW_TITLE)));
    }

    @Test
    void update() throws Exception {
        Long taskId = 1L;
        Task source = new Task();
        source.setTitle(NEW_TITLE + TITLE_POSTFIX);
        String content = objectMapper.writeValueAsString(source);

        given(taskService.updateTask(eq(taskId), any(Task.class))).willReturn(source);

        mockMvc.perform(patch("/tasks/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(NEW_TITLE + TITLE_POSTFIX)));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
