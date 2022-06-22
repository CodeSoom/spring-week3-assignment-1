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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    static final String TASK_TITLE = "Test Task Title";
    static final String CREATED_TASK_TITLE = "Created Test Task Title";
    static final String UPDATED_TITLE = "Test Title Updated";
    static final Long TASK_ID = 1L;
    static final Long INVALID_ID = 100L;
    static final String EMPTY_LIST = "[]";
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    private TaskService taskService;

    @BeforeEach
    void setup() {

        taskService = new TaskService();
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new TaskController(taskService))
                .setControllerAdvice(TaskErrorAdvice.class)
                .build();
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("저장되어 있는 task가 없다면")
        class No_task {
            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_returns_empty_List() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo(EMPTY_LIST)));
            }

        }

        @Nested
        @DisplayName("만약 task가 3개 있으면")
        class Has_task {
            final static int TASK_SIZE = 3;

            @BeforeEach
            void setup() {
                Task task = new Task();
                for (int i = 1; i <= TASK_SIZE; i++) {
                    task.setTitle("test title-" + i);
                    taskService.createTask(task);
                }
            }

            @Test
            @DisplayName("크기 3의 리스트를 반환한다.")
            void it_retruns_tasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(3)))
                        .andExpect(jsonPath("$[0].title").value("test title-1"))
                        .andExpect(jsonPath("$[0].id").value("1"))
                        .andExpect(jsonPath("$[1].title").value("test title-2"))
                        .andExpect(jsonPath("$[1].id").value("2"))
                        .andExpect(jsonPath("$[2].title").value("test title-3"))
                        .andExpect(jsonPath("$[2].id").value("3"));

            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("만약 저장되어 있지 않는 task의 ID가 주어지면")
        class No_task {

            @Test
            @DisplayName("404NotFound를 반환한다.")
            void it_throws_TaskNotFoundException() throws Exception {
                mockMvc.perform(get("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있는 task의 ID가 주어지면")
        class Has_task {
            @BeforeEach
            void setup() {
                Task task = new Task();
                task.setTitle(TASK_TITLE);
                taskService.createTask(task);
            }

            @Test
            @DisplayName("찾은 task를 반환한다.")
            void return_found_task() throws Exception {
                mockMvc.perform(get("/tasks/" + TASK_ID))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.title").value(TASK_TITLE))
                        .andExpect(jsonPath("$.id").value(TASK_ID));

            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("만약 task가 주어지면")
        class Create_Task {
            Task task;

            @Test
            @DisplayName("주어진 task를 등록하고 등록한 task를 리턴한다.")
            void it_return_created_task() throws Exception {
                task = new Task();
                task.setTitle(CREATED_TASK_TITLE);
                mockMvc.perform(post("/tasks")
                                .content(objectMapper.writeValueAsString(task))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.title").value(CREATED_TASK_TITLE));
            }

        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        Task source;

        @BeforeEach
        void setup() {
            Task savedtask = new Task();
            savedtask.setTitle(TASK_TITLE);
            taskService.createTask(savedtask);
            source = new Task();
            source.setTitle(UPDATED_TITLE);
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않는 task의 ID가 주어지면")
        class No_task {

            @Test
            @DisplayName("TasksNotFoundException을 던진다.")
            void it_throws_TaskNotFoundException() throws Exception {
                mockMvc.perform(put("/tasks/" + INVALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(source)))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있는 task의 ID가 주어지면")
        class Has_task {
            @Test
            @DisplayName("task를 업데이트 하고 업데이트된 task를 리턴한다.")
            void update_task() throws Exception {
                mockMvc.perform(put("/tasks/" + TASK_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(source)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.title").value(UPDATED_TITLE))
                        .andExpect(jsonPath("$.id").value(TASK_ID));
            }
        }

    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {
        Task source;

        @BeforeEach
        void setup() {
            Task savedtask = new Task();
            savedtask.setTitle(TASK_TITLE);
            taskService.createTask(savedtask);
            source = new Task();
            source.setTitle(UPDATED_TITLE);
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않는 task의 ID가 주어지면")
        class No_task {

            @Test
            @DisplayName("TasksNotFoundException을 던진다.")
            void it_throws_TaskNotFoundException() throws Exception {
                mockMvc.perform(put("/tasks/" + INVALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(source)))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있는 task의 ID가 주어지면")
        class Has_task {
            @Test
            @DisplayName("task를 업데이트 하고 업데이트된 task를 리턴한다.")
            void update_task() throws Exception {
                mockMvc.perform(put("/tasks/" + TASK_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(source)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.title").value(UPDATED_TITLE))
                        .andExpect(jsonPath("$.id").value(TASK_ID));
            }
        }


    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @BeforeEach
        void setup() {
            Task savedtask = new Task();
            savedtask.setTitle(TASK_TITLE);
            taskService.createTask(savedtask);
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않는 task의 ID가 주어지면")
        class No_task {

            @Test
            @DisplayName("TasksNotFoundException을 던진다.")
            void it_throws_TaskNotFoundException() throws Exception {
                mockMvc.perform(delete("/tasks/" + INVALID_ID))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있는 task의 ID가 주어지면")
        class has_task {
            @Test
            @DisplayName("task를 삭제한다.")
            void delete_task() throws Exception{
                assertThat(taskService.getTask(TASK_ID).getId()).isEqualTo(TASK_ID);
                mockMvc.perform(delete("/tasks/" + TASK_ID))
                                .andExpect(status().isNoContent());
                assertThatThrownBy(() -> taskService.getTask(TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}