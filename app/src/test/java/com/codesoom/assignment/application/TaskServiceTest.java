package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;
    private static final String TASK_TITLE = "test";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    void getTasks() {
        assertThat(taskService.getTasks()).isEmpty();
    }

    @Test
    void getTask() {
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TASK_TITLE);

        assertThatThrownBy(() -> taskService.getTask(1L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}