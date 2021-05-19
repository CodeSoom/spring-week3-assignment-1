package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    private static final String ROOT_PATH = "/";
    private static final String TASK_PATH = "tasks";
    private static final String TASK_TITLE = "test";
    private static final String TASK_CREATE_PREFIX = "new";
    private static final String TASK_UPDATE_PREFIX = "fix";
    private static final Long EXIST_ID = 1L;
    private static final Long WRONG_ID = -1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        List<Task> taskList = new ArrayList<>();
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskList.add(task);

        given(taskService.getTasks()).willReturn(taskList);
        given(taskService.getTask(EXIST_ID)).willReturn(task);
        given(taskService.getTask(WRONG_ID)).willThrow(new TaskNotFoundException(WRONG_ID));
    }

    @Test
    @DisplayName("모든 Task 목록 요청 - GET")
    void getTaskList() throws Exception {
        mockMvc.perform(get(ROOT_PATH + TASK_PATH))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(TASK_TITLE)));
    }

    @Test
    @DisplayName("Valid Id 값으로 Task 상세정보 요청 - GET")
    void detailWithValidId() throws Exception {
        mockMvc.perform(get(ROOT_PATH + TASK_PATH + ROOT_PATH + EXIST_ID))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(TASK_TITLE)));
    }

    @Test
    @DisplayName("Invalid Id 값으로 Task 상세정보 요청 - GET")
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get(ROOT_PATH + TASK_PATH + ROOT_PATH + WRONG_ID))
               .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("새 Task 등록 요청 - POST")
    void createTask() throws Exception {
        Task task = new Task();
        task.setTitle(TASK_CREATE_PREFIX + TASK_TITLE);
        String taskContent = objectMapper.writeValueAsString(task);

        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post(ROOT_PATH + TASK_PATH)
                       .content(taskContent)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(content().string(containsString(TASK_CREATE_PREFIX + TASK_TITLE)));
    }

    @Test
    @DisplayName("Valid Id 값으로 Task 수정 요청 - PUT")
    void updateTaskWithValidId() throws Exception {
        Task requestTask = new Task();
        requestTask.setTitle(TASK_UPDATE_PREFIX + TASK_TITLE);
        String taskContent = objectMapper.writeValueAsString(requestTask);

        when(taskService.updateTask(eq(EXIST_ID), any(Task.class))).thenReturn(requestTask);

        mockMvc.perform(put(ROOT_PATH + TASK_PATH + ROOT_PATH + EXIST_ID)
                       .content(taskContent)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(TASK_UPDATE_PREFIX + TASK_TITLE)));
    }

    @Test
    @DisplayName("Invalid Id 값으로 Task 수정 요청 - PUT")
    void updateTaskWithInvalidId() throws Exception {
        Task requestTask = new Task();
        requestTask.setTitle(TASK_UPDATE_PREFIX + TASK_TITLE);
        String taskContent = objectMapper.writeValueAsString(requestTask);

        when(taskService.updateTask(eq(WRONG_ID), any(Task.class))).thenThrow(new TaskNotFoundException(WRONG_ID));

        mockMvc.perform(put(ROOT_PATH + TASK_PATH + ROOT_PATH + WRONG_ID)
                       .content(taskContent)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(content().string(containsString("Task not found")));
    }

    @Test
    @DisplayName("Valid Id 값으로 Task 수정 요청 - PATCH")
    void patchTaskWithValidId() throws Exception {
        Task requestTask = new Task();
        requestTask.setTitle(TASK_UPDATE_PREFIX + TASK_TITLE);
        String taskContent = objectMapper.writeValueAsString(requestTask);

        when(taskService.updateTask(eq(EXIST_ID), any(Task.class))).thenReturn(requestTask);

        mockMvc.perform(patch(ROOT_PATH + TASK_PATH + ROOT_PATH + EXIST_ID)
                       .content(taskContent)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(TASK_UPDATE_PREFIX + TASK_TITLE)));
    }

    @Test
    @DisplayName("Invalid Id 값으로 Task 수정 요청 - PATCH")
    void patchTaskWithInvalidId() throws Exception {
        Task requestTask = new Task();
        requestTask.setTitle(TASK_UPDATE_PREFIX + TASK_TITLE);
        String taskContent = objectMapper.writeValueAsString(requestTask);

        when(taskService.updateTask(eq(WRONG_ID), any(Task.class))).thenThrow(new TaskNotFoundException(WRONG_ID));

        mockMvc.perform(put(ROOT_PATH + TASK_PATH + ROOT_PATH + WRONG_ID)
                       .content(taskContent)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(content().string(containsString("Task not found")));
    }

    @Test
    @DisplayName("Valid Id 값으로 Task 삭제 요청 - DELETE")
    void deleteTaskWithValidId() throws Exception {
        Task task = new Task();
        when(taskService.deleteTask(eq(EXIST_ID))).thenReturn(task);

        mockMvc.perform(delete(ROOT_PATH + TASK_PATH + ROOT_PATH + EXIST_ID))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Invalid Id 값으로 Task 삭제 요청 - DELETE")
    void deleteTaskWithInvalidId() throws Exception {
        when(taskService.deleteTask(eq(WRONG_ID))).thenThrow(new TaskNotFoundException(WRONG_ID));

        mockMvc.perform(delete(ROOT_PATH + TASK_PATH + ROOT_PATH + WRONG_ID))
               .andExpect(status().isNotFound())
               .andExpect(content().string(containsString("Task not found")));
    }
}
