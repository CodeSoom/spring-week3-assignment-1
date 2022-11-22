package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private TaskService service;

    @BeforeEach
    void before() {
        given(service.getTask(1L))
                .willReturn(Task.builder()
                                .id(1L)
                                .title("test")
                                .build());

        given(service.getTask(100L))
                .willThrow(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Tasks 목록 얻기")
    void getTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());

        verify(service).getTasks();
    }

    @Test
    @DisplayName("해당 Id에 해당하는 Task 얻기")
    void getTask() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andDo(print());

        verify(service).getTask(1L);
    }

    @Test
    @DisplayName("존재하지 않는 Id로 Task를 찾을 때 4xx 에러 날리기")
    void getTaskWithWrongId() throws Exception {
        mockMvc.perform(get("/tasks/{id}", 100L))
                .andExpect(
                        result -> assertThatThrownBy(() -> service.getTask(100L))
                                .isInstanceOf(TaskNotFoundException.class)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("Task 객체 생성하기")
    void createTask() throws Exception {
        Task task = Task.builder().id(2L).title("test2").build();
        String content = new ObjectMapper().writeValueAsString(task);
        log.info("content={}", content);

        //perform() 안에 조건 집어넣기
        mockMvc.perform(post("/tasks")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("title test -> test2 수정하기")
    void updateTask() throws Exception {
        //TODO  - content 어떻게 집어넣을지?
        //      - update 후 수정된 title이 test2로 되어있는지 확인하는 방법 찾기

        mockMvc.perform(put("/tasks/1")
                .content("{\"title\":\"test2\"}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("test2"))
                .andDo(print());
    }

    @Test
    @DisplayName("id가 1번인 Task 삭제하기")
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(service).deleteTask(1L);
    }
}
