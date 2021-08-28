package com.codesoom.assignment.controllers;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Task Controller WebTest의")
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp(){
        ArrayList<Task> tasks = new ArrayList<>();
        Task task = new Task(1L, "title");
        tasks.add(task);

        given(taskService.getTaskList()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(task);
    }

    @Nested
    @DisplayName("getTaskList 메소드는")
    class Describe_getTaskList {

        @Nested
        @DisplayName("할 일이 없으면")
        class Context_with_no_tasks {

            @BeforeEach
            void noTaskSetUp() {
                given(taskService.getTaskList()).willReturn(new ArrayList<Task>());
            }

            @Test
            @DisplayName("빈 리스트를 리턴하고 OK를 응답한다.")
            void list() throws Exception {
                MvcResult mvcResult = mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"))
                        .andDo(print())
                        .andReturn();
            }
        }

        @Nested
        @DisplayName("할 일이 있으면")
        class Context_with_tasks {
            @Test
            @DisplayName("전체 할 일 리스트를 리턴하고 OK를 응답한다.")
            void list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("title")))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        @Nested
        @DisplayName("할 일이 있으면")
        class Context_with_task {
            @Test
            @DisplayName("할 일을 리턴하고 OK를 응답한다.")
            void detailWithValidId() throws Exception {
                mockMvc.perform(get("/tasks/{id}", 1L))
                        .andExpect(status().isOk())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("할 일이 없으면 ")
        class Context_with_no_task {
            @Test
            @DisplayName("TaskNotFoundException을 던지고 Not Found를 응답한다.")
            void detailWithInvalidId() throws Exception {
                given(taskService.getTask(10000L))
                        .willThrow(new TaskNotFoundException("10000"));

                mockMvc.perform(get("/tasks/10000"))
                        .andExpect(status().isNotFound())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("비정상적인 PathVariable을 요청하면")
        class Context_with_bad_request {
            @Test
            @DisplayName("Not Found를 응답한다.")
            void detailWithInvalidPathVariable() throws Exception {
                mockMvc.perform(get("/tasks/undefined"))
                        .andExpect(status().isNotFound())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                        .andDo(print());
            }
        }
    }


    @Nested
    @DisplayName("할 일(id)에 대한 등록 요청이 왔을 때")
    class CreateTask {
        @Test
        @DisplayName("요청이 정상적인 경우 할 일을 등록한다.")
        void createTask() throws Exception {
            Task task2 = new Task(2L, "title2");
            mockMvc.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(task2)))
                    .andExpect(status().isCreated())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("할 일(id)에 대한 수정 요청이 오면")
    class UpdateTask {

        @Test
        @DisplayName("할 일(id)을 수정한다.")
        void updateTask() throws Exception {

            Task newTask = new Task(1L, "New Title");
            given(taskService.updateTask(1L, newTask)).willReturn(newTask);

            mockMvc.perform(put("/tasks/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newTask)))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("할 일(id)에 대한 완료 요청이 오면")
    class CompleteTask {

        @Test
        @DisplayName("할 일(id)을 리스트에서 제외한다.")
        void completeTask() throws Exception {
            mockMvc.perform(delete("/tasks/{id}", 1L))
                    .andExpect(status().isNoContent())
                    .andDo(print());
        }
    }
}
