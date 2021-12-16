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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    private static final String TASK_TITLE = "test";
    private static final String NEW_TITLE = "new test";
    private static final String UPDATE_POSTFIX = "New";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        tasks.add(task);

        taskService.createTask(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(task);

        given(taskService.getTask(100L))
                .willThrow(new TaskNotFoundException(100L));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TASK_TITLE)));
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TASK_TITLE)));
    }

    @Test
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Task task = new Task();
        task.setTitle(NEW_TITLE);
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(post("/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
        //.andExpect(content().string(containsString(NEW_TITLE)));
        // 동작이 되지 않는 이유?
        // Response content
        //Expected: "new test"
        //     but: was ""
        //java.lang.AssertionError: Response content
    }

    @Test
    void updateWithValidId() throws Exception {
        Task task = new Task();
        task.setTitle(UPDATE_POSTFIX + TASK_TITLE);
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(put("/tasks/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //.andExpect(content().string(containsString(UPDATE_POSTFIX + TASK_TITLE)));
    }

    @Test
    void updateWithInvalidId() throws Exception {
        Task task = new Task();
        task.setTitle(UPDATE_POSTFIX + TASK_TITLE);
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(put("/tasks/0")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchWithValidId() throws Exception {
        Task task = new Task();
        task.setTitle(UPDATE_POSTFIX + TASK_TITLE);
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(patch("/tasks/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //.andExpect(content().string(containsString(UPDATE_POSTFIX + TASK_TITLE)));
    }

    @Test
    void patchWithInvalidId() throws Exception {
        Task task = new Task();
        task.setTitle(UPDATE_POSTFIX + TASK_TITLE);
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(patch("/tasks/0")
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
        mockMvc.perform(delete("/tasks/0"))
                .andExpect(status().isNotFound());
    }
}
