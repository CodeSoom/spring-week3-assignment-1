package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Task Controller 클래스")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TEST_TITLE = "test";
    private static final String TEST_POSTFIX = "_!!!";
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 0L;

    @Nested
    @DisplayName("GET Mapping - Task 조회 요청은")
    class Describe_get_mapping {
        @Nested
        @DisplayName("만약 등록된 Task가 있다면")
        class Context_has_task {
            final int givenTaskCnt = 5;

            @BeforeEach
            void prepare() {
                List<Task> tasks = new ArrayList<>();
                for (int i = 0; i < givenTaskCnt; i++) {
                    tasks.add(getTask());
                }

                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("200(Ok)과 등록된 Task 전체 리스트를 응답합니다.")
            void it_return_ok_and_tasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(givenTaskCnt)))
                        .andDo(print());
            }
        }
        @Nested
        @DisplayName("만약 등록된 Task가 없다면")
        class Context_has_not_task {
            @Test
            @DisplayName("200(Ok)과 빈 리스트를 응답합니다.")
            void it_return_ok_and_empty_tasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(0)))
                        .andDo(print());
            }
        }


        @Nested
        @DisplayName("만약 등록된 Task의 id 가 주어진다면")
        class Context_with_id {
            @BeforeEach
            void prepare() {
                given(taskService.getTask(VALID_ID)).willReturn(getTask());
            }

            @Test
            @DisplayName("200(Ok) 과 등록된 Task 를 응답합니다..")
            void it_return_ok_and_task() throws Exception {
                mockMvc.perform(get("/tasks/" + VALID_ID))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title").value(TEST_TITLE))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 등록되지 않는 id 가 주어진다면")
        class Context_with_invalid_id {
            @BeforeEach
            void prepare() {
                given(taskService.getTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));
            }

            @Test
            @DisplayName("404(Not found) 를 응답합니다.")
            void it_return_taskNotFoundException() throws Exception {
                mockMvc.perform(get("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("POST Mapping - Task 생성 요청은")
    class Describe_post_mapping {
        @Nested
        @DisplayName("만약 등록할 Task 가 주어진다면")
        class Context_with_task {
            @BeforeEach
            void prepare() {
                when(taskService.createTask(any(Task.class)))
                        .then((arg) -> {
                            Optional<Task> source = Optional.of(arg.getArgument(0, Task.class));

                            Task task = source.orElseThrow(() -> new NullPointerException());

                            Task responseTask = new Task();
                            responseTask.setId(VALID_ID);
                            responseTask.setTitle(task.getTitle());
                            return responseTask;
                        });
            }

            @Test
            @DisplayName("201(Created) 과 Task 를 응답합니다.")
            void it_create_task_return_created_and_task() throws Exception {
                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getSourceTaskContent()))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.title").value(TEST_TITLE + TEST_POSTFIX))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 등록할 Task 가 없다면")
        class Context_without_task {
            @Test
            @DisplayName("400(Bad Request) 를 응답합니다.")
            void it_return_notFound() throws Exception {
                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("PUT 또는 PATCH Mapping - Task 업데이트 요청은")
    class Describe_put_or_patch_mapping {
        @BeforeEach
        void prepare() {
            when(taskService.updateTask(any(Long.class), any(Task.class)))
                    .then((arg) -> {
                        Long targetId = arg.getArgument(0, Long.class);
                        Task source = arg.getArgument(1, Task.class);

                        if (targetId.equals(INVALID_ID)) {
                            throw new TaskNotFoundException(INVALID_ID);
                        }

                        Task responseTask = new Task();
                        responseTask.setId(targetId);
                        responseTask.setTitle(source.getTitle());
                        return responseTask;
                    });
        }

        @Nested
        @DisplayName("만약 PUT 요청으로 등록된 Task의 id 와 Task 가 주어진다면")
        class Context_with_id_and_task_in_put_request {
            @Test
            @DisplayName("200(Ok) 과 Task 를 응답합니다.")
            void it_update_task_return_ok_and_task() throws Exception {
                mockMvc.perform(put("/tasks/" + VALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getSourceTaskContent()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(VALID_ID))
                        .andExpect(jsonPath("$.title").value(TEST_TITLE + TEST_POSTFIX))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 PATCH 요청으로 등록된 Task의 id 와 수정할 Task 가 주어진다면")
        class Context_with_id_and_task_in_patch_request {
            @Test
            @DisplayName("200(Ok) 과 Task 를 응답합니다.")
            void it_update_task_return_ok_and_task() throws Exception {
                mockMvc.perform(patch("/tasks/" + VALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getSourceTaskContent()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(VALID_ID))
                        .andExpect(jsonPath("$.title").value(TEST_TITLE + TEST_POSTFIX));
            }
        }

        @Nested
        @DisplayName("만약 등록된 Task의 id 만 주어진다면")
        class Context_with_id {
            @Test
            @DisplayName("400(Bad Request) 를 응답합니다.")
            void it_return_methodNotAllowed() throws Exception {
                mockMvc.perform(put("/tasks/" + VALID_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 수정할 Task 만 주어진다면")
        class Context_with_task {
            @Test
            @DisplayName("405(Method Not Allowed) 를 응답합니다.")
            void it_return_methodNotAllowed() throws Exception {
                mockMvc.perform(put("/tasks/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getSourceTaskContent()))
                        .andExpect(status().isMethodNotAllowed())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 등록되지 않은 Task의 id와 수정할 Task가 주어진다면 ")
        class Context_with_invalid_id_and_task {
            @Test
            @DisplayName("404(NOT_FOUND) 를 응답합니다.")
            void it_return_notFound() throws Exception {
                mockMvc.perform(put("/tasks/" + INVALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getSourceTaskContent()))
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("DELETE Mapping - Task 삭제 요청은")
    class Describe_delete_mapping {
        @Nested
        @DisplayName("만약 등록된 Task의 id가 주어진다면")
        class Context_with_id {
            @BeforeEach
            void prepare() {
                given(taskService.deleteTask(VALID_ID)).willReturn(null);
            }

            @Test
            @DisplayName("204(No Content) 과 빈값을 응답합니다.")
            void it_delete_task_return_noContent() throws Exception {
                mockMvc.perform(delete("/tasks/" + VALID_ID))
                        .andExpect(status().isNoContent())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 등록되지 않은 Task의 id가 주어진다면")
        class Context_with_invalid_id {
            @BeforeEach
            void prepare() {
                given(taskService.deleteTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));
            }

            @Test
            @DisplayName("404(NOT_FOUND) 를 응답합니다.")
            void it_delete_task_return_notFound() throws Exception {
                mockMvc.perform(delete("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }
        }
    }

    private String getSourceTaskContent() throws JsonProcessingException {
        Task source = new Task();
        source.setTitle(TEST_TITLE + TEST_POSTFIX);

        return objectMapper.writeValueAsString(source);
    }

    private Task getTask() {
        Task task = new Task();
        task.setTitle(TEST_TITLE);

        return task;
    }
}
