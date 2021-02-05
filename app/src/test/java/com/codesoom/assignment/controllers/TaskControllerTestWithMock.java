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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        task.setTitle("책읽기");

        objectMapper = new ObjectMapper();
    }

    @DisplayName("Task 목록")
    @Test
    void testList() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Task 상세")
    @Test
    void testDetailWithValidId() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/tasks"+ "/" + id))
                .andExpect(status().isOk());
    }

    @DisplayName("Task 생성")
    @Test
    void testCreate() throws Exception {
        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(task)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("Task 수정")
    @Test
    void testUpdate() throws Exception {

        Task newTask = new Task();
        newTask.setTitle("책구매");

        Long id = 1L;

        mockMvc.perform(put("/tasks" + "/" + id)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newTask)))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
}
