package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
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
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
class TaskControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final long givenID = 1L;
    private final long givenNotExistID = 100L;
    private final String givenTitle = "sample";
    private final String givenModifyTitle = "modify sample";

    private Task givenTask(long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        return task;
    }

    @Nested
    @DisplayName("[GET] /tasks 메서드는")
    class Describe_list {

        @Nested
        @DisplayName("등록된 Task 가 하나도 없을 때")
        class Context_without_task {
            @BeforeEach
            void setup() {
                List<Task> tasks = new ArrayList<>();

                given(taskService.getTasks())
                        .willReturn(tasks);
            }

            @Test
            @DisplayName("빈 집합을 응답한다.")
            void It_respond_void_array() throws Exception {
                final String expectContent = "[]";

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().json(expectContent));
            }
        }

        @Nested
        @DisplayName("등록된 Task 가 있을 때")
        class Context_with_task {
            @BeforeEach
            void setup() {
                Task task = givenTask(givenID, givenTitle);

                List<Task> tasks = new ArrayList<>();
                tasks.add(task);

                given(taskService.getTasks())
                        .willReturn(tasks);
            }

            @Test
            @DisplayName("Task 의 집합을 응답한다.")
            void It_respond_exists_array() throws Exception {
                final String expectContent = String
                        .format("[{\"title\":\"%s\",\"id\":%d}]", givenTitle, givenID);

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().json(expectContent));
            }
        }
    }

    @Nested
    @DisplayName("[GET] /tasks/{id} 메서드는")
    class Describe_detail {

        @Nested
        @DisplayName("찾는 id가 없을 때")
        class Context_without_target_id {
            @BeforeEach
            void setup() {
                given(taskService.getTask(givenNotExistID))
                        .willThrow(new TaskNotFoundException(givenNotExistID));
            }

            @Test
            @DisplayName("status not found 를 응답한다.")
            void It_respond_task_not_found_exception() throws Exception {
                mockMvc.perform(get("/tasks/{id}", givenNotExistID))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("찾는 id가 있을 때")
        class Context_with_target_id {
            @BeforeEach
            void setup() {
                Task task = givenTask(givenID, givenTitle);

                given(taskService.getTask(any(long.class)))
                        .willReturn(task);
            }

            @Test
            @DisplayName("Task 를 응답한다.")
            void It_respond_task() throws Exception {
                final String expectContent = String
                        .format("{\"title\":\"%s\",\"id\":%d}", givenTitle, givenID);

                mockMvc.perform(get("/tasks/{id}", givenID))
                        .andExpect(status().isOk())
                        .andExpect(content().json(expectContent));
            }
        }
    }

    @Nested
    @DisplayName("[POST] /tasks 메서드는")
    class Describe_create {
        @BeforeEach
        void setup() {
            Task task = givenTask(givenID, givenTitle);

            given(taskService.createTask(any(Task.class)))
                    .willReturn(task);
        }

        @Test
        @DisplayName("생성된 Task 를 응답한다.")
        void It_respond_created_task() throws Exception {
            String postContent = String.format("{\"title\":\"%s\"}", givenTitle);
            String expectContent = String.format("{\"title\":\"%s\",\"id\":%d}", givenTitle, givenID);

            mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(postContent))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(expectContent));
        }
    }

    @Nested
    @DisplayName("[PUT] /tasks/{id} 메서드는")
    class Describe_update {

        @Nested
        @DisplayName("대상 id가 없을 때")
        class Context_without_target_id {
            @BeforeEach
            void setup() {
                given(taskService.updateTask(any(long.class), any(Task.class)))
                        .willThrow(new TaskNotFoundException(givenNotExistID));
            }

            @Test
            @DisplayName("status not found 를 응답한다.")
            void It_respond_task_not_found_exception() throws Exception {
                String postContent = String.format("{\"title\":\"%s\"}", givenModifyTitle);

                mockMvc.perform(
                        put("/tasks/{id}", givenNotExistID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(postContent)
                ).andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("대상 id가 있을 때")
        class Context_with_target_id {
            @BeforeEach
            void setup() {
                Task task = givenTask(givenID, givenModifyTitle);

                given(taskService.updateTask(any(long.class), any(Task.class)))
                        .willReturn(task);
            }

            @Test
            @DisplayName("변경된 Task 를 응답한다.")
            void It_respond_modified_task() throws Exception {
                String postContent = String.format("{\"title\":\"%s\"}", givenModifyTitle);
                String expectContent = String.format("{\"title\":\"%s\",\"id\":%d}", givenModifyTitle, givenID);

                mockMvc.perform(put("/tasks/{id}", givenID).contentType(MediaType.APPLICATION_JSON).content(postContent))
                        .andExpect(status().isOk())
                        .andExpect(content().json(expectContent));
            }
        }
    }

    @Nested
    @DisplayName("[PATCH] /tasks/{id} 메서드는")
    class Describe_patch {

        @Nested
        @DisplayName("대상 id가 없을 때")
        class Context_without_target_id {
            @BeforeEach
            void setup() {
                given(taskService.updateTask(any(long.class), any(Task.class)))
                        .willThrow(new TaskNotFoundException(givenNotExistID));
            }

            @Test
            @DisplayName("status not found 를 응답한다.")
            void It_respond_task_not_found_exception() throws Exception {
                String postContent = String.format("{\"title\":\"%s\"}", givenModifyTitle);

                mockMvc.perform(
                        patch("/tasks/{id}", givenNotExistID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(postContent)
                ).andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("대상 id가 있을 때")
        class Context_with_target_id {
            @BeforeEach
            void setup() {
                Task task = givenTask(givenID, givenModifyTitle);

                given(taskService.updateTask(any(long.class), any(Task.class)))
                        .willReturn(task);
            }

            @Test
            @DisplayName("변경된 Task 를 응답한다.")
            void It_respond_modified_task() throws Exception {
                String postContent = String.format("{\"title\":\"%s\"}", givenModifyTitle);
                String expectContent = String.format("{\"title\":\"%s\",\"id\":%d}", givenModifyTitle, givenID);

                mockMvc.perform(patch("/tasks/{id}", givenID).contentType(MediaType.APPLICATION_JSON).content(postContent))
                        .andExpect(status().isOk())
                        .andExpect(content().json(expectContent));
            }
        }
    }

    @Nested
    @DisplayName("[DELETE] /tasks/{id} 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("대상 id가 없을 때")
        class Context_without_target_id {
            @BeforeEach
            void setup() {
                given(taskService.deleteTask(givenNotExistID))
                        .willThrow(new TaskNotFoundException(givenNotExistID));
            }

            @Test
            @DisplayName("Task 를 찾을 수 없다는 예외를 응답한다.")
            void It_respond_task_not_found_exception() throws Exception {
                mockMvc.perform(delete("/tasks/{id}", givenNotExistID))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("대상 id가 있을 때")
        class Context_with_target_id {
            @BeforeEach
            void setup() {
                given(taskService.getTask(givenID))
                        .willThrow(new TaskNotFoundException(givenID));
            }

            @Test
            @DisplayName("삭제 후 대상 id를 조회하면 status not found 를 응답한다.")
            void It_respond_modified_task() throws Exception {
                mockMvc.perform(delete("/tasks/{id}", givenID))
                        .andExpect(status().isNoContent());

                mockMvc.perform(get("/tasks/{id}", givenID))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
