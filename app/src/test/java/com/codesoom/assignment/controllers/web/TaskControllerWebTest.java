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
import java.util.Collections;
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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private enum TaskList {
        FIRST(1L, "FIRST TASK"),
        SECOND(2L, "SECOND TASK");

        private final long id;
        private final String title;

        TaskList(long id, String title) {
            this.id = id;
            this.title = title;
        }

        Task toTask() {
            return new Task(this.id, this.title);
        }

        public Long getId() {
            return this.id;
        }

        public String getTitle() {
            return this.title;
        }
    }

    @Nested
    @DisplayName("GET /tasks 요청은")
    class Describe_getTasks {

        @Test
        @DisplayName("모든 할 일 목록을 응답한다")
        void it_response_200() throws Exception {
            given(taskService.getTasks())
                .willReturn(Collections.singletonList(TaskList.FIRST.toTask()));

            mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TaskList.FIRST.getTitle())));
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
                given(taskService.getTask(TaskList.SECOND.getId()))
                    .willThrow(new TaskNotFoundException(TaskList.SECOND.getId()));

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
                given(taskService.getTask(TaskList.FIRST.getId()))
                    .willReturn(TaskList.FIRST.toTask());

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
            given(taskService.createTask(TaskList.FIRST.toTask()))
                .willReturn(TaskList.FIRST.toTask());

            mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TaskList.FIRST.toTask().toString()))
                .andExpect(status().isCreated());
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
                given(taskService.updateTask(TaskList.SECOND.getId(), TaskList.SECOND.toTask()))
                    .willThrow(new TaskNotFoundException(TaskList.SECOND.getId()));

                mockMvc.perform(put("/tasks/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TaskList.SECOND.toTask().toString()))
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 일경우")
        class Context_existTask {

            @Test
            @DisplayName("200을 응답한다")
            void it_response_200() throws Exception {
                given(taskService.updateTask(TaskList.FIRST.getId(), TaskList.SECOND.toTask()))
                    .willReturn(new Task(TaskList.FIRST.getId(), "SECOND TASK"));

                mockMvc.perform(put("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TaskList.SECOND.toTask().toString()))
                    .andExpect(status().isOk());
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
                given(taskService.updateTask(TaskList.SECOND.getId(), TaskList.SECOND.toTask()))
                    .willThrow(new TaskNotFoundException(TaskList.SECOND.getId()));

                mockMvc.perform(patch("/tasks/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TaskList.SECOND.toTask().toString()))
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 일경우")
        class Context_existTask {

            @Test
            @DisplayName("200을 응답한다")
            void it_response_200() throws Exception {
                given(taskService.updateTask(TaskList.FIRST.getId(), TaskList.SECOND.toTask()))
                    .willReturn(new Task(TaskList.FIRST.getId(), "SECOND TASK"));

                mockMvc.perform(patch("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TaskList.SECOND.toTask().toString()))
                    .andExpect(status().isOk());
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
                given(taskService.deleteTask(TaskList.SECOND.getId()))
                    .willThrow(new TaskNotFoundException(TaskList.SECOND.getId()));

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
                given(taskService.deleteTask(TaskList.FIRST.getId()))
                    .willReturn(TaskList.FIRST.toTask());

                mockMvc.perform(delete("/tasks/1"))
                    .andExpect(status().isNoContent());
            }
        }
    }
}
