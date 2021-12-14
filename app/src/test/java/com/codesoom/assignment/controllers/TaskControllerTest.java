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

    @Nested
    @DisplayName("GET Mapping - Task 조회")
    class Describe_get_mapping {
        @Nested
        @DisplayName("만약 id 값이 없다면")
        class Context_without_id {
            @Test
            @DisplayName("200(Ok) 과 tasks 가 반환됩니다.")
            void it_return_ok_and_task() throws Exception {
                // given
                List<Task> tasks = new ArrayList<>();
                Task task = getTask();
                tasks.add(task);

                given(taskService.getTasks()).willReturn(tasks);

                // when & then
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 id 값이 있다면")
        class Context_with_id {
            @Test
            @DisplayName("200(Ok) 과 task 가 반환됩니다.")
            void it_return_ok_and_task() throws Exception {
                // given
                Task task = getTask();

                given(taskService.getTask(1L)).willReturn(task);

                // when & then
                mockMvc.perform(get("/tasks/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title").value(TEST_TITLE))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 id 값이 존재하지 않다면")
        class Context_with_invalid_id {
            @Test
            @DisplayName("404(Not found) 가 반환됩니다.")
            void it_return_taskNotFoundException() throws Exception {
                // given
                given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));

                // when & then
                mockMvc.perform(get("/tasks/100"))
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }
        }

        private Task getTask() {
            Task task = new Task();
            task.setTitle(TEST_TITLE);

            return task;
        }
    }

    @Nested
    @DisplayName("POST Mapping - Task 생성")
    class Describe_post_mapping {
        @Nested
        @DisplayName("만약 Task 가 있다면")
        class Context_with_task {
            @BeforeEach
            void setGiven() {
                when(taskService.createTask(any(Task.class)))
                        .then((arg) -> {
                            Optional<Task> source = Optional.of(arg.getArgument(0, Task.class));

                            Task task = source.orElseThrow(() -> new NullPointerException());

                            Task responseTask = new Task();
                            responseTask.setId(1L);
                            responseTask.setTitle(task.getTitle());
                            return responseTask;
                        });
            }

            @Test
            @DisplayName("201(Created) 과 task 가 반환됩니다.")
            void it_create_task_return_created_and_task() throws Exception {
                // given
                String sourceTaskContent = getSourceTaskContent();

                // when & then
                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sourceTaskContent))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.title").value(TEST_TITLE + TEST_POSTFIX))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 Task 가 없다면")
        class Context_without_task {
            @Test
            @DisplayName("400(Bad Request) 가 반환됩니다.")
            void it_return_notFound() throws Exception {
                // when & then
                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("PUT 또는 PATCH Mapping - Task 업데이트")
    class Describe_put_or_patch_mapping {
        @BeforeEach
        void setGiven() {
            // given
            when(taskService.updateTask(any(Long.class), any(Task.class)))
                    .then((arg) -> {
                        Long targetId = arg.getArgument(0, Long.class);
                        Task source = arg.getArgument(1, Task.class);

                        if (targetId.equals(100L)) {
                            throw new TaskNotFoundException(100L);
                        }

                        Task responseTask = new Task();
                        responseTask.setId(targetId);
                        responseTask.setTitle(source.getTitle());
                        return responseTask;
                    });
        }

        @Nested
        @DisplayName("만약 PUT 요청으로 id 와 task 가 있다면")
        class Context_with_id_and_task_in_put_request {
            @Test
            @DisplayName("200(Ok) 과 task 가 반환됩니다.")
            void it_update_task_return_ok_and_task() throws Exception {
                // given
                String sourceTaskContent = getSourceTaskContent();

                // when & then
                mockMvc.perform(put("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sourceTaskContent))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.title").value(TEST_TITLE + TEST_POSTFIX))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 PATCH 요청으로 id 와 task 가 있다면")
        class Context_with_id_and_task_in_patch_request {
            @Test
            @DisplayName("200(Ok) 과 task 가 반환됩니다.")
            void it_update_task_return_ok_and_task() throws Exception {
                // given
                String sourceTaskContent = getSourceTaskContent();

                // when & then
                mockMvc.perform(patch("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sourceTaskContent))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.title").value(TEST_TITLE + TEST_POSTFIX));
            }
        }

        @Nested
        @DisplayName("만약 id 만 있다면")
        class Context_with_id {
            @Test
            @DisplayName("405(Method Not Allowed) 가 반환됩니다.")
            void it_return_methodNotAllowed() throws Exception {
                // when & then
                mockMvc.perform(put("/tasks/")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isMethodNotAllowed())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 task 만 있다면")
        class Context_with_task {
            @Test
            @DisplayName("405(Method Not Allowed) 가 반환됩니다.")
            void it_return_methodNotAllowed() throws Exception {
                String sourceTaskContent = getSourceTaskContent();

                // when & then
                mockMvc.perform(put("/tasks/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sourceTaskContent))
                        .andExpect(status().isMethodNotAllowed())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 id와 task가 있다면 ")
        class Context_with_invalid_id_and_task {
            @Test
            @DisplayName("404(NOT_FOUND) 가 반환됩니다.")
            void it_return_notFound() throws Exception {
                // given
                String sourceTaskContent = getSourceTaskContent();

                // when & then
                mockMvc.perform(put("/tasks/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sourceTaskContent))
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("DELETE Mapping - Task 삭제")
    class Describe_delete_mapping {
        @BeforeEach
        void setGiven() {
            // given
            when(taskService.deleteTask(any(Long.class)))
                    .then((arg) -> {
                        Long targetId = arg.getArgument(0, Long.class);

                        if (targetId.equals(100L)) {
                            throw new TaskNotFoundException(100L);
                        }

                        return taskService.getTask(targetId);
                    });
        }

        @Nested
        @DisplayName("만약 id가 있다면")
        class Context_with_id {
            @Test
            @DisplayName("204(No Content) 과 빈값이 반환됩니다.")
            void it_delete_task_return_noContent() throws Exception {
                // when & then
                mockMvc.perform(delete("/tasks/1"))
                        .andExpect(status().isNoContent())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 id가 있다면")
        class Context_with_invalid_id {
            @Test
            @DisplayName("404(NOT_FOUND) 가 반환됩니다.")
            void it_delete_task_return_noContent() throws Exception {
                // when & then
                mockMvc.perform(delete("/tasks/100"))
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
}
