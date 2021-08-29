package com.codesoom.assignment.controllers.taskController;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public final class TaskControllerWebTest extends TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(validId)).willReturn(task);
        given(taskService.getTask(invalidId))
                .willThrow(new TaskNotFoundException(invalidId));
        given(taskService.updateTask(eq(invalidId), any(Task.class)))
                .willThrow(new TaskNotFoundException(invalidId));
        given(taskService.deleteTask(eq(invalidId)))
                .willThrow(new TaskNotFoundException(invalidId));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(taskTitle)));

        verify(taskService).getTasks();
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService).getTask(validId);
    }

    @Test
    void detailWithInValidId() throws Exception {
        mockMvc.perform(get("/tasks/2"))
                .andExpect(status().isNotFound());
        
        verify(taskService).getTask(invalidId);
    }

    @Test
    void createTask() throws Exception {
        mockMvc.perform(
            post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"New Task\"}")
            )
            .andExpect(status().isCreated());

        verify(taskService).createTask(any(Task.class));
    }

    @Test
    void updateExistedTask() throws Exception {
        mockMvc.perform(
            patch("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Renamed Task\"}")
            )
            .andExpect(status().isOk());

        verify(taskService).updateTask(eq(validId), any(Task.class));
    }

    @Test
    void updateNotExistedTask() throws Exception {
        mockMvc.perform(
            patch("/tasks/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Renamed Task\"}")
            )
            .andExpect(status().isNotFound());

        verify(taskService).updateTask(eq(invalidId), any(Task.class));
    }

    @Test
    void deleteExistedTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
            .andExpect(status().isNoContent());

        verify(taskService).deleteTask(eq(validId));
    }

    @Test
    void deleteNotExistedTask() throws Exception {
        mockMvc.perform(delete("/tasks/2"))
            .andExpect(status().isNotFound());

        verify(taskService).deleteTask(eq(invalidId));
    }

}
