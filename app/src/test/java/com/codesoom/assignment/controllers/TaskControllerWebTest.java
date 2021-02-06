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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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

    @Nested
    @DisplayName("GET /tasks/{id} 요청이 오면")
    class Describe_getTasks {
        @Nested
        @DisplayName("id가 존재하는 id이면")
        class Context_exist_id {
            @BeforeEach
            void setup() {
                given(taskService.getTask(ID)).willReturn(task);
            }

            @Test
            @DisplayName("응답코드는 200이며 해당 task를 응답한다")
            void it_return_json() throws Exception {
                mockmvc.perform(get("/tasks/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").value("old title"));
            }
        }

        @Nested
        @DisplayName("id가 없으면")
        class Context_does_not_exist_id {
            @BeforeEach
            void setup() {
                given(taskService.getTask(DOES_NOT_EXIST_ID)).willThrow(new TaskNotFoundException(DOES_NOT_EXIST_ID));
            }

            @Test
            @DisplayName("응답코드는 404이며 Task not found 메세지를 응답한다")
            void it_return_not_found_message() throws Exception {
                mockmvc.perform(get("/tasks/{id}", DOES_NOT_EXIST_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("message").value("Task not found"));
            }
        }
    }

    @Nested
    @DisplayName("POST /tasks/{id} 요청이 오면")
    class Describe_postTasks {
        @Nested
        @DisplayName("task가 있으면")
        class Context_exist_task {
            @BeforeEach
            void setup() {
                given(taskService.createTask(any(Task.class))).willReturn(task);
            }

            @Test
            @DisplayName("응답코드는 201이며 해당 task를 응답한다")
            void it_return_task_json() throws Exception {
                mockmvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(task)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists());
            }
        }

        @Nested
        @DisplayName("task가 없으면")
        class Context_does_not_exist_task {
            @Test
            @DisplayName("bad request를 응답한다")
            void it_return_bad_request() throws Exception {
                mockmvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("PUT /tasks/{id} 요청이 오면")
    class Describe_putTasks {
        @Nested
        @DisplayName("id와 task가 있으면")
        class Context_exist_id_and_task {
            @BeforeEach
            void setup() {
                given(taskService.updateTask(eq(ID), any(Task.class))).willReturn(task);
            }

            @Test
            @DisplayName("응답코드는 200이며 수정된 task를 응답한다")
            void it_return_task_json() throws Exception {
                mockmvc.perform(put("/tasks/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(task)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists());
            }
        }

        @Nested
        @DisplayName("id와 task가 없으면")
        class Context_does_not_exist_id_and_task {
            @BeforeEach
            void setup() {
                given(taskService.updateTask(eq(DOES_NOT_EXIST_ID), any(Task.class))).willThrow(new TaskNotFoundException(DOES_NOT_EXIST_ID));
            }

            @Test
            @DisplayName("응답코드는 404이며 Task not found 메세지를 응답한다")
            void it_return_not_found_message() throws Exception {
                mockmvc.perform(put("/tasks/{id}", DOES_NOT_EXIST_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(task)))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("message").value("Task not found"));
            }
        }
    }

    @Nested
    @DisplayName("PATCH /tasks/{id} 요청이 오면")
    class Describe_patchTasks {
        @Nested
        @DisplayName("id와 task가 있으면")
        class Context_exist_id_and_task {
            @BeforeEach
            void setup() {
                given(taskService.updateTask(eq(ID), any(Task.class))).willReturn(task);

            }

            @Test
            @DisplayName("응답코드는 200이며 수정된 task를 응답한다")
            void it_return_task_json() throws Exception {
                mockmvc.perform(put("/tasks/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(task)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists());
            }
        }

        @Nested
        @DisplayName("id와 task가 없으면")
        class Context_does_not_exist_id_and_task {
            @BeforeEach
            void setup() {
                given(taskService.updateTask(eq(DOES_NOT_EXIST_ID), any(Task.class))).willThrow(new TaskNotFoundException(DOES_NOT_EXIST_ID));
            }

            @Test
            @DisplayName("응답코드는 404이며 Task not found 메세지를 응답한다")
            void it_return_not_found_message() throws Exception {
                mockmvc.perform(put("/tasks/{id}", DOES_NOT_EXIST_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(task)))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("message").value("Task not found"));
            }
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{id} 요청이 오면")
    class Describe_deleteTasks {
        @Nested
        @DisplayName("id가 존재하는 id이면")
        class Context_exist_id {
            @BeforeEach
            void setup() {
                given(taskService.deleteTask(eq(ID))).willReturn(task);
            }

            @Test
            @DisplayName("응답코드는 200이며 해당 task를 응답한다")
            void it_return_task_json() throws Exception {
                mockmvc.perform(delete("/tasks/{id}", ID))
                        .andDo(print())
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("id가 없으면")
        class Context_does_not_exist_id {
            @BeforeEach
            void setup() {
                given(taskService.deleteTask(eq(DOES_NOT_EXIST_ID))).willThrow(new TaskNotFoundException(DOES_NOT_EXIST_ID));
            }

            @Test
            @DisplayName("응답코드는 404이며 Task not found 메세지를 응답한다")
            void it_return_not_found_message() throws Exception {
                mockmvc.perform(delete("/tasks/{id}", DOES_NOT_EXIST_ID))
                        .andDo(print())
                        .andExpect(status().isNotFound());
            }
        }
    }
}

