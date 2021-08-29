package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskServiceTest {
    private final String taskTitle = "title";
    private final String updateTitle = "update";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task(null, taskTitle);

        taskService.createTask(task);
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).hasSize(1);

        Task task = tasks.get(0);
        assertThat(task.getTitle()).isEqualTo(taskTitle);
    }

    @Test
    void getTaskWithValidId() {
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(taskTitle);
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(100L))
        .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task task = new Task(null, taskTitle);

        taskService.createTask(task);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void updateTaskWithExist() {
        Task source = new Task(null, updateTitle);

        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(updateTitle);
    }

    @Test
    void updateTaskWithNotExist() {
        Task source = new Task(null, updateTitle);

        assertThatThrownBy(() -> taskService.updateTask(100L, source))
        .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTaskWithExist() {
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }

    @Test
    void deleteTaskWithNotExist() {
        assertThatThrownBy(() -> taskService.deleteTask(100l))
            .isInstanceOf(TaskNotFoundException.class);
    }
}
