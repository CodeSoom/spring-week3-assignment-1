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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("/tasks 모킹 테스트")
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    private final Long validTaskId = 1L;
    private final Long addtionalValidTaskId = 2L;
    private final Long invalidTaskId = 100L;
    private final String validTaskTitle = "Test1";
    private final String additionalValidTaskTitle = "Test2";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setId(validTaskId);
        task.setTitle(validTaskTitle);
        tasks.add(task);

        given(taskService.createTask(any(Task.class))).willReturn(task);
        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(validTaskId)).willReturn(task);

        Task newTask = new Task();
        newTask.setId(validTaskId);
        newTask.setTitle(additionalValidTaskTitle);

        given(taskService.updateTask(any(Long.class), any(Task.class))).willReturn(newTask);
    }

    @Test
    @DisplayName("전체 할 일 목록을 조회한다.")
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(validTaskTitle)));

        verify(taskService).getTasks();
    }

    @Test
    @DisplayName("할 일 목록에 등록된 할 일을 조회한다.")
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/" + validTaskId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("title").value(containsString(validTaskTitle)));

        verify(taskService).getTask(validTaskId);
    }

    @Test
    @DisplayName("할 일 목록에 없는 할 일을 조회한다.")
    void detailWithInvalidId() throws Exception {
        given(taskService.getTask(invalidTaskId)).willThrow(new TaskNotFoundException(invalidTaskId));

        mockMvc.perform(get("/tasks/" + invalidTaskId))
               .andExpect(status().isNotFound());

        verify(taskService).getTask(invalidTaskId);
    }

    @Test
    @DisplayName("새로운 할 일을 등록한다.")
    void createNewTask() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + validTaskTitle + "\"}");

        mockMvc.perform(requestBuilder)
               .andExpect(status().isCreated())
               .andExpect(content().string(containsString(validTaskTitle)));

        verify(taskService).createTask(any(Task.class));
    }

    @Test
    @DisplayName("지정한 할 일을 갱신한다.")
    void updateTask() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                put("/tasks/" + validTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + additionalValidTaskTitle + "\"}");

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(additionalValidTaskTitle)));

        verify(taskService).updateTask(eq(validTaskId), any(Task.class));
    }

    @Test
    @DisplayName("지정한 할 일을 삭제한다.")
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/" + validTaskId))
               .andExpect(status().isNoContent());

        verify(taskService).deleteTask(validTaskId);
    }
}
