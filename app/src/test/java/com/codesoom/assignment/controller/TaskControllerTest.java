package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.dto.TaskDto;
import com.codesoom.assignment.exception.TaskHasInvalidTitleException;
import com.codesoom.assignment.exception.TaskNotFoundException;
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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController Web Test")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final Long ID = 1L;
    private final Long NOT_EXIST_ID = 100L;
    private final String TITLE = "test";

    @Nested
    @DisplayName("GET /tasks")
    class list {

        @Nested
        @DisplayName("할일 목록을 조회하면")
        class when_list {

            @Test
            @DisplayName("200 OK를 반환합니다.")
            void list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk());
            }
        }

    }

    @Nested
    @DisplayName("GET /tasks/{id}")
    class detail {

        @Nested
        @DisplayName("할일이 존재하면")
        class when_task_is_exist {

            @BeforeEach
            void setUp() {
                Task task = new Task(ID, TITLE);
                given(taskService.detail(ID)).willReturn(task);
            }

            @Test
            @DisplayName("200 OK를 반환합니다.")
            void list() throws Exception {
                mockMvc.perform(get("/tasks/" + ID))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("할일이 존재하지 않으면")
        class when_task_is_not_exist {

            @BeforeEach
            void setUp() {
                given(taskService.detail(NOT_EXIST_ID)).willThrow(new TaskNotFoundException());
            }

            @Test
            @DisplayName("404 Not Found를 반환합니다.")
            void detail() throws Exception {
                mockMvc.perform(get("/tasks/" + NOT_EXIST_ID))
                        .andExpect(status().isNotFound());
            }
        }

    }

    @Nested
    @DisplayName("POST /tasks")
    class create {

        @Nested
        @DisplayName("title을 입력하면")
        class when_create_with_title {

            @BeforeEach
            void setUp() {
                Task task = new Task(ID, TITLE);
                given(taskService.create(task)).willReturn(task);
            }

            @Test
            @DisplayName("201 CREATED를 반환합니다.")
            void create() throws Exception {
                TaskDto taskDto = new TaskDto(ID, TITLE);
                ObjectMapper objectMapper = new ObjectMapper();
                OutputStream out = new ByteArrayOutputStream();
                objectMapper.writeValue(out, taskDto);

                mockMvc.perform(post("/tasks/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(out.toString()))
                        .andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("PUT /tasks/{id}")
    class update {

        @Nested
        @DisplayName("유효한 id와 task를 입력하면")
        class when_update_with_id_and_task {

            @BeforeEach
            void setUp() {
                Task task = new Task(ID, TITLE);
                given(taskService.update(ID, task)).willReturn(task);
            }

            @Test
            @DisplayName("200 OK를 반환합니다.")
            void update() throws Exception {
                TaskDto taskDto = new TaskDto(ID, TITLE);
                ObjectMapper objectMapper = new ObjectMapper();
                OutputStream out = new ByteArrayOutputStream();
                objectMapper.writeValue(out, taskDto);

                mockMvc.perform(put("/tasks/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(out.toString()))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않는다면")
        class when_update_with_invalid_id {

            @BeforeEach
            void setUp() {
                given(taskService.update(NOT_EXIST_ID, new Task(NOT_EXIST_ID, TITLE))).willThrow(new TaskNotFoundException());
            }

            @Test
            @DisplayName("404 NOT FOUND 를 반환합니다.")
            void update() throws Exception {
                TaskDto taskDto = new TaskDto(NOT_EXIST_ID, TITLE);
                ObjectMapper objectMapper = new ObjectMapper();
                OutputStream out = new ByteArrayOutputStream();
                objectMapper.writeValue(out, taskDto);

                mockMvc.perform(put("/tasks/" + NOT_EXIST_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(out.toString()))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("타이틀을 입력하지 않으면")
        class when_update_with_invalid_title {

            @BeforeEach
            void setUp() {
                given(taskService.update(ID, new Task(ID, ""))).willThrow(new TaskHasInvalidTitleException());
            }

            @Test
            @DisplayName("400 BAD REQUEST 를 반환합니다.")
            void update() throws Exception {

                TaskDto taskDto = new TaskDto(ID, "");
                ObjectMapper objectMapper = new ObjectMapper();
                OutputStream out = new ByteArrayOutputStream();
                objectMapper.writeValue(out, taskDto);

                mockMvc.perform(put("/tasks/" + ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(out.toString()))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{id}")
    class delete {

        @Nested
        @DisplayName("유효한 id를 입력하면")
        class when_delete_with_valid_id {

            @Test
            @DisplayName("204 NO CONTENT 를 반환합니다.")
            void update() throws Exception {
                mockMvc.perform(delete("/tasks/" + ID))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않는다면")
        class when_update_with_invalid_id {

            @BeforeEach
            void setUp() {
                doThrow(new TaskNotFoundException()).when(taskService).delete(NOT_EXIST_ID);
            }

            @Test
            @DisplayName("404 NOT FOUND 를 반환합니다.")
            void update() throws Exception {
                mockMvc.perform(delete("/tasks/" + NOT_EXIST_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
