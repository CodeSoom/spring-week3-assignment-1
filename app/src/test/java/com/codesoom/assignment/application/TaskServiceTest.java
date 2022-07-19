package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스의")
class TaskServiceTest {
    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_get_task {
        @Nested
        @DisplayName("주어진 식별자를 가진 작업이 존재하면")
        class Context_with_id {
            @Test
            @DisplayName("작업을 리턴한다")
            void it_returns_task_with_id() {
                TaskService taskService = new TaskService();
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
                TaskService taskService = new TaskService();

                assertThatThrownBy(() -> taskService.getTask(1L))
                        .isExactlyInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}