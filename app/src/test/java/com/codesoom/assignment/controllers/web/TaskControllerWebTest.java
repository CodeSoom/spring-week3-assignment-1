package com.codesoom.assignment.controllers.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    private static final String DEFAULT_TASK_TITLE = "TASK";
    private static final String NEW_TASK_TITLE = "NEW " + DEFAULT_TASK_TITLE;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        Task task = new Task(1L, DEFAULT_TASK_TITLE);
        List<Task> tasks = Collections.singletonList(task);

        given(taskService.getTasks())
            .willReturn(tasks);
        given(taskService.getTask(1L))
            .willReturn(task);
        given(taskService.getTask(2L))
            .willThrow(new TaskNotFoundException(2L));
        given(taskService.createTask(task))
            .willReturn(task);

        Task newTask = new Task(2L, DEFAULT_TASK_TITLE);

        given(taskService.updateTask(1L, newTask))
            .willReturn(new Task(1L, DEFAULT_TASK_TITLE));
        given(taskService.updateTask(2L, newTask))
            .willThrow(new TaskNotFoundException(2L));
        given(taskService.deleteTask(1L))
            .willReturn(task);
    }

    @Test
    @DisplayName("GET /tasks는 모든 할 일 목록을 반환한다")
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(DEFAULT_TASK_TITLE)));
    }

    @Test
    @DisplayName("GET /tasks/{id}는 존재하지 않는 id일 경우 404를 반환한다")
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/2"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /tasks/{id}는 존재하는 id일 경우 200을 반환한다")
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /tasks는 201을 반환한다")
    void createTask() throws Exception {
        Task task = new Task(1L, DEFAULT_TASK_TITLE);

        mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toString(task)))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("PUT /tasks/{id}는 존재하지 않는 id일 경우 404을 반환한다")
    void updateTaskPutWithInvalidId() throws Exception {
        Task task = new Task(2L, DEFAULT_TASK_TITLE);

        mockMvc.perform(put("/tasks/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toString(task)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /tasks/{id}는 존재하는 id일 경우 200을 반환한다")
    void updateTaskPutWithValidId() throws Exception {
        Task task = new Task(1L, NEW_TASK_TITLE);

        mockMvc.perform(put("/tasks/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toString(task)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /tasks/{id}는 존재하지 않는 id일 경우 404을 반환한다")
    void updateTaskPatchWithInvalidId() throws Exception {
        Task task = new Task(2L, DEFAULT_TASK_TITLE);

        mockMvc.perform(patch("/tasks/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toString(task)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /tasks/{id}는 존재하는 id일 경우 200을 반환한다")
    void updateTaskPatch() throws Exception {
        Task task = new Task(1L, NEW_TASK_TITLE);

        mockMvc.perform(patch("/tasks/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toString(task)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /tasks/{id}는 204를 반환한다")
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
            .andExpect(status().isNoContent());
    }

    private static String toString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
