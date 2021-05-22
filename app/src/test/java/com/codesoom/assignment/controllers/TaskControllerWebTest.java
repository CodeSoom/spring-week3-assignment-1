package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController web")
class TaskControllerWebTest {

    private final Long VALID_ID = 1L;
    private final Long INVALID_ID = 999L;

    private final String TITLE = "Test Task";
    private final String UPDATED = "[UPDATED]";

    private Task sourceTask;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() throws Exception {
        List<Task> tasks = new ArrayList<>();

        sourceTask = new Task();
        sourceTask.setId(VALID_ID);
        sourceTask.setTitle(TITLE);
        tasks.add(sourceTask);

//        given(taskService.getTasks()).willReturn(tasks);

//        given(taskService.getTask(VALID_ID)).willReturn(sourceTask);
//
//        given(taskService.getTask(INVALID_ID))
//                .willThrow(new TaskNotFoundException(INVALID_ID));
//
//        given(taskService.deleteTask(VALID_ID)).willReturn(sourceTask);
//
//        given(taskService.deleteTask(INVALID_ID))
//                .willThrow(new TaskNotFoundException(INVALID_ID));
//
//        sourceTask.setTitle(TITLE + UPDATED);
//
//        given(taskService.updateTask(eq(VALID_ID), any(Task.class)))
//                .willReturn(sourceTask);
//
//        given(taskService.updateTask(eq(INVALID_ID), any(Task.class)))
//                .willThrow(new TaskNotFoundException(INVALID_ID));
    }

    @Nested
    @DisplayName("Request GET /tasks")
    class DescribeGetTasks {
        @Nested
        @DisplayName("when exist tasks")
        class ContextWithTasks {
            @BeforeEach
            void prepare() throws Exception {
                List<Task> tasks = new ArrayList<>();
                tasks.add(sourceTask);

                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("It responses tasks array")
            void list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0]").value(sourceTask));
            }
        }

        @Nested
        @DisplayName("when empty tasks")
        class ContextWithEmptyTasks {
            @BeforeEach
            void prepare() throws Exception {
                List<Task> tasks = new ArrayList<>();

                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("It responses empty array")
            void list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));
            }
        }
    }

    @Nested
    @DisplayName("Request GET /tasks/:id")
    class DescribeGetTask {
        @Nested
        @DisplayName("when valid ID")
        class ContextWithTaskValidId {
            @BeforeEach
            void prepare() throws Exception {
                given(taskService.getTask(VALID_ID)).willReturn(sourceTask);
            }

            @Test
            @DisplayName("It responses a task")
            void ItGetTask() throws Exception {
                mockMvc.perform(get("/tasks/" + VALID_ID))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(sourceTask.getId()))
                        .andExpect(jsonPath("$.title").value(sourceTask.getTitle()));
            }
        }

        @Nested
        @DisplayName("when invalid ID")
        class ContextWithTaskInvalidId {
            @BeforeEach
            void prepare() throws Exception {
                given(taskService.getTask(INVALID_ID))
                        .willThrow(new TaskNotFoundException(INVALID_ID));
            }

            @Test
            @DisplayName("It responses not found code")
            void ItGetTask() throws Exception {
                mockMvc.perform(get("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }


    @Test
    @DisplayName("Delete one task matches ID, with valid ID, expected no content")
    void deleteWithValidId() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete one task matches ID, with invalid ID, expected not found")
    void deleteWithInvalidId() throws Exception {
        mockMvc.perform(delete("/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update a task matches ID, with valid ID, expected ok")
    void updateWithValidId() throws Exception {
        String content = objectMapper.writeValueAsString(sourceTask);

        mockMvc.perform(put("/tasks/1")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update a task matches ID, with invalid ID, expected not found")
    void updateWithInvalidId() throws Exception {
        String content = objectMapper.writeValueAsString(sourceTask);

        mockMvc.perform(put("/tasks/999")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Patch a task matches ID, with valid ID, expected ok")
    void patchWithValidId() throws Exception {
        String content = objectMapper.writeValueAsString(sourceTask);

        mockMvc.perform(patch("/tasks/1")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Patch a task matches ID, with invalid ID, expected not found")
    void patchWithInvalidId() throws Exception {
        String content = objectMapper.writeValueAsString(sourceTask);

        mockMvc.perform(patch("/tasks/999")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}