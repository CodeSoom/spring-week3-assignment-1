package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스의")
class TaskServiceTest {
    TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_get_task {
        @Nested
        @DisplayName("주어진 식별자를 가진 작업이 존재하면")
        class Context_with_id {
            @Test
            @DisplayName("작업을 리턴한다")
            void it_returns_task_with_id() {
                Task task = new Task(1L, "BJP");
                taskService.createTask(task);

                assertThat(taskService.getTask(1L)).isEqualTo(task);
            }
        }

        @Nested
        @DisplayName("주어진 식별자를 가진 작업이 없으면")
        class Context_without_id {
            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> taskService.getTask(1L))
                        .isExactlyInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_get_tasks {
        @Nested
        @DisplayName("작업을 가진 목록이 주어지면")
        class Context_with_tasks {
            @Test
            @DisplayName("작업 목록을 리턴한다")
            void It_returns_tasks() {
                taskService.createTask(new Task(null, "BJP"));
                taskService.createTask(new Task(null, "BJP"));

                assertThat(taskService.getTasks().size()).isEqualTo(2);
            }
        }

        @Nested
        @DisplayName("빈 작업 목록이 주어지면")
        class Context_with_empty_tasks {
            @Test
            @DisplayName("빈 배열을 리턴한다")
            void It_returns_empty_array() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_create_task {
        @Nested
        @DisplayName("작업이 주어졌을 때")
        class Context_with_task {
            @Test
            @DisplayName("식별자를 가진 작업을 생성하고 리턴한다")
            void It_returns_task_with_id() {
                assertThat(taskService.createTask(new Task(null, "BJP")))
                        .isEqualTo(new Task(1L, "BJP"));
                assertThat(taskService.createTask(new Task(null, "BJP")))
                        .isEqualTo(new Task(2L, "BJP"));
            }
        }
    }
}