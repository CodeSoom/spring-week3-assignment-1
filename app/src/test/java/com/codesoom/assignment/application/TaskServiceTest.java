package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService")
public class TaskServiceTest {
    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE = "Test";

    private TaskService taskService;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        task = new Task();
        task.setTitle(TASK_TITLE);
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

        @Nested
        @DisplayName("with a task")
        class Context_with_task {
            @BeforeEach
            void addNewTask() {
                taskService.createTask(task);
            }

            @Test
            @DisplayName("return list")
            void it_return_empty() {
                assertThat(taskService.getTasks()).hasSize(1);
            }
        }
    }

    @Nested
    @DisplayName("getTask")
    class Describe_getTask {
        @BeforeEach
        void addNewTask() {
            taskService.createTask(task);
        }

        @Nested
        @DisplayName("with valid id")
        class Context_with_valid_id {
            @Test
            @DisplayName("return task")
            void it_return_empty() {
                assertThat(taskService.getTask(1L).getClass()).isEqualTo(Task.class);
                assertThat(taskService.getTask(1L).getId()).isEqualTo(TASK_ID);
                assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("with invalid id")
        class Context_with_invalid_id {
            @Test
            @DisplayName("return task")
            void it_return_empty() {
                assertThatThrownBy(() -> taskService.getTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
