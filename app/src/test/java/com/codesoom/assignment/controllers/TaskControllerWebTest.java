package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getEmptyListOfTask() throws Exception {
        // given
        List<Task> emptyList = Collections.<Task>emptyList();
        given(taskService.getTasks()).willReturn(emptyList);

        // when
        mockMvc.perform(get("/tasks"))
        // then
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getTaskListWithTwoTask() throws Exception {
        // given
        Task source1 = new Task();
        source1.setId(1L);
        source1.setTitle("Task1");

        Task source2 = new Task();
        source2.setId(2L);
        source2.setTitle("Task2");

        List<Task> taskList = List.of(source1, source2);

        given(taskService.getTasks()).willReturn(taskList);

        // when
        mockMvc.perform(get("/"))
        // then
               .andExpect(status().isOk());
        //TODO: test taskList
    }

    @Test
    public void getTask() throws Exception {
        // given
        Task task = new Task();

        task.setId(1L);
        task.setTitle("task1");
        given(taskService.getTask(task.getId()))
                .willReturn(task);

        // when
        mockMvc.perform(get(String.format("/tasks/%d", task.getId())))
        // then
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1, 'title':'task1'}"));

    }

    @Test
    public void getTaskWithInvalidId() throws Exception {
        // given
        long invalidId = 42L;
        given(taskService.getTask(invalidId))
                .willThrow(new TaskNotFoundException(invalidId));

        // when
        mockMvc.perform(get(String.format("/tasks/%d", invalidId)))
        // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }

    @Test
    public void createTask() throws Exception {
        // given
        Task source = new Task();
        source.setId(1L);
        source.setTitle("task1");
        given(taskService.createTask(source))
                .willReturn(source);
        ObjectMapper objectMapper = new ObjectMapper();
        String sourceJson = objectMapper.writeValueAsString(source);

        // when
        mockMvc.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(sourceJson))
        // then
                .andExpect(status().isCreated())
                .andExpect(content().json(sourceJson));
    }

    @Test
    public void updateTask() throws Exception {
        // given
        Task source = new Task();
        source.setId(1L);
        source.setTitle("task1");
        given(taskService.updateTask(source.getId(), source))
                .willReturn(source);
        ObjectMapper objectMapper = new ObjectMapper();
        String sourceJson = objectMapper.writeValueAsString(source);

        // when
        mockMvc.perform(put(String.format("/tasks/%d", source.getId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(sourceJson))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(sourceJson));

    }

    @Test
    public void patchTask() throws Exception {
        // given
        Task source = new Task();
        source.setId(1L);
        source.setTitle("task1");
        given(taskService.updateTask(source.getId(), source))
                .willReturn(source);
        ObjectMapper objectMapper = new ObjectMapper();
        String sourceJson = objectMapper.writeValueAsString(source);

        // when
        mockMvc.perform(patch(String.format("/tasks/%d", source.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(sourceJson))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(sourceJson));

    }

    @Test
    public void deleteTask() throws Exception {
        // given
        given(taskService.deleteTask(1L))
                .willReturn(null);

        // when
        mockMvc.perform(delete(String.format("/tasks/%d", 1L)))
                // then
                .andExpect(status().isNoContent());

    }
}
