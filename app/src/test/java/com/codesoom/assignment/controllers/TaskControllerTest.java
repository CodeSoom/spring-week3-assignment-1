package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final long TASK_ID = 0;
    private final String TASK_TITLE = "sample";

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("Task 가 하나도 없을 때")
        class Context_without_any_tasks {
            @BeforeEach
            void setEmpty() {
                given(taskService.getTasks())
                        .willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("빈 배열을 리턴한다.")
            void It_returns_empty() throws Exception {
                final String expectContent = "[]";

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(expectContent));
            }
        }

        @Nested
        @DisplayName("Task 가 있을 때")
        class Context_with_tasks {
            @BeforeEach
            void setTasks() {
                Task task = new Task();
                task.setId(TASK_ID);
                task.setTitle(TASK_TITLE);

                List<Task> tasks = new ArrayList<>();
                tasks.add(task);

                given(taskService.getTasks())
                        .willReturn(tasks);
            }

            @Test
            @DisplayName("task 가 담겨있는 JSON Array 문자열을 리턴한다.")
            void It_returns_JSON_array_string() throws Exception {
                final String expectContent = String.format("[{\"id\":%d,\"title\":\"%s\"}]", TASK_ID, TASK_TITLE);

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(expectContent));
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("찾는 id가 없을 때")
        class Context_without_target_id {

            @BeforeEach
            void setEmpty() {
                Task task = new Task();
                task.setId(TASK_ID);
                task.setTitle(TASK_TITLE);

                given(taskService.getTask(TASK_ID))
                        .willThrow(new TaskNotFoundException(TASK_ID));
            }

            @Test
            @DisplayName("TaskNotFoundException 을 던진다.")
            void It_throws_TaskNotFoundException() throws Exception {
                mockMvc.perform(get(String.format("/tasks/%d", TASK_ID)))
                        .andExpect(status().isNotFound())
                        .andExpect(
                                result -> assertThat(result.getResolvedException())
                                        .isInstanceOf(TaskNotFoundException.class)
                        );
            }
        }

        @Nested
        @DisplayName("찾는 id가 있을 때")
        class Context_with_target_id {
            @BeforeEach
            void setTask() {
                Task task = new Task();
                task.setId(TASK_ID);
                task.setTitle(TASK_TITLE);

                given(taskService.getTask(TASK_ID))
                        .willReturn(task);
            }

            @Test
            @DisplayName("task 를 리턴한다.")
            void It_returns_task() throws Exception {
                final String expectContent = String.format("{\"id\":%d,\"title\":\"%s\"}", TASK_ID, TASK_TITLE);

                mockMvc.perform(get(String.format("/tasks/%d", TASK_ID)))
                        .andExpect(status().isOk())
                        .andExpect(content().string(expectContent));
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("변경 하려는 id가 있을 때")
        class Context_with_target_id {

        }

        @Nested
        @DisplayName("변경 하려는 id가 없을 때")
        class Context_without_target_id {

        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {

        @Nested
        @DisplayName("변경 하려는 id가 있을 때")
        class Context_with_target_id {

        }

        @Nested
        @DisplayName("변경 하려는 id가 없을 때")
        class Context_without_target_id {

        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("삭제 하려는 id가 있을 때")
        class Context_with_target_id {

        }

        @Nested
        @DisplayName("삭제 하려는 id가 없을 때")
        class Context_without_target_id {

        }
    }
}