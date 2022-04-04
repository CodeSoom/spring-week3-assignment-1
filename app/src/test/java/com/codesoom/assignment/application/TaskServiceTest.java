package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)
    private static final String TASK_TITLE = "test";
    private static final String UPDATE_TITLE = "other";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    void getTasks() {
        assertThat(taskService.getTasks()).isNotEmpty();
        assertThat(taskService.getTasks()).hasSize(1);
    }

    @Test
    void getTaskWithValidId() {
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void CreateTask() {
        Long oldSize = taskService.getTasksSize();

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);

        Long newSize = taskService.getTasksSize();

        assertThat(newSize - oldSize).isEqualTo(1);
        assertThat(taskService.getTasks()).hasSize(2);
    }

    @Test
    @DisplayName("generateIdTest")
    void CreateTaskWithGenerateId() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);

        assertThat(taskService.getTask(taskService.getTasksSize())
                .getId()).isEqualTo(2L);
    }

    @Test
    void updateTask() {
        Long id = Long.valueOf(taskService.getTasksSize());
        Task task = taskService.getTask(id);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);

        task.setTitle(UPDATE_TITLE);
        taskService.updateTask(id, task);
        task = taskService.getTask(id);

        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    void deleteTask() {
        Long oldSize = taskService.getTasksSize();

        taskService.deleteTask(oldSize);

        Long newSize = taskService.getTasksSize();

        assertThat(oldSize - newSize).isEqualTo(1);
        assertThat(taskService.getTasks()).hasSize(0);
    }
}