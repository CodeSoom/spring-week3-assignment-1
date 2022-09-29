package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.appllication.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerMockMVCTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    private Task updateTask;
    private Task createTask;
    private Task createTask2;
    private final String TEST_TITLE = "TEST_TITLE";


    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.updateTitle(TEST_TITLE);
        tasks.add(task);

        updateTask = new Task(1L, "update");
        createTask = new Task(null, TEST_TITLE);
        createTask2 = new Task(1L, TEST_TITLE);
        given(taskService.updateTask(1L, updateTask)).willReturn(updateTask);
        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(task);
        given(taskService.getTask(10L)).willThrow(new TaskNotFoundException(10L));
        given(taskService.deleteTask(1L)).willReturn(task);
        given(taskService.deleteTask(100L)).willThrow(new TaskNotFoundException(100L));
        given(taskService.createTask(createTask)).willReturn(createTask2);


    }

    @Test
    @DisplayName("GET:tasks가져오기")
    void getList() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TEST_TITLE)));
    }

    @Test
    @DisplayName("GET:id로 task가져오기")
    void getListById() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TEST_TITLE)));
    }

    @Test
    @DisplayName("GET:실패")
    void getListByIdFail() throws Exception {
        mockMvc.perform(get("/tasks/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST:task객체 생성 및 tasks에 추가")
    void createTask() throws Exception {
        String content = new ObjectMapper().writeValueAsString(createTask);

        mockMvc.perform(post("/tasks")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("PUT:task객체 title업데이트")
    void updateTask() throws Exception {

        String content = new ObjectMapper().writeValueAsString(updateTask);

        mockMvc.perform(put("/tasks/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("DELETE:실패시 NotFound")
    void failedDeleteTask() throws Exception {
        mockMvc.perform(
                delete("/tasks/100"))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("DELETE:성공시 isNoContent")
    void DeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

    }

}

