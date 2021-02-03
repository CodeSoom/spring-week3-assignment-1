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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    @Autowired
    private MockMvc mockmvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private TaskService taskService;
    private List<Task> tasks;
    private Task task;
    final Long ID = 1L;
    final Long DOES_NOT_EXIST_ID = 11L;

    @BeforeEach
    void setup() {
        tasks = new ArrayList<>();
        task = new Task();
        task.setId(ID);
        task.setTitle("old title");
        tasks.add(task);
    }

    @Test
    @DisplayName("GET /tasks 요청")
    void list() throws Exception {
        given(taskService.getTasks()).willReturn(tasks);

        mockmvc.perform(get("/tasks")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(tasks)));
    }

    @Test
    @DisplayName("GET /tasks/{id} 존재하는 id 요청")
    void detail() throws Exception {
        given(taskService.getTask(ID)).willReturn(task);

        mockmvc.perform(get("/tasks/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").value("old title"));
    }

    @Test
    @DisplayName("GET /tasks/{id} 없는 id 요청")
    void detaiDoesNotExistId() throws Exception {
        given(taskService.getTask(DOES_NOT_EXIST_ID)).willThrow(new TaskNotFoundException(DOES_NOT_EXIST_ID));

        mockmvc.perform(get("/tasks/{id}", DOES_NOT_EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("id").doesNotExist())
                .andExpect(jsonPath("title").doesNotExist());
    }

    @Test
    @DisplayName("POST /tasks task 있으면")
    void create() throws Exception {
        given(taskService.createTask(any(Task.class))).willReturn(task);

        mockmvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(task)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists());
    }

    @Test
    @DisplayName("POST /tasks task 없으면 bad request")
    void createDoesNotExistTask() throws Exception {
        mockmvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /tasks id, task가 주어지면")
    void update() throws Exception {
        given(taskService.updateTask(eq(ID), any(Task.class))).willReturn(task);

        mockmvc.perform(put("/tasks/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(task)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists());
    }

    @Test
    @DisplayName("PUT /tasks 존재하지 않는 id")
    void updateDoesNotExistId() throws Exception {
        given(taskService.updateTask(eq(DOES_NOT_EXIST_ID), any(Task.class))).willThrow(new TaskNotFoundException(DOES_NOT_EXIST_ID));

        mockmvc.perform(put("/tasks/{id}", DOES_NOT_EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(task)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /tasks id, task가 주어지면")
    void patch() throws Exception {
        given(taskService.updateTask(eq(ID), any(Task.class))).willReturn(task);

        mockmvc.perform(put("/tasks/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(task)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists());
    }

    @Test
    @DisplayName("PATCH /tasks 존재하지 않는 id")
    void patchDoesNotExistId() throws Exception {
        given(taskService.updateTask(eq(DOES_NOT_EXIST_ID), any(Task.class))).willThrow(new TaskNotFoundException(DOES_NOT_EXIST_ID));

        mockmvc.perform(put("/tasks/{id}", DOES_NOT_EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(task)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /tasks/{id}  id가 존재하면")
    void deleteOk() throws Exception {
        given(taskService.deleteTask(eq(ID))).willReturn(task);

        mockmvc.perform(delete("/tasks/{id}", ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        when(taskService.getTask(ID)).thenThrow(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("DELETE /tasks/{id} 존재하지 않는 id")
    void deleteDoesNotExistId() throws Exception {
        given(taskService.deleteTask(eq(DOES_NOT_EXIST_ID))).willThrow(new TaskNotFoundException(DOES_NOT_EXIST_ID));

        mockmvc.perform(delete("/tasks/{id}", DOES_NOT_EXIST_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
