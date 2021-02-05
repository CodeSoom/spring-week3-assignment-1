package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    TaskService taskService;

    final long givenID = 1L;
    final String givenTitle = "sample";
    final String givenModifyTitle = "modify sample";

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {

        @Nested
        @DisplayName("등록된 Task 가 하나도 없을 때")
        class Context_without_task {

            @Test
            @DisplayName("빈 집합을 리턴한다.")
            void It_returns_void_array() throws Exception {
                when(taskService.getTasks())
                        .thenReturn(new ArrayList<>());

                mockMvc.perform(get("/tasks"))
                        .andExpect(content().json("[]"));
            }
        }

        @Nested
        @DisplayName("등록된 Task 가 있을 때")
        class Context_with_task {

            @Test
            @DisplayName("Task 의 집합을 리턴한다.")
            void It_returns_exists_array() throws Exception {
                Task task = new Task();
                task.setTitle(givenTitle);

                when(taskService.getTasks())
                        .thenReturn(new ArrayList<>(Collections.singletonList(task)));

                mockMvc.perform(get("/tasks"))
                        .andExpect(content().json(String.format("[{\"title\":\"%s\",\"id\":%d}]", givenTitle, givenID)));
            }
        }
    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {

        @Nested
        @DisplayName("찾는 id가 없을 때")
        class Context_without_target_id {

            @Test
            @DisplayName("status not found 를 던진다.")
            void It_throws_task_not_found_exception() throws Exception {
                when(taskService.getTask(givenID))
                        .thenThrow(new TaskNotFoundException(givenID));

                mockMvc.perform(get("/tasks/{id}", givenID))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("찾는 id가 있을 때")
        class Context_with_target_id {

            @Test
            @DisplayName("Task 를 리턴한다.")
            void It_returns_task() throws Exception {
                Task task = new Task();
                task.setTitle(givenTitle);
                task.setId(givenID);

                when(taskService.getTask(givenID))
                        .thenReturn(task);

                mockMvc.perform(get("/tasks/{id}", givenID))
                        .andExpect(content().json(String.format("{\"title\":\"%s\",\"id\":%d}", givenTitle, givenID)));
            }
        }
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        @Test
        @DisplayName("생성된 Task 를 리턴한다.")
        void It_returns_created_task() throws Exception {
            Task task = new Task();
            task.setTitle(givenTitle);
            task.setId(givenID);

            when(taskService.createTask(any()))
                    .thenReturn(task);

            String postContent = String.format("{\"title\":\"%s\"}", task.getTitle());
            String receiveJSON = String.format("{\"title\":\"%s\",\"id\":%d}", task.getTitle(), task.getId());

            mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(postContent))
                    .andExpect(content().json(receiveJSON));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {

        @Nested
        @DisplayName("대상 id가 없을 때")
        class Context_without_target_id {

            @Test
            @DisplayName("status not found 를 던진다.")
            void It_throws_task_not_found_exception() throws Exception {
                when(taskService.updateTask(any(), any()))
                        .thenThrow(new TaskNotFoundException(givenID));

                String postContent = String.format("{\"title\":\"%s\"}", givenModifyTitle);

                mockMvc.perform(put("/tasks/{id}", givenID).contentType(MediaType.APPLICATION_JSON).content(postContent))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("대상 id가 있을 때")
        class Context_with_target_id {

            @Test
            @DisplayName("변경된 Task 를 리턴한다.")
            void It_returns_modified_task() throws Exception {
                Task task = new Task();
                task.setTitle(givenModifyTitle);
                task.setId(givenID);

                when(taskService.updateTask(any(), any()))
                        .thenReturn(task);

                String postContent = String.format("{\"title\":\"%s\"}", task.getTitle());
                String receiveJSON = String.format("{\"title\":\"%s\",\"id\":%d}", task.getTitle(), task.getId());

                mockMvc.perform(put("/tasks/{id}", givenID).contentType(MediaType.APPLICATION_JSON).content(postContent))
                        .andExpect(content().json(receiveJSON));
            }
        }
    }

    @Nested
    @DisplayName("patch 메서드는")
    class Describe_patch {

        @Nested
        @DisplayName("대상 id가 없을 때")
        class Context_without_target_id {

            @Test
            @DisplayName("status not found 를 던진다.")
            void It_throws_task_not_found_exception() throws Exception {
                when(taskService.updateTask(any(), any()))
                        .thenThrow(new TaskNotFoundException(givenID));

                String postContent = String.format("{\"title\":\"%s\"}", givenModifyTitle);

                mockMvc.perform(patch("/tasks/{id}", givenID).contentType(MediaType.APPLICATION_JSON).content(postContent))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("대상 id가 있을 때")
        class Context_with_target_id {

            @Test
            @DisplayName("변경된 Task 를 리턴한다.")
            void It_returns_modified_task() throws Exception {
                Task task = new Task();
                task.setTitle(givenModifyTitle);
                task.setId(givenID);

                when(taskService.updateTask(any(), any()))
                        .thenReturn(task);

                String postContent = String.format("{\"title\":\"%s\"}", task.getTitle());
                String receiveJSON = String.format("{\"title\":\"%s\",\"id\":%d}", task.getTitle(), task.getId());

                mockMvc.perform(patch("/tasks/{id}", givenID).contentType(MediaType.APPLICATION_JSON).content(postContent))
                        .andExpect(content().json(receiveJSON));
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("대상 id가 없을 때")
        class Context_without_target_id {

            @Test
            @DisplayName("Task 를 찾을 수 없다는 예외를 던진다.")
            void It_throws_task_not_found_exception() throws Exception {
                when(taskService.deleteTask(givenID))
                        .thenThrow(new TaskNotFoundException(givenID));

                mockMvc.perform(delete("/tasks/{id}", givenID))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("대상 id가 있을 때")
        class Context_with_target_id {

            @Test
            @DisplayName("삭제 후 대상 id를 조회하면 status not found 를 던진다.")
            void It_returns_modified_task() throws Exception {
                when(taskService.getTask(givenID))
                        .thenThrow(new TaskNotFoundException(givenID));

                mockMvc.perform(delete("/tasks/{id}", givenID));
                mockMvc.perform(get("/tasks/{id}", givenID))
                        .andExpect(status().isNotFound());
            }
        }
    }
}