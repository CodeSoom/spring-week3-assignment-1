package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@DisplayName("TaskController 클래스")
public class TaskControllerWebTest {
    final long TASK_ID = 0L;
    final long NOT_FOUND_TASK_ID = 100L;
    final String TASK_TITLE = "Get Sleep";
    final String UPDATE_TITLE = "Do Study";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    TaskService taskService;

    @AfterEach
    void clear() {
        Mockito.reset(taskService);
    }

    Task createTask(String title) throws Exception {
        Task task = new Task();
        task.setTitle(title);
        task.setId(TASK_ID);
        return task;
    }

    @Nested
    @DisplayName("GET 메소드 리퀘스트는")
    class Describe_GET {
        @Nested
        @DisplayName("tasks가 비어있다면")
        class Context_empty_tasks {
            @Test
            @DisplayName("비어있는 리스트 응답한다")
            void it_returns_empty_array() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));
            }
        }

        @Nested
        @DisplayName("tasks가 비어있지 않다면")
        class Context_fill_task {
            List<Task> givenTasks;

            @BeforeEach
            void givenTaskCreate() throws Exception {
                Task task = createTask(TASK_TITLE);
                givenTasks = Collections.singletonList(task);
                given(taskService.getTasks()).willReturn(givenTasks);
            }

            @Test
            @DisplayName("task 리스트를 응답한다")
            void it_returns_empty_array() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TASK_TITLE)))
                        .andExpect(content().string(objectMapper.writeValueAsString(givenTasks)));
            }
        }

        @Nested
        @DisplayName("존재하는 task id를 요청한다면")
        class Context_exist_id {
            Task givenTask;

            @BeforeEach
            void setUp() throws Exception {
                givenTask = createTask(TASK_TITLE);
                given(taskService.getTask(TASK_ID)).willReturn(givenTask);
            }

            @Test
            @DisplayName("task 객체를 리턴한다")
            void it_returns_task_object() throws Exception {
                mockMvc.perform(get("/tasks/" + TASK_ID))
                        .andExpect(status().isOk())
                        .andExpect(content().json(objectMapper.writeValueAsString(givenTask)));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id를 요청한다면")
        class Context_not_exist_id {

            @BeforeEach()
            void setUp() {
                given(taskService.getTask(NOT_FOUND_TASK_ID))
                        .willThrow(new TaskNotFoundException(NOT_FOUND_TASK_ID));
            }

            @Test
            @DisplayName("404코드를 응답한다")
            void it_returns_not_found() throws Exception {
                mockMvc.perform(get("/tasks/" + NOT_FOUND_TASK_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("POST 메소드 리퀘스트는")
    class Describe_POST {
        @Nested
        @DisplayName("task가 주어진다면")
        class Context_with_task {

            @BeforeEach
            void setUp() throws Exception {
                Task givenTask = createTask(TASK_TITLE);
                given(taskService.createTask(any(Task.class))).willReturn(givenTask);
            }

            @DisplayName("201코드와 task를 응답한다")
            @Test
            void it_returns_task() throws Exception {
                //given
                Task source = new Task();
                source.setTitle(TASK_TITLE);
                //when
                //then
                mockMvc.perform(post("/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(source)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id").value(TASK_ID))
                        .andExpect(jsonPath("title").value(TASK_TITLE));
            }
        }
    }

    @Nested
    @DisplayName("PUT 메소드 레퀘스트는")
    class Describe_PUT {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_exist_id {
            Task givenTask;

            @BeforeEach
            void setUp() throws Exception {
                Task mockTask = createTask(UPDATE_TITLE);
                given(taskService.updateTask(eq(TASK_ID), any(Task.class))).willReturn(mockTask);
            }

            @BeforeEach
            void givenCreateTask() throws Exception {
                givenTask = createTask(TASK_TITLE);
            }

            @DisplayName("200코드와 title이 수정된 task를 응답한다")
            @Test
            void it_returns_updated_task() throws Exception {
                //given
                givenTask.setTitle(UPDATE_TITLE);

                //when
                MvcResult mvcResult = mockMvc.perform(put("/tasks/{id}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(givenTask)))
                        .andExpect(status().isOk())
                        .andReturn();

                //then
                Task updatedTask =
                        objectMapper.readValue(mvcResult.getRequest().getContentAsString(), Task.class);
                assertThat(updatedTask).isNotNull();
                assertThat(updatedTask.getId()).isEqualTo(TASK_ID);
                assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TITLE);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_not_exist_id {

            @BeforeEach()
            void setUp() {
                given(taskService.updateTask(eq(NOT_FOUND_TASK_ID), any(Task.class)))
                        .willThrow(new TaskNotFoundException(NOT_FOUND_TASK_ID));
            }

            @Test
            @DisplayName("404코드를 응답한다")
            void it_returns_not_found() throws Exception {
                //given
                Task givenTask = new Task();
                givenTask.setId(NOT_FOUND_TASK_ID);
                givenTask.setTitle(UPDATE_TITLE);
                //when
                //then
                mockMvc.perform(put("/tasks/{id}", NOT_FOUND_TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(givenTask)))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("PATCH 메소드 레퀘스트는")
    class Describe_PATCH {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_exist_id {
            Task givenTask;

            @BeforeEach
            void setUp() throws Exception {
                Task mockTask = createTask(UPDATE_TITLE);
                given(taskService.updateTask(eq(TASK_ID), any(Task.class))).willReturn(mockTask);
            }

            @BeforeEach
            void givenCreateTask() throws Exception {
                givenTask = createTask(TASK_TITLE);
            }

            @DisplayName("200코드와 title이 수정된 task를 응답한다")
            @Test
            void it_returns_updated_task() throws Exception {
                //given
                givenTask.setTitle(UPDATE_TITLE);

                //when
                MvcResult mvcResult = mockMvc.perform(patch("/tasks/{id}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(givenTask)))
                        .andExpect(status().isOk())
                        .andReturn();

                //then
                Task updatedTask =
                        objectMapper.readValue(mvcResult.getRequest().getContentAsString(), Task.class);
                assertThat(updatedTask).isNotNull();
                assertThat(updatedTask.getId()).isEqualTo(TASK_ID);
                assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TITLE);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_not_exist_id {

            @BeforeEach()
            void setUp() {
                given(taskService.updateTask(eq(NOT_FOUND_TASK_ID), any(Task.class)))
                        .willThrow(new TaskNotFoundException(NOT_FOUND_TASK_ID));
            }

            @Test
            @DisplayName("404코드를 응답한다")
            void it_returns_not_found() throws Exception {
                //given
                Task givenTask = new Task();
                givenTask.setId(NOT_FOUND_TASK_ID);
                givenTask.setTitle(UPDATE_TITLE);
                //when
                //then
                mockMvc.perform(patch("/tasks/{id}", NOT_FOUND_TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(givenTask)))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("DELETE 메소드 리퀘스트는")
    class Describe_DELETE {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_exist_id {
            Task givenTask;

            @BeforeEach
            void setUp() throws Exception {
                givenTask = createTask(TASK_TITLE);
                given(taskService.deleteTask(eq(TASK_ID))).willReturn(givenTask);
            }

            @DisplayName("204코드를 응답한다")
            @Test
            void it_returns_204() throws Exception {
                //when
                //then
                mockMvc.perform(delete("/tasks/{id}", givenTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_not_exist_id {

            @BeforeEach()
            void setUp() {
                given(taskService.deleteTask(eq(NOT_FOUND_TASK_ID)))
                        .willThrow(new TaskNotFoundException(NOT_FOUND_TASK_ID));
            }

            @Test
            @DisplayName("404코드를 응답한다")
            void it_returns_not_found() throws Exception {
                //given
                long id = NOT_FOUND_TASK_ID;
                //when
                //then
                mockMvc.perform(delete("/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }
    }

}

