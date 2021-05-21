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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController")
public class TaskControllerWebTest {

    private static final String TASK_TITLE = "test";
    private static final String TASK_NOT_FOUND = "Task not found";
    private static final String TASK_CREATE_PREFIX = "new";
    private static final String TASK_UPDATE_PREFIX = "fix";
    private static final Long EXIST_ID = 1L;
    private static final Long WRONG_ID = -1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Nested
    @DisplayName("GET 메소드")
    class GetMethod {

        @Nested
        @DisplayName("TaskList 목록에 대한")
        class givenTaskList {

            @BeforeEach
            void setUp() {
                List<Task> taskList = new ArrayList<>();
                Task task = new Task();
                task.setTitle(TASK_TITLE);
                taskList.add(task);

                given(taskService.getTasks()).willReturn(taskList);
            }

            @Nested
            @DisplayName("요청이 들어오면")
            class whenRequest {

                @Test
                @DisplayName("모든 TaskList 를 리턴한다")
                void thenReturnTaskList() throws Exception {
                    mockMvc.perform(get("/tasks"))
                           .andExpect(status().isOk())
                           .andExpect(content().string(containsString(TASK_TITLE)));
                }
            }
        }

        @Nested
        @DisplayName("Task 상세정보에 대한")
        class givenDetailTask {

            @Nested
            @DisplayName("Task id 값이 유효하면")
            class whenRequestWithValidTaskId {

                @BeforeEach
                void setUp() {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);

                    given(taskService.getTask(EXIST_ID)).willReturn(task);
                }

                @Test
                @DisplayName("Task 를 리턴한다")
                void thenReturnTask() throws Exception {
                    mockMvc.perform(get("/tasks/" + EXIST_ID))
                           .andExpect(status().isOk())
                           .andExpect(content().string(containsString(TASK_TITLE)));
                }
            }

            @Nested
            @DisplayName("Task id 값이 null 이거나 유효하지 않으면")
            class whenRequestWithNullOrInvalidTaskId {

                @BeforeEach
                void setUp() {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);

                    given(taskService.getTask(null)).willThrow(new TaskNotFoundException(null));
                    given(taskService.getTask(WRONG_ID)).willThrow(new TaskNotFoundException(WRONG_ID));
                }

