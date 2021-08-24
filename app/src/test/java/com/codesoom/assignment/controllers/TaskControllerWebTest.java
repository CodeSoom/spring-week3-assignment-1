package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @BeforeTestClass
    void setUp(){
        Task task = new Task();
        task.setTitle("Test1");
        taskService.createTask(task);
    }

    @Test
    void list() throws Exception{
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(status().isNotFound())
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Test
    void getTask() throws Exception{
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test1")));
    }

    /*@Test
    void createTask() throws Exception{
        mockMvc.perform(post("/tasks/1"))
                .andExpect(status().isOk());
    }*/

}
