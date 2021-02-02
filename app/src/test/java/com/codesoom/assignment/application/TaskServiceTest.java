package com.codesoom.assignment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskService")
public class TaskServiceTest {
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTasks")
    class Describe_getTasks {
        @Nested
        @DisplayName("without any task")
        class Context_without_any_task {
            @Test
            @DisplayName("return empty list")
            void it_return_empty() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }

    }
}
