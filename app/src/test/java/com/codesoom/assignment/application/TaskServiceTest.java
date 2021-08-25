package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    private TaskService service;

    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 100L;
    private static final String TASK_TITLE = "my first task";
    private static final String NEW_TASK_TITLE = "my new task";

    private final Task newTask =  new Task(2L, NEW_TASK_TITLE);

    @Nested
    class GetTests {
        @BeforeEach
        void setup() {
            HashMap<Long, Task> tasks = new HashMap<>();
            tasks.put(VALID_ID, new Task(VALID_ID, TASK_TITLE));
            service = new TaskService(tasks);
        }

        @Test
        void getTasks() {
            Collection<Task> result = service.getTasks();

            assertThat(result).hasSize(1);
        }

        @Test
        void getTaskWithValidId() {
            Task result = service.getTask(VALID_ID);

            assertThat(result.getId())
                    .isEqualTo(VALID_ID);
            assertThat(result.getTitle())
                    .isEqualTo(TASK_TITLE);
        }

        @Test
        void getTaskWithInvalidId() {
            assertThatThrownBy(() -> service.getTask(INVALID_ID))
                    .isInstanceOf(TaskNotFoundException.class);
        }
    }

    @Nested
    class CreateTests {
        @BeforeEach
        void setup() {
            service = new TaskService();
        }

        @Test
        void createTask() {
            service.createTask(new Task(VALID_ID, TASK_TITLE));
        }
    }

    @Nested
    class UpdateTests {
        @BeforeEach
        void setup() {
            HashMap<Long, Task> tasks = new HashMap<>();
            tasks.put(VALID_ID, new Task(VALID_ID, TASK_TITLE));
            service = new TaskService(tasks);
        }

        @Test
        void updateTaskWithValidId() {
            Task result = service.updateTask(VALID_ID, newTask);

            assertThat(result.getId())
                    .isEqualTo(VALID_ID);
            assertThat(result.getTitle())
                    .isEqualTo(NEW_TASK_TITLE);
        }

        @Test
        void updateTaskWithInvalidId() {
            assertThatThrownBy(() -> service.updateTask(INVALID_ID, newTask))
                    .isInstanceOf(TaskNotFoundException.class);
        }
    }

    @Nested
    class DeleteTests {
        @BeforeEach
        void setup() {
            HashMap<Long, Task> tasks = new HashMap<>();
            tasks.put(VALID_ID, new Task(VALID_ID, TASK_TITLE));
            service = new TaskService(tasks);
        }

        @Test
        void deleteTaskWithValidId() {
            Task result = service.deleteTask(VALID_ID);

            assertThat(service.getTasks()).isEmpty();
            assertThatThrownBy(() -> service.getTask(VALID_ID))
                    .isInstanceOf(TaskNotFoundException.class);

            assertThat(result.getId()).isEqualTo(VALID_ID);
            assertThat(result.getTitle()).isEqualTo(TASK_TITLE);
        }

        @Test
        void deleteTaskWithInvalidId() {
            assertThatThrownBy(() ->  service.deleteTask(INVALID_ID))
                    .isInstanceOf(TaskNotFoundException.class);
        }
    }
}
