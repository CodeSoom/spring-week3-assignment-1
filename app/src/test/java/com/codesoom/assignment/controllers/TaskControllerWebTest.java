package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskController Web API")
public class TaskControllerWebTest {
    public static final String FIXTURE_TITLE = "title";

    private TaskController taskController;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        taskController = new TaskController(new TaskService());
        mockMvc = MockMvcBuilders
                .standaloneSetup(taskController)
                .build();
    }

    @Nested
    @DisplayName("/tasks GET 요청은")
    class Describe_getTasks {
        @Nested
        @DisplayName("생성되어 있는 할 일이 없다면")
        class Context_didNotCreateTask {
            final Long deletedTaskId = 1L;

            @BeforeEach
            void prepare() {
                taskController.create(new Task());
                taskController.delete(deletedTaskId);
            }

            @Test
            @DisplayName("OK status, 빈 목록을 반환한다")
            void it_returnsEmptyList() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));
            }
        }

        @Nested
        @DisplayName("할 일을 생성했었을 때")
        class Context_didCreateContext {
            @BeforeEach
            void prepare() {
                final Task task1 = new Task();
                task1.setTitle(FIXTURE_TITLE + 1);
                taskController.create(task1);

                final Task task2 = new Task();
                task2.setTitle(FIXTURE_TITLE + 2);
                taskController.create(task2);
            }

            @Test
            @DisplayName("OK status, 생성된 할 일 목록을 반환한다")
            void it_returnsTasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[{\"id\":1,\"title\":\"title1\"},{\"id\":2,\"title\":\"title2\"}]"));
            }
        }
    }
}
