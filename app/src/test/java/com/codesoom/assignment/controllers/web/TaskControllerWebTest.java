package com.codesoom.assignment.controllers.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import javax.xml.bind.annotation.XmlType.DEFAULT;
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

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    private static final String DEFAULT_TASK_TITLE = "DEFAULT TASK TITLE";
    private static final String NEW_TASK_TITLE = "NEW TASK TITLE";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final Task task1 = new Task(1L, DEFAULT_TASK_TITLE);
    private final Task task2 = new Task(2L, DEFAULT_TASK_TITLE);

    @Nested
    @DisplayName("GET /tasks 요청은")
    class Describe_getTasks {

        @Test
        @DisplayName("모든 할 일 목록을 응답한다")
        void it_response_200() throws Exception {
            given(taskService.getTasks())
                .willReturn(Collections.singletonList(task1));

            mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(DEFAULT_TASK_TITLE)));
        }
    }

    @Nested
    @DisplayName("GET /tasks/{id} 요청은")
    class Describe_getTask {

        @Nested
        @DisplayName("존재하지 않는 할 일 일경우")
        class Context_notExistTask {

            @Test
            @DisplayName("404를 응답한다")
            void it_response_404() throws Exception {
                given(taskService.getTask(2L))
                    .willThrow(new TaskNotFoundException(2L));

                mockMvc.perform(get("/tasks/2"))
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 일경우")
        class Context_existTask {

            @Test
            @DisplayName("200을 응답한다")
            void it_response_200() throws Exception {
                given(taskService.getTask(1L))
                    .willReturn(task1);

                mockMvc.perform(get("/tasks/1"))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("POST /tasks 요청은")
    class Describe_createTask {

        @Test
        @DisplayName("201을 응답한다")
        void it_response_201() throws Exception {
            given(taskService.createTask(task1))
                .willReturn(task1);

            mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(task1)))
                .andExpect(status().isCreated());
        }

        private String toString(Object object) throws JsonProcessingException {
            return new ObjectMapper().writeValueAsString(object);
        }
    }

    @Nested
    @DisplayName("PUT /tasks/{id} 요청은")
    class Describe_updateTaskPut {

        @Nested
        @DisplayName("존재하지 않는 할 일 일경우")
        class Context_notExistTask {

            @Test
            @DisplayName("404을 응답한다")
            void it_response_404() throws Exception {
                given(taskService.updateTask(2L, task2))
                    .willThrow(new TaskNotFoundException(2L));

                mockMvc.perform(put("/tasks/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toString(task2)))
                    .andExpect(status().isNotFound());
            }

            private String toString(Object object) throws JsonProcessingException {
                return new ObjectMapper().writeValueAsString(object);
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 일경우")
        class Context_existTask {

            @Test
            @DisplayName("200을 응답한다")
            void it_response_200() throws Exception {
                given(taskService.updateTask(1L, task2))
                    .willReturn(new Task(1L, NEW_TASK_TITLE));

                mockMvc.perform(put("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toString(task2)))
                    .andExpect(status().isOk());
            }

            private String toString(Object object) throws JsonProcessingException {
                return new ObjectMapper().writeValueAsString(object);
            }
        }
    }

    @Nested
    @DisplayName("PATCH /tasks/{id} 요청은")
    class Describe_updateTaskPatch {

        @Nested
        @DisplayName("존재하지 않는 할 일 일경우")
        class Context_notExistTask {

            @Test
            @DisplayName("404을 응답한다")
            void it_response_404() throws Exception {
                given(taskService.updateTask(2L, task2))
                    .willThrow(new TaskNotFoundException(2L));

                mockMvc.perform(patch("/tasks/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toString(task2)))
                    .andExpect(status().isNotFound());
            }

            private String toString(Object object) throws JsonProcessingException {
                return new ObjectMapper().writeValueAsString(object);
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 일경우")
        class Context_existTask {

            @Test
            @DisplayName("200을 응답한다")
            void it_response_200() throws Exception {
                given(taskService.updateTask(1L, task2))
                    .willReturn(new Task(1L, NEW_TASK_TITLE));

                mockMvc.perform(patch("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toString(task2)))
                    .andExpect(status().isOk());
            }

            private String toString(Object object) throws JsonProcessingException {
                return new ObjectMapper().writeValueAsString(object);
            }
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{id} 요청은")
    class Describe_deleteTask {

        @Nested
        @DisplayName("존재하지 않는 할 일 일경우")
        class Context_notExistTask {

            @Test
            @DisplayName("404을 응답한다")
            void it_response_404() throws Exception {
                given(taskService.deleteTask(2L))
                    .willThrow(new TaskNotFoundException(2L));

                mockMvc.perform(delete("/tasks/2"))
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 일경우")
        class Context_existTask {

            @Test
            @DisplayName("204를 응답한다")
            void it_response_204() throws Exception {
                given(taskService.deleteTask(1L))
                    .willReturn(task1);

                mockMvc.perform(delete("/tasks/1"))
                    .andExpect(status().isNoContent());
            }
        }
    }
}
