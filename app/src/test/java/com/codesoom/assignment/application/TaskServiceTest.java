package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService();

        //fixtures
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).hasSize(1);

        Task task = tasks.get(0);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithValidId() {
        Task found = taskService.getTask(1L);
        assertThat(found.getTitle()).isEqualTo(TASK_TITLE);

    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(()-> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);

        int newSize = taskService.getTasks().size();
        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void updateTask() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    void deleteTask() {
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);

    }
}
