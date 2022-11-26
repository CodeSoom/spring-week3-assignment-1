package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskServiceImpl;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class TaskControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImpl service;

    @BeforeEach
    void before() {
        given(service.getTask(1L))
                .willReturn(new Task(1L, "test"));

        doThrow(TaskNotFoundException.class).when(service).getTask(100L);
    }

    @Test
    @DisplayName("GET Tasks 목록 테스트")
    void getTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());

        verify(service).getTasks();
    }

    @Test
    @DisplayName("GET 해당 Id에 해당하는 Task 얻고 title이 존재하는지 테스트")
    void getTask() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());

        verify(service).getTask(1L);
    }

    @Test
    @DisplayName("GET 존재하지 않는 Id로 Task를 찾을 때 TaskNotFoundException을 던지는지 테스트")
    void getTaskWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                                        .isInstanceOf(TaskNotFoundException.class));
    }

    @Test
    @DisplayName("POST Task 객체 생성 테스트")
    void createTask() throws Exception {
        Task task = new Task(2L, "test2");
        String content = new ObjectMapper().writeValueAsString(task);

        //perform() 안에 조건 집어넣기
        mockMvc.perform(post("/tasks")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("PUT title을 test -> test2 수정 테스트")
    void updateTask() throws Exception {
        mockMvc.perform(put("/tasks/1")
                        .content("{\"title\":\"test2\"}")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("PATCH title을 test -> test3 수정 테스트")
    void patchTask() throws Exception {
        mockMvc.perform(patch("/tasks/1")
                        .content("{\"title\":\"test3\"}")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE id가 1번인 Task 삭제 후 NO_CONTENT 리턴 테스트")
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(service).deleteTask(1L);
    }
}
