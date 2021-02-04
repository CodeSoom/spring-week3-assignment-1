package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Task Service는")
public class NestedTaskServiceTest {

    @Nested
    @DisplayName("기존에 생성된 task가 없는 경우")
    class Describe_getTask {
        private final TaskService taskService = new TaskService();

        @Test
        @DisplayName("모든 테스크를 얻으려고 한다면 empty list를 반환한다.")
        void get_tasks_return_empty_list() {
            List<Task> tasks = taskService.getTasks();
            assertTrue(tasks.isEmpty());
        }

        @Test
        @DisplayName("특정 task를 얻으려고 한다면 TaskNotFoundException에러가 발생한다.")
        void get_task_raise_error() {
            assertThrows(
                    TaskNotFoundException.class,
                    () -> taskService.getTask(1L)
            );
        }
    }
}
