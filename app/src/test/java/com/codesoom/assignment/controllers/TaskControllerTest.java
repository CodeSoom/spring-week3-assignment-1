package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DisplayName("할일 컨트롤러 기능 테스트 ")
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    @DisplayName("할일 리스트 조회 요청 성공시 200을 반환한다.")
    public void getList_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
