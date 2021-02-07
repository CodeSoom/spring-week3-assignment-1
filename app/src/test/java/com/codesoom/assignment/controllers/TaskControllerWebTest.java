package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("요청을 받은 TaskController 클래스는")
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final Long givenSavedTaskId = 1L;
    private final Long givenUnsavedTaskId = 100L;
    private final String givenTaskTitle = "Test";
    private final String givenModifyTaskTitle = "Modified";

    private final int repeatTime = 2;

    private List<Task> taskList;
    private Task task;

    private OutputStream outputStream;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String taskJsonString;

    private RequestBuilder requestBuilder;
    private String uriTemplate;

    @BeforeEach
    void setUp() throws IOException {
        taskList = new ArrayList<Task>();

        task = new Task();
        task.setId(givenSavedTaskId);
        task.setTitle(givenTaskTitle);

        outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        taskJsonString = outputStream.toString();
    }

    @Nested
    @DisplayName("요청이 GET /tasks 이고,")
    class Context_when_request_is_get_tasks {
        @BeforeEach
        void setRequest() {
            requestBuilder = get("/tasks");
        }

        @Nested
        @DisplayName("task가 없다면")
        class Context_without_any_task {
            @Test
            @DisplayName("200 OK와 비어 있는 리스트를 응답한다.")
            void it_respond_200_ok_and_empty_list() throws Exception {
                given(taskService.getTasks()).willReturn(new ArrayList<>());

                mockMvc.perform(requestBuilder)
                        .andExpect(status().isOk())
                        .andExpect(content().json("[]"));
            }
        }

        @Nested
        @DisplayName("task가 1개 이상있다면")
        class Context_with_more_than_task {
            @BeforeEach
            void setAddedTask(RepetitionInfo repetitionInfo) {
                for (int i = 0; i < repetitionInfo.getCurrentRepetition(); i++) {
                    taskList.add(task);
                }
            }

            @RepeatedTest(value = repeatTime, name = "200 OK와 크기가 1이상인 리스트를 응답한다.")
            void it_respond_200_ok_and_saved_task_list() throws Exception {
                given(taskService.getTasks()).willReturn(taskList);

                outputStream = new ByteArrayOutputStream();
                objectMapper.writeValue(outputStream, taskList);
                taskJsonString = outputStream.toString();

                mockMvc.perform(requestBuilder)
                        .andExpect(status().isOk())
                        .andExpect(content().json(taskJsonString));
            }
        }
    }

    @Nested
    @DisplayName("요청이 GET /tasks/:id 이고")
    class Context_when_request_is_get_tasks_id {
        private String stringFormat = "/tasks/%d";
        private Long givenId;

        @Nested
        @DisplayName("id값이 저장된 task의 id값과 동일하다면")
        class Context_when_id_is_equal_to_saved_task_id {
            @BeforeEach
            void setRequest() {
                givenId = givenSavedTaskId;
                uriTemplate = String.format(stringFormat, givenId);
                requestBuilder = get(uriTemplate);
            }

            @Test
            @DisplayName("200 OK와 저장된 task를 응답한다.")
            void it_respond_200_ok_and_saved_task() throws Exception {
                given(taskService.getTask(givenId)).willReturn(task);

                mockMvc.perform(requestBuilder)
                        .andExpect(status().isOk())
                        .andExpect(content().json(taskJsonString));
            }
        }

        @Nested
        @DisplayName("id값이 저장된 task의 id값과 동일하지 않다면")
        class Context_when_id_is_not_equal_to_saved_task_id {
            @BeforeEach
            void setRequest() {
                givenId = givenUnsavedTaskId;
                uriTemplate = String.format(stringFormat, givenId);
                requestBuilder = get(uriTemplate);
            }

            @Test
            @DisplayName("404 Not Found를 응답한다.")
            void it_respond_404_not_found() throws Exception {
                given(taskService.getTask(givenUnsavedTaskId))
                        .willThrow(new TaskNotFoundException(givenId));

                mockMvc.perform(requestBuilder)
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("요청이 POST /tasks 이면")
    class Context_when_request_is_post_tasks {
        @BeforeEach
        void setRequest() throws IOException {
            requestBuilder = post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(taskJsonString);
        }

        @Test
        @DisplayName("task를 추가하고, 201 Created와 추가된 task를 응답한다.")
        void it_add_task_and_respond_201_created_and_added_task() throws Exception {
            given(taskService.createTask(any(Task.class))).willReturn(task);

            mockMvc.perform(requestBuilder)
                    .andExpect(status().isCreated())
                    .andExpect(content().json(taskJsonString));
        }
    }

    @Nested
    @DisplayName("요청이 PUT /tasks/:id 이면")
    class Context_when_request_is_put_tasks_id {
        private String stringFormat = "/tasks/%d";
        private Long givenId;

        @Nested
        @DisplayName("id값이 저장된 task의 id값과 동일하다면")
        class Context_when_id_is_equal_to_saved_task_id {
            @BeforeEach
            void setRequest() throws IOException {
                givenId = givenSavedTaskId;
                task.setTitle(givenTaskTitle);

                outputStream = new ByteArrayOutputStream();
                objectMapper.writeValue(outputStream, task);
                taskJsonString = outputStream.toString();

                uriTemplate = String.format(stringFormat, givenId);
                requestBuilder = put(uriTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJsonString);
            }

            @Test
            @DisplayName("task를 수정하고, 수정된 task를 응답한다.")
            void it_modify_task_and_respond_200_ok_and_modified_task() throws Exception {
                given(taskService.updateTask(any(long.class), any(Task.class))).willReturn(task);

                mockMvc.perform(requestBuilder)
                        .andExpect(status().isOk())
                        .andExpect(content().json(taskJsonString));
            }
        }

        @Nested
        @DisplayName("id값이 저장된 task의 id값과 동일하지 않다면")
        class Context_when_id_is_not_equal_to_saved_task_id {
            @BeforeEach
            void setRequest() throws IOException {
                givenId = givenUnsavedTaskId;
                task.setTitle(givenTaskTitle);

                outputStream = new ByteArrayOutputStream();
                objectMapper.writeValue(outputStream, task);
                taskJsonString = outputStream.toString();

                uriTemplate = String.format(stringFormat, givenId);
                requestBuilder = put(uriTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJsonString);
            }

            @Test
            @DisplayName("404 Not Found를 응답한다.")
            void it_respond_404_not_found() throws Exception {
                given(taskService.updateTask(any(long.class), any(Task.class)))
                        .willThrow(new TaskNotFoundException(givenId));

                mockMvc.perform(requestBuilder)
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("요청이 PATCH /tasks/:id 이면")
    class Context_when_request_is_patch_tasks_id {
        private String stringFormat = "/tasks/%d";
        private Long givenId;

        @Nested
        @DisplayName("id값이 저장된 task의 id값과 동일하다면")
        class Context_when_id_is_equal_to_saved_task_id {
            @BeforeEach
            void setRequest() throws IOException {
                givenId = givenSavedTaskId;
                task.setTitle(givenTaskTitle);

                outputStream = new ByteArrayOutputStream();
                objectMapper.writeValue(outputStream, task);
                taskJsonString = outputStream.toString();

                uriTemplate = String.format(stringFormat, givenId);
                requestBuilder = patch(uriTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJsonString);
            }

            @Test
            @DisplayName("task를 수정하고, 수정된 task를 응답한다.")
            void it_modify_task_and_respond_200_ok_and_modified_task() throws Exception {
                given(taskService.updateTask(any(long.class), any(Task.class))).willReturn(task);

                mockMvc.perform(requestBuilder)
                        .andExpect(status().isOk())
                        .andExpect(content().json(taskJsonString));
            }
        }

        @Nested
        @DisplayName("id값이 저장된 task의 id값과 동일하지 않다면")
        class Context_when_id_is_not_equal_to_saved_task_id {
            @BeforeEach
            void setRequest() throws IOException {
                givenId = givenUnsavedTaskId;
                task.setTitle(givenTaskTitle);

                outputStream = new ByteArrayOutputStream();
                objectMapper.writeValue(outputStream, task);
                taskJsonString = outputStream.toString();

                uriTemplate = String.format(stringFormat, givenId);
                requestBuilder = patch(uriTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJsonString);

                given(taskService.updateTask(any(long.class), any(Task.class)))
                        .willThrow(new TaskNotFoundException(givenId));
            }

            @Test
            @DisplayName("404 Not Found를 응답한다.")
            void it_respond_404_not_found() throws Exception {
                mockMvc.perform(requestBuilder)
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("요청이 DELETE /tasks/:id 이면")
    class Context_when_request_is_delete_tasks_id {
        private String stringFormat = "/tasks/%d";
        private Long givenId;

        @Nested
        @DisplayName("id값이 저장된 task의 id값과 동일하다면")
        class Context_when_id_is_equal_to_saved_task_id {
            @BeforeEach
            void setRequest() {
                givenId = givenSavedTaskId;

                uriTemplate = String.format(stringFormat, givenId);
                requestBuilder = delete(uriTemplate);

                given(taskService.deleteTask(givenId)).willReturn(task);
            }

            @Test
            @DisplayName("204 No Content를 응답한다.")
            void it_respond_204_no_content() throws Exception {
                mockMvc.perform(requestBuilder)
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("id값이 저장된 task의 id값과 동일하지 않다면")
        class Context_when_id_is_not_equal_to_saved_task_id {
            @BeforeEach
            void setRequest() {
                givenId = givenUnsavedTaskId;

                uriTemplate = String.format(stringFormat, givenId);
                requestBuilder = delete(uriTemplate);

                given(taskService.deleteTask(givenId))
                        .willThrow(new TaskNotFoundException(givenId));
            }

            @Test
            @DisplayName("404 Not Found를 응답한다.")
            void it_respond_404_not_found() throws Exception {
                mockMvc.perform(requestBuilder)
                        .andExpect(status().isNotFound());
            }
        }
    }
}
