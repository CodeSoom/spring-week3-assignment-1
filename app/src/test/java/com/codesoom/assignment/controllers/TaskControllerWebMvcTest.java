package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class TaskControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        List<Task> tasks = new ArrayList<>();
        
        Task beforeTask = new Task();
        beforeTask.setTitle("Test task");
        tasks.add(beforeTask);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(beforeTask);
        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
    }

    @Test
    @DisplayName("전체 Tasks를 조회한다")
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test task")));
    }

    @Test
    @DisplayName("Task를 조회할 때 id가 존재한다면  OK를 반환한다")
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test task")));
    }

    @Test
    @DisplayName("Task를 조회할 때 id가 존재하지 않는다면  NOT_FOUND를 반환한다")
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("새로운 Task를 생성한다.")
    void create() throws Exception {
        Task task = new Task();
        task.setTitle("Second");
        given(taskService.createTask(any())).willReturn(task);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Second\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Task를 수정할 때 id가 존재한다면  OK를 반환한다.")
    void updateWIthValidId() throws Exception {
        mockMvc.perform(patch("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"new\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Task를 수정할 때 id가 존재하지 않는다면  NOT_FOUND를 반환한다")
    void updateWIthInvalidId() throws Exception {
        mockMvc.perform(patch("/tasks/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"new\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Task를 삭제할 때 id가 존재한다면  NO_CONTENT를 반환한다")
    void deleteWithValidId() throws Exception {
        mockMvc.perform(delete("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Task를 삭제할 때 id가 존재하지 않는다면  NOT_FOUND를 반환한다")
    void deleteWithInvalidId() throws Exception {
        mockMvc.perform(delete("/tasks/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
