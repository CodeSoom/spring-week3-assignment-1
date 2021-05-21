package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc; // 우리가 new 하지 않고, 스프링이 알아서 자동으로 넣어준다 => autowired

    @MockBean
    private TaskService taskService;

    @Test
    public void list() throws Exception {
        mockMvc.perform(get("/tasks")).andExpect(status().isOk());
    }

//    @Test
//    void detailWithValidId() throws Exception {
//        mockMvc.perform(get("/tasks/1")).andExpect(status().isOk());
//    }

//    @Test
//    void detailWithInvalidId() throws Exception {
//        mockMvc.perform(get("/tasks/1")).andExpect(status().isNotFound());
//    }
}
