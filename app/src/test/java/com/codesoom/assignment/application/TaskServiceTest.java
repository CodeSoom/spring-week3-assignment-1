package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void createTask() {
        int originalSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle("test");

        taskService.createTask(task);

        int afterCreationSize = taskService.getTasks().size();

        assertThat(afterCreationSize - originalSize).isEqualTo(1);
    }

    @Test
    void getTasks() {
        assertThat(taskService.getTasks()).isEmpty();
    }

    @Test
    void getTaskWithValidId() {
        Task task = new Task();
        task.setTitle("test");

        taskService.createTask(task);

        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("test");
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(1L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTask() {
        Task task = new Task();
        task.setTitle("test");

        taskService.createTask(task);
        int originalSize = taskService.getTasks().size();

        taskService.deleteTask(1L);
        int afterDeletionSize = taskService.getTasks().size();

        assertThat(originalSize - afterDeletionSize).isEqualTo(1);
    }

    @Test
    void updateTask() {
        Task task = new Task();

        task.setTitle("original title");
        taskService.createTask(task);

        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("original title");

        task.setTitle("updated title");
        taskService.updateTask(1L, task);

        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("updated title");
    }
}
