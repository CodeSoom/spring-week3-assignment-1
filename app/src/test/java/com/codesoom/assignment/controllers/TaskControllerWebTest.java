package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setTitle("Test Task");
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(task);
        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));

        given(taskService.createTask(task)).willReturn(task);
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Task")));
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    void detailWithNotExistedId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void detailWithBadRequestId() throws Exception {
        mockMvc.perform(get("/tasks/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create() throws Exception {
        Task source = new Task();
        source.setTitle("Test task");

        String content = objectMapper.writeValueAsString(source);

        mockMvc.perform(post("/tasks")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        // TODO: 빈 응답값이 온다. 왜 그럴까? 응답값은 어떻게 테스트 하는가?
    }
}
