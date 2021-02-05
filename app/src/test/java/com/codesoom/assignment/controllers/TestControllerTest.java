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

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
public class TestControllerTest {
    private static final Long GIVEN_ID = 1L;
    private static final String GIVEN_TITLE = "task1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    protected ObjectMapper objectMapper;

    Task createTask() {
        Task task = new Task();
        task.setId(GIVEN_ID);
        task.setTitle(GIVEN_TITLE);
        return task;
    }

    @Nested
    @DisplayName("GET /TASKS는")
    class Describe_getTasks {
        @Nested
        @DisplayName("서비스로 호출하는 Tasks가 존재하면")
        class Context_with_tasks {
            List<Task> tasks = new ArrayList<>();

            @BeforeEach
            void setUp() {
                Task task = new Task();
                task.setId(GIVEN_ID);
                task.setTitle(GIVEN_TITLE);
                tasks.add(task);

                given(taskService.getTasks())
                        .willReturn(tasks);
            }

            @DisplayName("OK 상태와 tasks 목록을 응답한다.")
            @Test
            void it_response_ok_with_tasks() throws Exception {

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(objectMapper.writeValueAsString(tasks)));
            }
        }

        @Nested
        @DisplayName("서비스로 호출하는 Tasks가 없으면")
        class Context_without_tasks {

            @DisplayName("OK 상태와 비어있는 tasks 목록 응답한다.")
            @Test
            void it_response_ok_with_empty_tasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("[]")));
            }
        }
    }

    @Nested
    @DisplayName("GET /TASK는")
    class Describe_getTask {

        @Nested
        @DisplayName("서비스로 호출하는 Task가 없으면")
        class Context_without_task {
            @BeforeEach
            void setUp() {
                given(taskService.getTask(GIVEN_ID))
                        .willThrow(new TaskNotFoundException(GIVEN_ID));
            }

            @DisplayName("Not Found 상태를 응답한다.")
            @Test
            void It_response_not_found() throws Exception {
                mockMvc.perform(get("/tasks/" + GIVEN_ID))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("서비스로 호출하는 Task가 존재하면")
        class Context_with_task {
            Task task = new Task();

            @BeforeEach
            void setUp() {
                task.setId(GIVEN_ID);
                task.setTitle(GIVEN_TITLE);

                given(taskService.getTask(GIVEN_ID))
                        .willReturn(task);
            }

            @DisplayName("OK 상태와 task를 응답한다.")
            @Test
            void it_response_ok_with_task() throws Exception {
                mockMvc.perform(get("/tasks/" + GIVEN_ID))
                        .andExpect(status().isOk())
                        .andExpect(content().string(objectMapper.writeValueAsString(task)));
            }
        }
    }

    @Nested
    @DisplayName("POST /tasks는")
    class Describe_create {
        @Nested
        @DisplayName("생성할 Task가 존재하면")
        class Context_with_task {
            Task task = createTask();

            @Test
            @DisplayName("Created 상태를 응답한다.")
            void It_response_created() throws Exception {
                mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task))
                ).andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("UPDATE /tasks/:id 는")
    class Describe_updateTask {

        @Nested
        @DisplayName("서비스로 갱신되는 Task가 존재하면")
        class Context_with_task {
            @BeforeEach
            void setUp() {
                given(taskService.updateTask(anyLong(), any(Task.class))).willReturn(createTask());
            }

            @DisplayName("OK 상태와 task를 응답한다.")
            @Test
            void it_response_task() throws Exception {
                mockMvc.perform(put("/tasks/{id}", GIVEN_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createTask())))
                        .andExpect(status().isOk());
            }
        }


        @Nested
        @DisplayName("서비스로 갱신되는 Task가 없으면")
        class Context_without_task {
            @BeforeEach
            void setUp() {
                given(taskService.updateTask(anyLong(), any(Task.class)))
                        .willThrow(new TaskNotFoundException(GIVEN_ID));
            }

            @DisplayName("Not Found 상태를 응답한다.")
            @Test
            void It_response_not_found() throws Exception {
                mockMvc.perform(put("/tasks/{id}", GIVEN_ID))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("PATCH /tasks/:id 는")
    class Describe_patchTask {

        @Nested
        @DisplayName("서비스로 갱신되는 Task가 존재하면")
        class Context_with_task {

            @BeforeEach
            void setUp() {
                given(taskService.updateTask(anyLong(), any(Task.class))).willReturn(createTask());
            }

            @DisplayName("OK 상태와 task를 응답한다.")
            @Test
            void it_response_task() throws Exception {
                mockMvc.perform(patch("/tasks/{id}", GIVEN_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createTask())))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists());
            }
        }

        @Nested
        @DisplayName("서비스로 갱신되는 Task가 없으면")
        class Context_without_task {
            @BeforeEach
            void setUp() {
                given(taskService.updateTask(anyLong(), any(Task.class)))
                        .willThrow(new TaskNotFoundException(GIVEN_ID));
            }

            @DisplayName("Not Found 상태를 응답한다.")
            @Test
            void It_response_not_found() throws Exception {
                mockMvc.perform(patch("/tasks/{id}", GIVEN_ID))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/:id 는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("서비스로 삭제되는 Task가 없으면")
        class Context_without_task {
            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(anyLong()))
                        .willThrow(new TaskNotFoundException(anyLong()));
            }

            @DisplayName("Not Found 상태를 응답한다.")
            @Test
            void It_response_not_found() throws Exception {
                mockMvc.perform(delete("/tasks/{id}", GIVEN_ID))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("서비스로 삭제되는 Task가 존재하면")
        class Context_with_task {

            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(anyLong())).willReturn(createTask());
            }

            @DisplayName("NO CONTENT 상태와 task를 응답한다.")
            @Test
            void it_response_no_content_with_task() throws Exception {
                mockMvc.perform(delete("/tasks/{id}", GIVEN_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createTask())))
                        .andExpect(status().isNoContent());
            }
        }
    }
}
