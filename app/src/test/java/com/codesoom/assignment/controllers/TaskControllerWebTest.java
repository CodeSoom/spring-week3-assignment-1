package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    @DisplayName("작업 목록이 없을 때, 빈 배열을 리턴하고 200 응답을 보낸다.")
    void getListTest() throws Exception {
        given(taskService.getTasks()).willReturn(new ArrayList<>());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("작업 목록이 주어지고 조회할 때, 작업 목록과 200 응답을 리턴한다.")
    void GivenTasksAndWhenQueriedReturnsTasksAndOk() throws Exception {
        given(taskService.getTasks()).willReturn(Arrays.asList(new Task(), new Task()));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }
}
