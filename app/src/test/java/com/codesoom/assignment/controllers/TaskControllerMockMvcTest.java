package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService service;

    @BeforeEach
    void before() {
        Task task = new Task();
        task.setTitle("test");
        service.createTask(task);
    }

    @Test
    @DisplayName("Tasks 목록 얻기")
    void getTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("해당 Id에 해당하는 Task 얻기")
    void getTask() throws Exception {
        Task task = new Task();
        task.setTitle("test");
        Task resultTask = service.createTask(task);
        Long id = resultTask.getId();

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isOk());
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
        //perform() 안에 조건 집어넣기
        mockMvc.perform(post("/tasks")
                        .content("{\"title\":\"test\"}")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("title test -> test2 수정하기")
    void updateTask() throws Exception {
        mockMvc.perform(put("/tasks/1")
                .content("{\"title\":\"test2\"}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("id가 1번인 Task 삭제하기")
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