                @Test
                @DisplayName("TaskNotFoundException 예외를 던진다")
                void thenReturnTaskNotFoundException() throws Exception {
                    mockMvc.perform(get("/tasks/" + null))
                           .andExpect(status().is4xxClientError());
                    mockMvc.perform(get("/tasks/" + WRONG_ID))
                           .andExpect(status().isNotFound());
                }
            }
        }
    }

    @Nested
    @DisplayName("POST 메소드")
    class postMethod {

        @Nested
        @DisplayName("새 Task 에 대한")
        class givenCreateNewTask {

            @Nested
            @DisplayName("등록 요청이 유효 하면")
            class whenRequestWithValid {

                @Test
                @DisplayName("새 Task 를 저장하고 리턴한다")
                void thenReturnNewTask() throws Exception {
                    Task task = new Task();
                    task.setTitle(TASK_CREATE_PREFIX + TASK_TITLE);
                    String taskContent = objectMapper.writeValueAsString(task);

                    when(taskService.createTask(any(Task.class))).thenReturn(task);

                    mockMvc.perform(post("/tasks")
                                   .content(taskContent)
                                   .contentType(MediaType.APPLICATION_JSON)
                                   .accept(MediaType.APPLICATION_JSON))
                           .andExpect(status().isCreated())
                           .andExpect(content().string(containsString(TASK_CREATE_PREFIX + TASK_TITLE)));
                }
            }

            @Nested
            @DisplayName("등록 요청이 Null 이면")
            class whenRequestWithNull {

                @Test
                @DisplayName("NullPointException 예외를 던진다")
                void thenReturnNullPointException() {
                    /* TODO
                        Add Service logic
                    */
                }
            }

            @Nested
            @DisplayName("등록 요청이 유효하지 않으면")
            class whenRequestWithInvalid {

                @Test
                @DisplayName("Task 를 저장하지 않고 요청을 끝낸다")
                void thenReturnNothing() {
                    /* TODO
                        Add Service logic
                    */
                }
            }
        }
    }

    @Nested
    @DisplayName("PUT & PATCH 메소드")
    class putAndPatchMethod {

        @Nested
        @DisplayName("수정 할 Task 에 대한")
        class givenUpdateTask {

            @Nested
            @DisplayName("Task id 값이 유효 하면")
            class whenRequestWithValidTaskId {

                @BeforeEach
                void setUp() {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);

                    given(taskService.getTask(EXIST_ID)).willReturn(task);
                }

                @Test
                @DisplayName("Task 를 수정하고 저장한 뒤 리턴한다")
                void thenReturnModifiedTask() throws Exception {
                    Task requestTask = new Task();
                    requestTask.setTitle(TASK_UPDATE_PREFIX + TASK_TITLE);
                    String taskContent = objectMapper.writeValueAsString(requestTask);

                    when(taskService.updateTask(eq(EXIST_ID), any(Task.class))).thenReturn(requestTask);

                    mockMvc.perform(put("/tasks/" + EXIST_ID)
                           .content(taskContent)
                           .contentType(MediaType.APPLICATION_JSON)
                           .accept(MediaType.APPLICATION_JSON))
                           .andExpect(status().isOk())
                           .andExpect(content().string(containsString(TASK_UPDATE_PREFIX + TASK_TITLE)));

                    requestTask.setTitle(TASK_TITLE);
                    taskContent = objectMapper.writeValueAsString(requestTask);

                    mockMvc.perform(patch("/tasks/" + EXIST_ID)
                           .content(taskContent)
                           .contentType(MediaType.APPLICATION_JSON)
                           .accept(MediaType.APPLICATION_JSON))
                           .andExpect(status().isOk())
                           .andExpect(content().string(containsString(TASK_TITLE)));
                }
            }

            @Nested
            @DisplayName("Task id 값이 Null 이거나 유효하지 않으면")
            class whenRequestWithNullOrInvalidTaskId {

                @Test
                @DisplayName("TaskNotFoundException 예외를 던진다")
                void thenReturnTaskNotFoundException() throws Exception {
                    Task requestTask = new Task();
                    requestTask.setTitle(TASK_UPDATE_PREFIX + TASK_TITLE);
                    String taskContent = objectMapper.writeValueAsString(requestTask);

                    when(taskService.updateTask(eq(WRONG_ID), any(Task.class))).thenThrow(new TaskNotFoundException(WRONG_ID));

                    mockMvc.perform(put("/tasks/" + WRONG_ID)
                           .content(taskContent)
                           .contentType(MediaType.APPLICATION_JSON)
                           .accept(MediaType.APPLICATION_JSON))
                           .andExpect(status().isNotFound())
                           .andExpect(content().string(containsString(TASK_NOT_FOUND)));
                }
            }
        }
    }

    @Nested
    @DisplayName("DELETE 메소드")
    class deleteMethod {

        @Nested
        @DisplayName("삭제 할 Task 에 대한")
        class givenDeleteTask {

            @Nested
            @DisplayName("Task id 값이 유효 하면")
            class whenRequestWithValidTaskId {

                @BeforeEach
                void setUp() {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE);

                    given(taskService.getTask(EXIST_ID)).willReturn(task);
                }

                @Test
                @DisplayName("Task 를 삭제하고 No Content (204 status) 리턴한다")
                void thenReturnNoContent() throws Exception {
                    Task task = new Task();
                    when(taskService.deleteTask(eq(EXIST_ID))).thenReturn(task);

                    mockMvc.perform(delete("/tasks/" + EXIST_ID))
                           .andExpect(status().isNoContent());
                }
            }

            @Nested
            @DisplayName("Task id 값이 Null 이거나 유효하지 않으면")
            class whenRequestWithInvalidTaskId {

                @Test
                @DisplayName("TaskNotFoundException 예외를 던진다")
                void thenReturnTaskNotFoundException() throws Exception {
                    when(taskService.deleteTask(eq(WRONG_ID))).thenThrow(new TaskNotFoundException(WRONG_ID));

                    mockMvc.perform(delete("/tasks/" + WRONG_ID))
                           .andExpect(status().isNotFound())
                           .andExpect(content().string(containsString(TASK_NOT_FOUND)));
                }
            }
        }
    }
}
