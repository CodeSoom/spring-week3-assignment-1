package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DisplayName("TaskController MockMVC 테스트")
public class TaskControllerWebTest {
    private static final Long TEST_ID = 1L;
    private static final Long TEST_NOT_EXIST_ID = 100L;
    private static final String TEST_TITLE = "테스트는 재밌군요!";
    private String taskSourceToContent;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() throws IOException {
        taskSourceToContent = "{\"title\":\"테스트는 재밌군요!\"}";

        Task task = Task.builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .build();

        given(taskService.getTask(eq(TEST_ID)))
                .willReturn(task);
        given(taskService.getTask(eq(TEST_NOT_EXIST_ID)))
                .willThrow(new TaskNotFoundException(TEST_NOT_EXIST_ID));

        given(taskService.createTask(any()))
                .willReturn(task);

        given(taskService.updateTask(eq(TEST_ID), any(Task.class)))
                .willReturn(task);
        given(taskService.updateTask(eq(TEST_NOT_EXIST_ID), any(Task.class)))
                .willThrow(new TaskNotFoundException(TEST_NOT_EXIST_ID));

        given(taskService.deleteTask(eq(TEST_ID)))
                .willReturn(task);
        given(taskService.deleteTask(eq(TEST_NOT_EXIST_ID)))
                .willThrow(new TaskNotFoundException(TEST_NOT_EXIST_ID));
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class list_메서드는 {
        @Test
        @DisplayName("200 코드를 반환한다")
        void it_responses_200() throws Exception {
            mockMvc.perform(
                            get("/tasks")
                    )
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class detail_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id가_주어지면 {
            @Test
            @DisplayName("200 코드를 반환한다")
            void it_responses_200() throws Exception {
                mockMvc.perform(
                                get("/tasks/" + TEST_ID)
                        )
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id가_주어지면 {
            @Test
            @DisplayName("404 코드를 반환한다")
            void it_responses_404() throws Exception {
                mockMvc.perform(
                                get("/tasks/" + TEST_NOT_EXIST_ID)
                        )
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class create_메서드는 {
        @Test
        @DisplayName("201 코드를 반환한다")
        void it_returns_201() throws Exception {
            mockMvc.perform(
                            post("/tasks")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(taskSourceToContent)
                    )
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class update_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id가_주어지면 {
            @Test
            @DisplayName("200 코드를 반환한다")
            void it_returns_200() throws Exception {
                mockMvc.perform(
                                put("/tasks/" + TEST_ID)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(taskSourceToContent)
                        )
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id가_주어지면 {
            @Test
            @DisplayName("404 코드를 반환한다")
            void it_returns_404() throws Exception {
                mockMvc.perform(
                                put("/tasks/" + TEST_NOT_EXIST_ID)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(taskSourceToContent)
                        )
                        .andDo(print())
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class delete_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id가_주어지면 {
            @Test
            @DisplayName("204 코드를 반환한다")
            void it_returns_204() throws Exception {
                mockMvc.perform(
                                delete("/tasks/" + TEST_ID)
                        )
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id가_주어지면 {
            @Test
            @DisplayName("404 코드를 반환한다")
            void it_returns_404() throws Exception {
                mockMvc.perform(
                                delete("/tasks/" + TEST_NOT_EXIST_ID)
                        )
                        .andExpect(status().isNotFound());
            }
        }
    }
}
