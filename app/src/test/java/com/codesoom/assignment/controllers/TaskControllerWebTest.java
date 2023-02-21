package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private static final String TITLE = "TEST_TITLE";

    private static final String UPDATE_TITLE = "UPDATE_TITLE";

    private static final Long DEFAULT_ID = 1L;

    private static final Long CREATE_ID = 2L;

    private static final Long INVALID_ID = 100L;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setTitle(TITLE);
        task.setId(DEFAULT_ID);
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(DEFAULT_ID)).willReturn(task);
        given(taskService.getTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));

        Task createTask = new Task();
        createTask.setId(CREATE_ID);
        createTask.setTitle(TITLE);
        given(taskService.createTask(any(Task.class))).willReturn(createTask);

        Task updateTask = new Task();
        updateTask.setId(DEFAULT_ID);
        updateTask.setTitle(UPDATE_TITLE);
        given(taskService.updateTask(eq(DEFAULT_ID), any(Task.class))).willReturn(updateTask);
        given(taskService.updateTask(eq(INVALID_ID), any(Task.class))).willThrow(new TaskNotFoundException(INVALID_ID));

        given(taskService.deleteTask(DEFAULT_ID)).willReturn(null);
        given(taskService.deleteTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));

    }
    @Test
    void list() throws Exception {

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":1,\"title\":\"TEST_TITLE\"}]"));
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/"+DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"title\":\"TEST_TITLE\"}"));
    }

    @Test
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/"+INVALID_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"message\":\"Task not found\"}"));
   }

    @Test
    void create() throws Exception {

        Task resource = new Task();
        resource.setTitle(TITLE);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title",TITLE).exists());

  }

    @Test
    void updateWithValidId() throws Exception {

        Task updateTask = new Task();
        updateTask.setTitle(UPDATE_TITLE);

        mockMvc.perform(put("/tasks/"+DEFAULT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title",UPDATE_TITLE).exists())
                .andExpect(jsonPath("id",DEFAULT_ID).exists());

   }

    @Test
    void updateWithInvalidId() throws Exception {
        Task updateTask = new Task();
        updateTask.setTitle(UPDATE_TITLE);

        mockMvc.perform(put("/tasks/"+INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"message\":\"Task not found\"}"));
   }

    @Test
    void patchWithValidId() throws Exception{
        Task updateTask = new Task();
        updateTask.setTitle(UPDATE_TITLE);

        mockMvc.perform(patch("/tasks/"+DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title",UPDATE_TITLE).exists())
                .andExpect(jsonPath("id",DEFAULT_ID).exists());
   }

    @Test
    void patchWithInvalidId() throws Exception {
        Task updateTask = new Task();
        updateTask.setTitle(UPDATE_TITLE);

        mockMvc.perform(patch("/tasks/"+INVALID_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"message\":\"Task not found\"}"));


    }

    @Test
    void deleteWithValidId() throws Exception {

        mockMvc.perform(delete("/tasks/"+DEFAULT_ID))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteWithInvalidId() throws Exception {

        mockMvc.perform(delete("/tasks/"+INVALID_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"message\":\"Task not found\"}"));
    }
}
