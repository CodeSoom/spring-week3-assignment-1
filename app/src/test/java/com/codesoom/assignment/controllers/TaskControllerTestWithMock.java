package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTestWithMock {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    Task task;
    ObjectMapper objectMapper;

    @BeforeEach
    void init(){
        task = new Task();
        task.setId(1L);
        task.setTitle("책일기");

        objectMapper = new ObjectMapper();


    }

    @DisplayName("존재하는 Task 목록")
    @Test
    void testListWithResource() throws Exception {

        taskService.createTask(task);

        mockMvc.perform(get("/tasks")
                .content(objectMapper.writeValueAsString(task)))
                .andDo(print())
                .andExpect(status().isOk());
    }



}
