package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    void createTestTask() {
        Task task = new Task();

        task.setTitle("Test1");
        taskService.createTask(task);

        task.setTitle("Test2");
        taskService.createTask(task);
    }

    @DisplayName("Test to get Task list to use \"getTasks\" on TaskService class")
    @Test
    void getTasks() {
        assertThat(taskService.getTasks()).isEmpty();
    }

    @DisplayName("Test to get specific Task to use \"getTask\" on TaskService class")
    @Test
    void getTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        createTestTask();

        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("Test1");
        assertThat(taskService.getTask(2L).getTitle()).isEqualTo("Test2");

        assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("Test to create Task to use \"createTask\" on TaskService class")
    @Test
    void createTask() {
        Task task = new Task();

        assertThatThrownBy(() -> taskService.getTask(1L)).isInstanceOf(TaskNotFoundException.class);
        assertThatThrownBy(() -> taskService.getTask(2L)).isInstanceOf(TaskNotFoundException.class);

        task.setTitle("Test1");
        taskService.createTask(task);

        task.setTitle("Test2");
        taskService.createTask(task);

        assertThat(taskService.getTasks().get(0).getId()).isEqualTo(1L);
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("Test1");

        assertThat(taskService.getTasks().get(1).getId()).isEqualTo(2L);
        assertThat(taskService.getTask(2L).getTitle()).isEqualTo("Test2");
    }

    @DisplayName("Test to update Task's title to use \"updateTask\" on TaskService class")
    @Test
    void updateTask() {
        Task task = new Task();
        task.setTitle("New Test1");

        assertThatThrownBy(() -> taskService.updateTask(1L, task)).isInstanceOf(TaskNotFoundException.class);

        createTestTask();

        taskService.updateTask(1L, task);

        task.setTitle("New Test2");
        taskService.updateTask(2L, task);

        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("New Test1");
        assertThat(taskService.getTask(2L).getTitle()).isEqualTo("New Test2");
    }

    @DisplayName("Test to delete specific Task to use \"deleteTask\" on TaskService class")
    @Test
    void deleteTask() {
        assertThatThrownBy(() -> taskService.getTask(1L)).isInstanceOf(TaskNotFoundException.class);
        assertThatThrownBy(() -> taskService.getTask(2L)).isInstanceOf(TaskNotFoundException.class);

        createTestTask();

        assertThat(taskService.getTask(1L)).isNotNull();
        assertThat(taskService.getTask(2L)).isNotNull();

        taskService.deleteTask(1L);

        assertThatThrownBy(() -> taskService.getTask(1L)).isInstanceOf(TaskNotFoundException.class);
        assertThat(taskService.getTask(2L)).isNotNull();

        taskService.deleteTask(2L);

        assertThatThrownBy(() -> taskService.getTask(2L)).isInstanceOf(TaskNotFoundException.class);
    }
}
