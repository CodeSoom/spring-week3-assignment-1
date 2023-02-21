package com.codesoom.assignment.controllers;


import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@RequiredArgsConstructor
class TaskWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;



    @Test
    void list() throws Exception {
        //given
        ArrayList<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("Test task");
        tasks.add(task);
        given(taskService.getTasks()).willReturn(tasks);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test task")));
    }

    @Test
    public void detailWithValidId() throws Exception{
        Task task = new Task();
        given(taskService.getTask(1L)).willReturn(task);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void detailWithInvalidId() throws Exception{
        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

}