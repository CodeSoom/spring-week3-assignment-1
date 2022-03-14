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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskController 클래스 WebTest")
@AutoConfigureMockMvc
@SpringBootTest
public class TaskControllerWebTest {

    private static final String TEST_TASK_TITLE = "test";
    private static final Long INVALID_ID = 0L;
    private static final Long VALID_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;

    void prepareTaskService() {
        taskService.reset();
    }

    void prepareTestTask() {
        Task task = new Task();
        task.setTitle(TEST_TASK_TITLE);
        taskService.createTask(task);
    }

    @DisplayName("GET - '/tasks/{id}'는")
    @Nested
    class Describe_detail {
        @BeforeEach
        void prepare() {
            prepareTaskService();
            prepareTestTask();
        }

        @DisplayName("등록되지않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_invalid_id {
            @DisplayName("status not found를 리턴한다.")
            @Test
            void it_responses_not_found() throws Exception {
                mockMvc.perform(get("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound());
            }
        }

        @DisplayName("등록된 Task의 id가 주어진다면")
        @Nested
        class Context_with_valid_id {
            @DisplayName("status ok와 해당 id의 Task를 json 타입으로 리턴한다.")
            @Test
            void it_responses_that_task() throws Exception {
                mockMvc.perform(get("/tasks/" + VALID_ID))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(containsString(TEST_TASK_TITLE)));
            }
        }
    }

    @DisplayName("put - '/tasks/{id}'는")
    @Nested
    class Describe_put {

        private static final String UPDATE_TASK_TITLE = "updated_title";
        private String content;

        void prepareContent() throws JsonProcessingException {
            Task task = new Task();
            task.setTitle(UPDATE_TASK_TITLE);
            content = objectMapper.writeValueAsString(task);
        }

        @BeforeEach
        void prepare() throws JsonProcessingException {
            prepareTaskService();
            prepareTestTask();
            prepareContent();
        }

        @DisplayName("등록되지않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_invalid_id {
            @DisplayName("status not found를 리턴한다.")
            @Test
            void it_responses_not_found() throws Exception {
                mockMvc.perform(put("/tasks/" + INVALID_ID)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }

        @DisplayName("등록되지않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_valid_id {
            @DisplayName("status ok와 업데이트한 Task를 json 타입으로 반환한다.")
            @Test
            void it_responses_updated_task() throws Exception {
                mockMvc.perform(put("/tasks/" + VALID_ID)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(UPDATE_TASK_TITLE)));
            }
        }
    }


    @DisplayName("patch - '/tasks/{id}'는")
    @Nested
    class Describe_patch {

        private static final String UPDATE_TASK_TITLE = "updated_title";
        private String content;

        void prepareContent() throws JsonProcessingException {
            Task task = new Task();
            task.setTitle(UPDATE_TASK_TITLE);
            content = objectMapper.writeValueAsString(task);
        }

        @BeforeEach
        void prepare() throws JsonProcessingException {
            prepareTaskService();
            prepareTestTask();
            prepareContent();
        }

        @DisplayName("등록되지않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_invalid_id {
            @DisplayName("status not found를 리턴한다.")
            @Test
            void it_responses_not_found() throws Exception {
                mockMvc.perform(patch("/tasks/" + INVALID_ID)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }

        @DisplayName("등록된 Task의 id가 주어진다면")
        @Nested
        class Context_with_valid_id {
            @DisplayName("해당 id의 Task를 업데이트하고, status ok와 업데이트한 Task를 json 타입으로 리턴한다.")
            @Test
            void it_responses_updated_task() throws Exception {
                mockMvc.perform(patch("/tasks/" + VALID_ID)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(UPDATE_TASK_TITLE)));
            }
        }
    }

    @DisplayName("DELETE - '/tasks/{id}'는")
    @Nested
    class Describe_delete {
        @BeforeEach
        void prepare() {
            prepareTaskService();
            prepareTestTask();
        }

        @DisplayName("등록되지않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_invalid_id {
            @DisplayName("status not found를 리턴한다.")
            @Test
            void it_responses_not_found() throws Exception {
                mockMvc.perform(delete("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound());
            }
        }

        @DisplayName("등록된 Task의 id가 주어진다면")
        @Nested
        class Context_with_valid_id {
            @DisplayName("해당 id의 Task를 삭제하고 status no content를 리턴한다.")
            @Test
            void it_responses_no_content() throws Exception {
                mockMvc.perform(delete("/tasks/" + VALID_ID))
                        .andExpect(status().isNoContent());
            }
        }
    }

    @DisplayName("POST - '/tasks' 는")
    @Nested
    class Describe_create {
        @DisplayName("생성할 Task가 주어진다면")
        @Nested
        class Context_with_new_task {
            private static final String NEW_TASK_TITLE = "new";
            private String content;

            void prepareContent() throws JsonProcessingException {
                Task task = new Task();
                task.setTitle(NEW_TASK_TITLE);
                content = objectMapper.writeValueAsString(task);
            }

            @BeforeEach
            void prepare() throws JsonProcessingException {
                prepareTaskService();
                prepareContent();
            }

            @DisplayName("해당 Task를 생성하고, status created과 함께 리턴한다.")
            @Test
            void it_responses_new_task() throws Exception {
                mockMvc.perform(post("/tasks")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(NEW_TASK_TITLE)));

            }
        }
    }

    @DisplayName("GET - '/tasks' 는")
    @Nested
    class Describe_list {
        @DisplayName("등록된 Task가 있다면")
        @Nested
        class Context_with_task {

            @BeforeEach
            void prepare() {
                prepareTaskService();
                prepareTestTask();
            }

            @DisplayName("status ok과 등록된 모든 Task의 List를 json타입으로 리턴한다.")
            @Test
            void it_responses_list_with_test_task() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(containsString(TEST_TASK_TITLE)));
            }
        }

        @DisplayName("등록된 Task가 없다면")
        @Nested
        class Context_without_task {

            @BeforeEach
            void prepare() {
                prepareTaskService();
            }

            @DisplayName("status ok와 비어있는 리스트를 리턴한다.")
            @Test
            void it_responses_list_with_test_task() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(containsString("[]")));
            }
        }
    }
}
