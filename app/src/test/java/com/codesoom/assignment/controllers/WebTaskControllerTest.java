package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.TaskEditDto;
import com.codesoom.assignment.dto.TaskSaveDto;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("할 일에 대한 HTTP 요청")
@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc
public class WebTaskControllerTest {

    private static final String TEST_TASK_TITLE = "테스트";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("GET - /tasks 요청시")
    class Describe_list {

        @Nested
        @DisplayName("할 일 목록 수 만큼")
        class Context_test {

            final int givenSize = 10;

            @BeforeEach
            void setUp() {
                List<Task> tasks = LongStream.rangeClosed(1, givenSize)
                        .mapToObj(index -> {
                            Task task = new Task();
                            task.setId(index);
                            task.setTitle(TEST_TASK_TITLE + "_" + index);
                            return task;
                        })
                        .collect(Collectors.toList());

                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("할 일 목록을 응답한다.[http status code:200]")
            void it_response_tasks_and_http_status_200() throws Exception {

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(givenSize)))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("POST /tasks 요청시")
    class Describe_create {

        @Nested
        @DisplayName("유효한 할 일 데이터 값이라면")
        class Context_valid {

            final Long taskId = 1L;
            final TaskSaveDto source = new TaskSaveDto(TEST_TASK_TITLE);

            @BeforeEach
            void setUp() {
                Task task = new Task();
                task.setId(taskId);
                task.setTitle(TEST_TASK_TITLE);

                given(taskService.createTask(any())).willReturn(task);
            }

            ResultActions request() throws Exception {
                return mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(source)));
            }

            @Test
            @DisplayName("할일 등록 후 할일을 응답한다. [200]")
            void it_response_201() throws Exception {
                request().andExpect(status().isCreated())
                        .andExpect(jsonPath("id").value(String.valueOf(taskId)))
                        .andExpect(jsonPath("title").value(TEST_TASK_TITLE));
            }
        }
    }

    @Nested
    @DisplayName("GET /tasks/{taskId} 요청시")
    class Describe_detail {
        @Nested
        @DisplayName("{taskId} 와 일치하는 할 일이 있다면")
        class Context_exists {

            final Long taskId = 1L;

            @BeforeEach
            void setUp() {
                Task task = new Task();
                task.setId(taskId);
                task.setTitle(TEST_TASK_TITLE);
                given(taskService.getTask(taskId)).willReturn(task);
            }

            @Test
            @DisplayName("HTTP 200 OK")
            void it_response_task() throws Exception {
                mockMvc.perform(get("/tasks/{taskId}", taskId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(String.valueOf(taskId)))
                        .andExpect(jsonPath("title").value(TEST_TASK_TITLE));
            }
        }

        @Nested
        @DisplayName("{taskId} 와 일치하는 할 일이 없다면")
        class Context_notExists {

            final Long notExistsTaskId = 999L;

            @BeforeEach
            void setUp() {
                given(taskService.getTask(notExistsTaskId)).willThrow(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("HTTP 404 Not Found")
            void it_response_404() throws Exception {
                mockMvc.perform(get("/tasks/{taskId}", notExistsTaskId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("DELETE - /tasks/{taskId} 삭제 요청시")
    class Describe_delete {
        @Nested
        @DisplayName("{taskId} 와 일치하는 할 일이 있다면")
        class Context_exists {

            final Long existsTaskId = 1L;

            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(existsTaskId)).willReturn(null);
            }

            @Test
            @DisplayName("HTTP 204 No Content")
            void it_response_204() throws Exception {
                mockMvc.perform(delete("/tasks/{taskId}", existsTaskId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("{taskId} 와 일치하는 할 일이 없다면")
        class Context_notExists {

            final Long notExistsTaskId = 999L;

            @BeforeEach
            void setUp() {
                given(taskService.deleteTask(notExistsTaskId)).willThrow(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("HTTP 404 Not Found")
            void it_response_404() throws Exception {
                mockMvc.perform(delete("/tasks/{taskId}", notExistsTaskId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("PUT - /tasks/{taskId} 대체 요청시")
    class Describe_update {

        final TaskEditDto source = new TaskEditDto(TEST_TASK_TITLE);
        final Long taskId = 1L;

        @Nested
        @DisplayName("{taskId} 와 일치하는 할 일이 있다면")
        class Context_exists {

            @BeforeEach
            void setUp() {
                Task task = new Task(TEST_TASK_TITLE);
                task.setId(taskId);
                given(taskService.updateTask(taskId, source)).willReturn(task);
            }

            @Test
            @DisplayName("HTTP 200 OK")
            void it_response_200() throws Exception {

                mockMvc.perform(put("/tasks/{taskId}", taskId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(source)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(taskId))
                        .andExpect(jsonPath("title").value(TEST_TASK_TITLE));
            }
        }

        @Nested
        @DisplayName("{taskId} 와 일치하는 할 일이 없다면")
        class Context_notExists {

            final Long notExistsTaskId = 998L;

            @BeforeEach
            void setUp() {
                given(taskService.updateTask(notExistsTaskId, source)).willThrow(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("HTTP 404 Not Found")
            void it_response_404() throws Exception {
                mockMvc.perform(put("/tasks/{taskId}", notExistsTaskId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(source)))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("PATCH - /tasks/{taskId} 수정 요청시")
    class Describe_patch {

        final TaskEditDto source = new TaskEditDto(TEST_TASK_TITLE);
        final Long taskId = 2L;

        @BeforeEach
        void setUp() {
            Task task = new Task(TEST_TASK_TITLE);
            task.setId(taskId);

            given(taskService.updateTask(taskId, source)).willReturn(task);
        }

        @Nested
        @DisplayName("{taskId} 와 일치하는 할 일이 있다면")
        class Context_exists {

            @Test
            @DisplayName("HTTP 200 OK")
            void it_response_200() throws Exception {

                mockMvc.perform(patch("/tasks/{taskId}", taskId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(source)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("title").exists());
            }
        }

        @Nested
        @DisplayName("{taskId} 와 일치하는 할 일이 없다면")
        class Context_notExists {

            final Long notExistsTaskId = 999L;

            @BeforeEach
            void setUp() {
                given(taskService.updateTask(notExistsTaskId, source)).willThrow(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("HTTP 404 Not Found")
            void it_response_404() throws Exception {
                mockMvc.perform(patch("/tasks/{taskId}", notExistsTaskId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(source)))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
