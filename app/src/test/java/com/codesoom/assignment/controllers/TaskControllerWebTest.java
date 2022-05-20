package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp(){
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("Test Task");
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Task")));
    }

    @Test
    void detail() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    void detailWithValidId() throws Exception {

        Task task = new Task();
        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));

        taskService.createTask(task);

        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isOk());
    }
}
