package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@DisplayName("TaskControllerWebTest 클래스")
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Nested
    @DisplayName("전체 할 일을 요청할 때")
    class Describe_listMvc {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();

        @Nested
        @DisplayName("할 일이 존재하지 않을 경우")
        class Context_not_exist_task {

            @Test
            @DisplayName("빈 배열을 리턴하고, 200 응답코드를 전달합니다.")
            void it_return_empty() throws Exception {
                given(taskService.getTasks()).willReturn(tasks);

                final ResultActions actions = mockMvc.perform(get("/tasks"));

                actions
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("[]")))
                        .andExpect(jsonPath("$.length()", is(0)));
            }

        }

        @Nested
        @DisplayName("할 일이 존재할 경우")
        class Context_exist_task {
            @Test
            @DisplayName("할 일 목록을 리턴하고, 200 응답코드를 전달합니다.")
            void it_return_all() throws Exception {
                given(taskService.getTasks()).willReturn(tasks);
                task.setTitle("Test");
                tasks.add(task);

                final ResultActions actions = mockMvc.perform(get("/tasks"));

                actions
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.[0].title", is("Test")));
            }
        }
    }

    @Nested
    @DisplayName("특정한 할 일을 요청할 때")
    class Describe_detailMvc {
        Task task = new Task();

        @Nested
        @DisplayName("요청한 ID에 맞는 할 일이 없다면")
        class Context_not_exist_task {
            @Test
            @DisplayName("TaskNotFound 예외를 던지고, 404 응답코드를 전달합니다.")
            void it_throw_exception() throws Exception {
                given(taskService.getTask(100L))
                        .willThrow(TaskNotFoundException.class);

                final ResultActions actions = mockMvc.perform(get("/tasks/100"));

                actions
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("요청한 ID에 맞는 할 일이 있다면")
        class Context_exist_task {
            Task task = new Task();

            @Test
            @DisplayName("요청 ID의 할 일을 리턴하고, 200 응답코드를 전달합니다.")
            void it_return_specific_task() throws Exception {
                given(taskService.getTask(1L)).willReturn(task);
                task.setTitle("Test");

                final ResultActions actions = mockMvc.perform(get("/tasks/1"));

                actions
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.title", is("Test")));
            }
        }
    }

    @Test
    void createMvcTest() throws Exception {
        mockMvc.perform(post("/tasks title:Test"))
                .andExpect(status().isCreated());
    }

    @Test
    void updateMvcTest() throws Exception {
        mockMvc.perform(put("/tasks/1 title:New Test"))
                .andExpect(status().isOk());
    }

    @Test
    void patchMvcTest() throws Exception {
        mockMvc.perform(patch("/tasks/1 title:New Test"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMvcTest() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
