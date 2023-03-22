package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효한 할일에 대하여 상세조회 요청시 201을 반환한다.")
    public void detailWithValidId() throws Exception {
        Task task = new Task();
        given(taskService.getTask(any())).willReturn(task);

        System.out.println(taskService.getTask(10L).getTitle());
        mockMvc.perform(get("/tasks/10"))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("유효하지 않은 할일에 대하여 상세조회 요청 시  404 에러를 반환한다.")
    public void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/10"))
                .andExpect(status().isNotFound());
    }
}
