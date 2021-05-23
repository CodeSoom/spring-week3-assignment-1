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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@DisplayName("TaskControllerWebTest 클래스")
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper;

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

                actions.andDo(MockMvcResultHandlers.print());

                actions
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.title", is("Test")));
            }
        }
    }

    @Nested
    @DisplayName("할 일 생성을 요청할 때")
    class Describe_createMvc {

        @Nested
        @DisplayName("Title 내용이 주어지면")
        class Context_exist_title {
            Task source = new Task();
            Task task = new Task();

            @BeforeEach
            void setUp() {
                mapper = new ObjectMapper();
            }

            @Test
            @DisplayName("할 일을 생성하고, 생성된 할 일을 리턴, 201 응답코드를 전달합니다.")
            void it_create_task_and_return() throws Exception {
                given(taskService.createTask(source)).willReturn(task);
                source.setTitle("Test");
                task.setTitle("Test");
                task.setId(1L);

                final ResultActions actions = mockMvc.perform(post("/tasks")
                        .content(mapper.writeValueAsString(source))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

                actions
                        .andExpect(status().isCreated());
//                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                        .andExpect(jsonPath("$.title", is("Test")))
//                        .andExpect(jsonPath("$.id", is(1L)));
            }
        }
    }

    @Nested
    @DisplayName("등록된 할 일을 수정할 때")
    class Describe_updateMvc {

        @Nested
        @DisplayName("등록되지 않은 ID로 요청하면")
        class Context_with_not_registered_ID {
            Task source = new Task();

            @BeforeEach
            void setUp() {
                mapper = new ObjectMapper();
            }

            @Test
            @DisplayName("TaskNotFound 예외를 던지고, 404 응답코드를 전달합니다.")
            void it_throw_exception() throws Exception {
                given(taskService.updateTask(100L, source))
                        .willThrow(TaskNotFoundException.class);
                given(taskService.getTask(100L))
                        .willThrow(TaskNotFoundException.class);
                source.setTitle("New Task");

                final ResultActions actions = mockMvc.perform(patch("/tasks/100")
                        .content(mapper.writeValueAsString(source))
                        .contentType(MediaType.APPLICATION_JSON));
//                        .accept(MediaType.APPLICATION_JSON));

                actions
                        .andExpect(status().isNotFound());
            }
        }
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
