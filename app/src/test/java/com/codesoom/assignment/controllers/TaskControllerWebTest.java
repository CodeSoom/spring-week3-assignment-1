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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskController 클래스의")
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Nested
    @DisplayName("'/tasks/{id}'로 PUT 요청 시")
    class Describe_of_PUT_tasks_by_id {

        @Nested
        @DisplayName("만약 등록되어 있는 할 일 ID와 새로운 제목이 주어진다면")
        class Context_of_valid_task {
            private final Long validTaskId = 1L;
            private final String additionalValidTaskTitle = "test2";
            private Task newTask = new Task();

            @BeforeEach
            void when() {
                newTask.setId(validTaskId);
                newTask.setTitle(additionalValidTaskTitle);

                given(taskService.updateTask(any(Long.class), any(Task.class)))
                        .willReturn(newTask);
            }

            @Test
            @DisplayName("해당하는 할 일을 새로운 제목으로 갱신한다")
            void updateTask() throws Exception {
                MockHttpServletRequestBuilder requestBuilder =
                        put("/tasks/" + validTaskId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"" + additionalValidTaskTitle + "\"}");

                mockMvc.perform(requestBuilder)
                       .andExpect(status().isOk())
                       .andExpect(content().string(containsString(additionalValidTaskTitle)));

                verify(taskService).updateTask(eq(validTaskId), any(Task.class));
            }
        }
    }

    @Nested
    @DisplayName("'/tasks/{id}'로 DELETE 요청 시")
    class Describe_of_DELETE_tasks_by_id {

        @Nested
        @DisplayName("만약 등록되어 있는 할 일 ID이 있다면")
        class Context_of_valid_task_id {
            private final Long validTaskId = 1L;
            private final String validTaskTitle = "test1";
            private List<Task> tasks = new ArrayList<>();
            private Task task = new Task();

            @BeforeEach
            void when() {
                task.setId(validTaskId);
                task.setTitle(validTaskTitle);

                tasks.add(task);

                given(taskService.deleteTask(validTaskId))
                        .willReturn(null);
            }

            @Test
            @DisplayName("지정한 할 일을 삭제한다")
            void It_returns_no_content_status() throws Exception {
                mockMvc.perform(delete("/tasks/" + validTaskId))
                       .andExpect(status().isNoContent());

                verify(taskService).deleteTask(validTaskId);
            }
        }
    }

    @Nested
    @DisplayName("'/tasks'로 POST 요청 시")
    class Describe_of_POST_tasks {

        @Nested
        @DisplayName("만약 유효한 할 일이 주어진다면")
        class Context_of_valid_task {
            private final Long validTaskId = 1L;
            private final String validTaskTitle = "test1";
            private List<Task> tasks = new ArrayList<>();
            private Task task = new Task();

            @BeforeEach
            void when() {
                task.setId(validTaskId);
                task.setTitle(validTaskTitle);

                tasks.add(task);

                given(taskService.createTask(any(Task.class))).willReturn(task);
            }

            @Test
            @DisplayName("새로운 할 일을 등록한다")
            void createNewTask() throws Exception {
                MockHttpServletRequestBuilder requestBuilder =
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"" + validTaskTitle + "\"}");

                mockMvc.perform(requestBuilder)
                       .andExpect(status().isCreated())
                       .andExpect(content().string(containsString(validTaskTitle)));

                verify(taskService).createTask(any(Task.class));
            }
        }
    }

    @Nested
    @DisplayName("'/tasks/{id}'로 GET 요청 시")
    class Describe_of_GET_tasks_by_id {

        @Nested
        @DisplayName("만약 주어진 ID의 할 일이 없다면")
        class Context_of_empty_tasks {
            private final Long invalidTaskId = 100L;

            @BeforeEach
            void when() {
                given(taskService.getTask(invalidTaskId))
                        .willThrow(new TaskNotFoundException(invalidTaskId));
                // .willThrow(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void It_throws_task_not_found_exception() throws Exception {
                mockMvc.perform(get("/tasks/" + invalidTaskId))
                       .andExpect(status().isNotFound());

                verify(taskService).getTask(invalidTaskId);
            }
        }

        @Nested
        @DisplayName("만약 주어진 ID의 할 일이 있다면")
        class Context_of_one_task {
            private final Long validTaskId = 1L;
            private final String validTaskTitle = "test1";
            private Task task = new Task();

            @BeforeEach
            void when() {
                task.setId(validTaskId);
                task.setTitle(validTaskTitle);

                given(taskService.getTask(validTaskId))
                        .willReturn(task);
            }

            @Test
            @DisplayName("해당하는 할 일을 응답한다")
            void It_returns_one_task() throws Exception {
                mockMvc.perform(get("/tasks/" + validTaskId))
                       .andExpect(status().isOk())
                       .andExpect(jsonPath("id").value(validTaskId))
                       .andExpect(jsonPath("title").value(containsString(validTaskTitle)));

                verify(taskService).getTask(validTaskId);
            }
        }
    }

    @Nested
    @DisplayName("'/tasks'로 GET 요청 시")
    class Describe_of_GET_tasks {

        @Nested
        @DisplayName("만약 등록된 할 일이 없다면")
        class Context_of_empty_tasks {

            @BeforeEach
            void when() {
                given(taskService.getTasks()).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("빈 리스트를 응답한다")
            void It_returns_empty_list() throws Exception {
                mockMvc.perform(get("/tasks"))
                       .andExpect(status().isOk())
                       .andExpect(jsonPath("$[*]", hasSize(0)))
                       .andExpect(content().string("[]"));
            }
        }

        @Nested
        @DisplayName("만약 등록되어 있는 할 일이 1개 있다면")
        class Context_of_one_tasks {
            private final Long validTaskId = 1L;
            private final String validTaskTitle = "test1";
            private List<Task> tasks = new ArrayList<>();
            private Task task = new Task();

            @BeforeEach
            void when() {
                task.setId(validTaskId);
                task.setTitle(validTaskTitle);

                tasks.add(task);

                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("할 일 1개를 응답한다")
            void It_returns_one_task() throws Exception {
                mockMvc.perform(get("/tasks"))
                       .andExpect(status().isOk())
                       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                       .andExpect(jsonPath("$[*]", hasSize(1)))
                       .andExpect(jsonPath("$[0].id",
                                           is(validTaskId.intValue()))
                                 )
                       .andExpect(jsonPath("$[0].title",
                                           containsString(validTaskTitle)));

                verify(taskService).getTasks();
            }
        }
    }
}
