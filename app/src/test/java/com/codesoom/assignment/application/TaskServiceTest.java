package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssumptions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;

class TaskServiceTest {

    private Task task;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        task = new Task();
        taskService = new TaskService();
    }

    @Test
    void getTasksWithSize() {
        assertThat(taskService.getTasks()).hasSize(0);
        assertThat(taskService.getTasks()).isEqualTo(Arrays.asList());

        taskService.createTask(new Task());

        assertThat(taskService.getTasks()).hasSize(1);

        taskService.createTask(new Task());
        taskService.createTask(new Task());

        assertThat(taskService.getTasks()).hasSize(3);

        taskService.deleteTask(1L);
        taskService.deleteTask(2L);
        taskService.deleteTask(3L);

        assertThat(taskService.getTasks()).hasSize(0);
    }

    @Test
    void getTaskWithValidIdAndTitle() {

        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.getTask(1L))
            .withMessage("Task not found: 1");

        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.getTask(2L))
            .withMessage("Task not found: 2");

        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.getTask(3L))
            .withMessage("Task not found: 3");

        taskService.createTask(new Task("title 1")); // id: 1
        taskService.createTask(new Task("title 2")); // id: 2
        taskService.createTask(new Task("title 3")); // id: 3

        assertThat(taskService.getTask(1L).getId()).isEqualTo(1);
        assertThat(taskService.getTask(2L).getId()).isEqualTo(2);
        assertThat(taskService.getTask(3L).getId()).isEqualTo(3);

        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("title 1");
        assertThat(taskService.getTask(2L).getTitle()).isEqualTo("title 2");
        assertThat(taskService.getTask(3L).getTitle()).isEqualTo("title 3");
    }

    @Test
    void deleteAndUpdateTask() {
        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.getTask(1L))
            .withMessage("Task not found: 1");

        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.getTask(2L))
            .withMessage("Task not found: 2");

        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.getTask(3L))
            .withMessage("Task not found: 3");

        taskService.createTask(new Task("title 1")); // id: 1
        taskService.createTask(new Task("title 2")); // id: 2
        taskService.createTask(new Task("title 3")); // id: 3

        taskService.updateTask(1L, new Task("update 1"));
        taskService.updateTask(2L, new Task("update 2"));
        taskService.updateTask(3L, new Task("update 3"));

        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("update 1");
        assertThat(taskService.getTask(2L).getTitle()).isEqualTo("update 2");
        assertThat(taskService.getTask(3L).getTitle()).isEqualTo("update 3");

        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.updateTask(4L, new Task("update 4")))
            .withMessage("Task not found: 4");
        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.updateTask(5L, new Task("update 5")))
            .withMessage("Task not found: 5");
        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.updateTask(6L, new Task("update 6")))
            .withMessage("Task not found: 6");

        taskService.deleteTask(1L);
        taskService.deleteTask(2L);
        taskService.deleteTask(3L);

        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.deleteTask(1L))
            .withMessage("Task not found: 1");
        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.deleteTask(2L))
            .withMessage("Task not found: 2");
        assertThatExceptionOfType(TaskNotFoundException.class)
            .isThrownBy(() -> taskService.deleteTask(3L))
            .withMessage("Task not found: 3");
    }
}
