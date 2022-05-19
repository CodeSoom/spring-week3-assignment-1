package com.codesoom.assignment.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("Task 1 Title");
        tasks.add(task);

        given(taskService.getTask(1L)).willReturn(task);

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Task 1 Title")));
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Task 1 Title")));
    }

    @Test
    void detailWithInValidId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask() throws Exception {
        String taskTitle = "New Task";

        Task sourceTask = new Task();
        sourceTask.setTitle(taskTitle);

        Task createdTask = new Task();
        createdTask.setId(1L);
        createdTask.setTitle(taskTitle);

        given(taskService.createTask(sourceTask)).willReturn(createdTask);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJsonString(sourceTask))
                        .accept(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(taskTitle)))
                .andDo(print());
    }

    @Test
    void updateTask() throws Exception {
        String sourceTaskTitle = "Source Task Title";
        String updatedTaskTitle = "Updated Task Title";

        Task sourceTask = new Task();
        sourceTask.setId(1L);
        sourceTask.setTitle(sourceTaskTitle);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle(updatedTaskTitle);

        given(taskService.updateTask(1L, sourceTask)).willReturn(updatedTask);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJsonString(updatedTask))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(updatedTaskTitle)));

        mockMvc.perform(patch("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJsonString(updatedTask))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(updatedTaskTitle)));
    }

    @Test
    void deleteTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Delete Task");

        given(taskService.deleteTask(1l)).willReturn(task);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    private String convertToJsonString(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
