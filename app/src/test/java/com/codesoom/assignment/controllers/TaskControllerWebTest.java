package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    private static final Long INVALID_ID = 100L;
    private static final String TASK_TITLE = "my first task";
    private final Task taskFixture = new Task(1L, TASK_TITLE);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private <T> T getResponseContent(ResultActions actions, TypeReference<T> type)
            throws UnsupportedEncodingException, JsonProcessingException {
        MvcResult mvcResult = actions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, type);
    }

    @Nested
    @DisplayName("Post Request")
    class PostRequest {
        @Test
        @DisplayName("returns a created task with HTTP status code 201")
        void returnsNewTask() throws Exception {
            mockMvc.perform(post("/tasks")
                            .content(objectMapper.writeValueAsString(taskFixture))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(TASK_TITLE)));
        }

        @Test
        @DisplayName("increases the number of task by 1")
        void increasesNumberOfTask() throws Exception {
            Collection<Task> tasksBeforePost = getResponseContent(
                    mockMvc.perform(get("/tasks")),
                    new TypeReference<Collection<Task>>() {}
            );

            mockMvc.perform(post("/tasks")
                    .content(objectMapper.writeValueAsString(taskFixture))
                    .contentType(MediaType.APPLICATION_JSON));

            Collection<Task> tasksAfterPost = getResponseContent(
                    mockMvc.perform(get("/tasks")),
                    new TypeReference<Collection<Task>>() {}
            );

            assertThat(tasksAfterPost).hasSize(tasksBeforePost.size() + 1);
        }
    }

    @Nested
    @DisplayName("Get Request")
    class GetRequest {
        Task taskInService;

        @BeforeEach
        void setup() throws Exception {
            ResultActions resultActions = mockMvc.perform(post("/tasks")
                    .content(objectMapper.writeValueAsString(taskFixture))
                    .contentType(MediaType.APPLICATION_JSON));

            taskInService = getResponseContent(resultActions, new TypeReference<Task>() {});
        }

        @Nested
        @DisplayName("without path variables")
        class WithoutPathVariables {
            @Test
            @DisplayName("returns a task list with http status code 200")
            void returnsTasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TASK_TITLE)));
            }
        }

        @Nested
        @DisplayName("with a valid test id")
        class WithValidId {
            @Test
            @DisplayName("returns the task with http status code 200")
            void returnsTask() throws Exception {
                mockMvc.perform(get("/tasks/" + taskInService.getId()))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TASK_TITLE)));
            }
        }

        @Nested
        @DisplayName("with an invalid test id")
        class WithInvalidId {
            @Test
            @DisplayName("returns a 404 error")
            void returnsError() throws Exception {
                mockMvc.perform(get("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("Put Request")
    class PutRequest {
        Task taskInService;

        @BeforeEach
        void setup() throws Exception {
            ResultActions resultActions = mockMvc.perform(post("/tasks")
                    .content(objectMapper.writeValueAsString(taskFixture))
                    .contentType(MediaType.APPLICATION_JSON));

            taskInService = getResponseContent(resultActions, new TypeReference<Task>() {});
        }

        @Nested
        @DisplayName("with a valid id")
        class WithValidId {
            @Test
            @DisplayName("returns an updated task with HTTP status code 200")
            void returnsUpdatedTask() throws Exception {
                mockMvc.perform(put("/tasks/" + taskInService.getId())
                                .content(objectMapper.writeValueAsString(taskFixture))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(taskFixture.getTitle())));
            }
        }

        @Nested
        @DisplayName("with an invalid id")
        class WithInvalidId {
            @Test
            @DisplayName("returns a 404 error")
            void returnsError() throws Exception {
                mockMvc.perform(put("/tasks/" + INVALID_ID)
                                .content(objectMapper.writeValueAsString(taskFixture))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("Delete Request")
    class DeleteRequest {
        Task taskInService;
        @BeforeEach
        void setup() throws Exception {
            ResultActions resultActions = mockMvc.perform(post("/tasks")
                    .content(objectMapper.writeValueAsString(taskFixture))
                    .contentType(MediaType.APPLICATION_JSON));

            taskInService = getResponseContent(resultActions, new TypeReference<Task>() {});
        }

        @Nested
        @DisplayName("with a valid id")
        class WithValidId {
            @Test
            @DisplayName("returns a deleted task with HTTP status code 204")
            void returnsDeletedTask() throws Exception {
                // TODO: fix delete controller
                mockMvc.perform(delete("/tasks/" + taskInService.getId()))
                        .andExpect(status().isNoContent());
            }

            @Test
            @DisplayName("reduces the number of tasks by one")
            void reducesNumberOfTasks() throws Exception {
                Collection<Task> tasksBeforeDelete = getResponseContent(
                        mockMvc.perform(get("/tasks")),
                        new TypeReference<Collection<Task>>() {}
                );

                mockMvc.perform(delete("/tasks/" + taskInService.getId()));

                Collection<Task> tasksAfterDelete = getResponseContent(
                        mockMvc.perform(get("/tasks")),
                        new TypeReference<Collection<Task>>() {}
                );

                assertThat(tasksAfterDelete).hasSize(tasksBeforeDelete.size() - 1);
            }
        }

        @Nested
        @DisplayName("with an invalid id")
        class WithInvalidId {
            @Test
            @DisplayName("returns a 404 error")
            void returnsError() throws Exception {
                mockMvc.perform(delete("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
