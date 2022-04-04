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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerMock_MVC_Test {
    private static final String TASK_TITLE_ONE = "testOne";
    private static final String TASK_TITLE_TWO = "testTwo";
    private static final String UPDATE_TITLE = "otherTest";
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setTitle(TASK_TITLE_ONE);
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(1L)).willReturn(task);

        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));

//        task = new Task();
//        task.setTitle(TASK_TITLE_TWO);
//        tasks.add(task);
//        given(taskService.getTask(2L)).willReturn(task);
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TASK_TITLE_ONE)));
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TASK_TITLE_ONE)));
    }

    @Test
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {

        //String content = task.toString();

//        mockMvc.perform(post("/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                        .andExpect(status().isCreated());

        //verify(taskService).createTask(task);

        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setTitle(TASK_TITLE_ONE);
        tasks.add(task);

        String content = task.toString();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(TASK_TITLE_ONE)));
    }
}
