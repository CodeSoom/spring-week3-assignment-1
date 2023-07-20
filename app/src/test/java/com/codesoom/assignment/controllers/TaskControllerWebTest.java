package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.hamcrest.Matchers.containsString;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    @DisplayName("요청한 할일목록 있는 경우 해당할일 응답")
    void list() throws Exception {
        //given
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("test task");
        tasks.add(task);
        given(taskService.getTasks()).willReturn(tasks);

        // expect
        mockMvc.perform(get("/tasks"))
                .andExpect((status().isOk()))
                .andExpect(content().string(containsString("test task")));
    }

    @Test
    @DisplayName("요청한 할일이 있는 경우 200 상태코드와 조회한 할일 응답")
    void detailWithValidId() throws Exception {
        // given
        Task task = new Task();
        given(taskService.getTask(1L)).willReturn(task);

        // expect
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("요청한 할일이 없는 경우 404 상태코드 응답")
    void detailWithInvalidId() throws Exception {
        // given
        given(taskService.getTask(100L))
                .willThrow(new TaskNotFoundException(100L));
        // expect
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("요청한 할일 생성시 201 상태코드 응답 후 생성된 할일 응답")
    void createTask() throws Exception {
        // given
        Task task = new Task();
        task.setTitle("test task");
        given(taskService.createTask(any(Task.class))).willReturn(task);

        // expect
        mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content("{\"title\":\"test task\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("test task")));
    }

    @Test
    @DisplayName("요청한 할일 수정시 200 상태코드 응답 후 수정된 할일 응답")
    void updateTask() throws Exception {
        // given
        Task task = new Task();
        task.setTitle("test task2");
        given(taskService.updateTask(any(Long.class), any(Task.class))).willReturn(task);

        // expect
        mockMvc.perform(put("/tasks/1")
                        .contentType("application/json")
                        .content("{\"title\":\"test task2\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test task2")));
    }

    @Test
    @DisplayName("요청한 할일 수정시 203 상태코드 응답")
    void deleteTask() throws Exception {
        // given
        given(taskService.deleteTask(any(Long.class))).willReturn(null);

        // expect
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
