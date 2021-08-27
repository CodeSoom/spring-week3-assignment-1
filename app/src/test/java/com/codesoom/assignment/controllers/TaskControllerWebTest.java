package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotCreateException;
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
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
public class TaskControllerWebTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TaskService taskService;

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("request = GET, path = /tasks 일 때")
        class Context_request_isGET_and_path_isTasks {
            @Nested
            @DisplayName("등록된 할 일이 없다면")
            class Context_no_have_tasks {
                final String EMPTY_LIST = "[]";

                @BeforeEach
                void prepareTasks() {
                    List<Task> tasks = new ArrayList<>();
                    tasks.clear();

                    given(taskService.getTasks()).willReturn(tasks);
                }

                @Test
                @DisplayName("비어있는 리스트와 OK(200)을 응답합니다")
                void it_respond_emptyTasks_and_200_OK() throws Exception {
                    mockMvc.perform(get("/tasks"))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(EMPTY_LIST)));
                }
            }

            @Nested
            @DisplayName("등록된 할 일이 있다면")
            class Context_have_tasks {
                List<Task> tasks;
                String tasksToJSON;

                @BeforeEach
                void prepareTasks() throws IOException {
                    tasks = new ArrayList<>();
                    Task task1 = new Task();
                    Task task2 = new Task();
                    tasks.add(task1);
                    tasks.add(task2);

                    tasksToJSON = tasksToJSON(tasks);
                    given(taskService.getTasks()).willReturn(tasks);
                }

                @Test
                @DisplayName("할 일들과 OK(200)을 응답합니다")
                void it_respond_tasks_and_200_OK() throws Exception {
                    mockMvc.perform(get("/tasks"))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(tasksToJSON)));
                }
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("request = GET, path = /tasks/{id} 일 때")
        class Context_request_isGET_and_path_isTasksId {
            @Nested
            @DisplayName("입력받은 id와 일치하는 등록된 할 일이 없다면")
            class Context_matchId_NotExist {
                final Long ID = 1L;

                @BeforeEach
                void prepareTask() {
                    new Task();
                    taskService.deleteTask(ID);
                    given(taskService.getTask(ID))
                            .willThrow(new TaskNotFoundException(ID));
                }

                @Test
                @DisplayName("NOT_FOUND(404)를 응답합니다 ")
                void it_respond_400_NOT_FOUND() throws Exception {
                    mockMvc.perform(get("/tasks/" + ID))
                            .andExpect(status().isNotFound());
                }
            }

            @Nested
            @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
            class Context_matchId_Exist {
                String taskToJSON;
                final Long ID = 1L;

                @BeforeEach
                void prepareTask() throws IOException {
                    Task task = new Task();
                    given(taskService.getTask(ID)).willReturn(task);
                    taskToJSON = taskToJSON(task);
                }

                @Test
                @DisplayName("할 일과 OK(200)을 응답합니다")
                void it_respond_200_OK() throws Exception {
                    mockMvc.perform(get("/tasks/" + ID))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(taskToJSON)));
                }
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("request = POST, path = /tasks 일 때")
        class Context_request_isPOST_and_path_isTasks {
            @Nested
            @DisplayName("제목이 입력되지 않았다면")
            class Context_task_no_have_title {
                Task task;

                @BeforeEach
                void prepareTask() {
                    task = new Task();
                    task.setTitle(null);

                    given(taskService.createTask(task))
                            .willThrow(new TaskNotCreateException(task.getId()));
                }

                @Test
                @DisplayName("할 일을 생성 할 수 없다는 예외를 던집니다")
                void it_respond_BAD_REQEUST() throws Exception {
                    mockMvc.perform(post("/tasks"))
                            .andExpect(status().isBadRequest());
                }
            }

            @Nested
            @DisplayName("제목이 입력되었다면")
            class Context_task_have_title {
                String taskToJSON;

                @BeforeEach
                void prepareTask() throws IOException {
                    Task task = new Task();
                    task.setTitle("Test");
                    given(taskService.createTask(task)).willReturn(task);
                    taskToJSON = taskToJSON(task);
                }

                @Test
                @DisplayName("생성한 할 일과 OK(200)을 응답합니다")
                void It_respond_createdTask_and_200_OK() throws Exception {
                    mockMvc.perform(post("/tasks"))
                            .andExpect(status().isCreated())
                            .andExpect(content().string(containsString(taskToJSON)));
                }
            }
        }
    }


    private String tasksToJSON(List<Task> tasks) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String taskToJSON(Task task) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }
}
