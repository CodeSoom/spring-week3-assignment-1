package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;
    private Task task1;
    private Task task2;
    final private Long INVALID_ID = 100L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        task1 = new Task(0L, "title1");
        task2 = new Task(1L, "title2");
    }

    @Test
    void getTasks() {
        assertThat(taskService.getTasks()).hasSize(0);

        taskService.createTask(task1);
        assertThat(taskService.getTasks()).hasSize(1);
        assertThat(taskService.getTasks().get(0).getId()).isEqualTo(1L);
        assertThat(taskService.getTasks().get(0).getTitle()).isEqualTo(task1.getTitle());


        taskService.createTask(task2);
        assertThat(taskService.getTasks()).hasSize(2);
        assertThat(taskService.getTasks().get(1).getId()).isEqualTo(2L);
        assertThat(taskService.getTasks().get(1).getTitle()).isEqualTo(task2.getTitle());
    }

    @Test
    void getTaskWithValid() {
        Task newTask = taskService.createTask(task1);
        assertThat(taskService.getTask(1L).getId()).isEqualTo(newTask.getId());
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo(newTask.getTitle());
    }

    @Test
    void getTaskWithInvalid() {
        taskService.createTask(task1);
        assertThatThrownBy(() -> taskService.getTask(INVALID_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() {
        Task newTask1 = taskService.createTask(task1);

        assertThat(newTask1.getId()).isEqualTo(1L);
        assertThat(newTask1.getId()).isNotEqualTo(task1.getId());
        assertThat(newTask1.getTitle()).isEqualTo(task1.getTitle());
        assertThat(taskService.getTasks()).hasSize(1);
    }

    @Test
    void updateTaskWithValidId() {
        Task newTask = taskService.createTask(task1);
        Task updatedTask = taskService.updateTask(newTask.getId(), task2);

        assertThat(updatedTask.getId()).isEqualTo(newTask.getId());
        assertThat(updatedTask.getTitle()).isEqualTo(task2.getTitle());
    }

    @Test
    void updateTaskWithInvalidId() {
        taskService.createTask(task1);

        assertThatThrownBy(() ->  taskService.updateTask(INVALID_ID, task2))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTaskWithValidId() {
        Task newTask = taskService.createTask(task1);
        assertThat(taskService.getTasks()).hasSize(1);

        Task deletedTask = taskService.deleteTask(newTask.getId());
        assertThat(taskService.getTasks()).hasSize(0);
        assertThat(deletedTask.getId()).isEqualTo(newTask.getId());
        assertThat(deletedTask.getTitle()).isEqualTo(newTask.getTitle());
    }

    @Test
    void deleteTaskWithInvalidId() {
        taskService.createTask(task1);

        assertThatThrownBy(() ->  taskService.deleteTask(INVALID_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }
}