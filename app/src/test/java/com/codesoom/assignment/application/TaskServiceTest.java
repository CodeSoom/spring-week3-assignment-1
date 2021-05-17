package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private static final String TASK_TITLE = "test";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void getTasks() {
        assertThat(taskService.getTasks()).isEmpty();
    }

    @Test
    void getTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("test");

        assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }
}