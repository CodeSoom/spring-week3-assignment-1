package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private static TaskService taskService;
    private static final String TASK_TITLE = "title";
    private static final String UPDATE_POSTFIX = "-changed";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    @DisplayName("Get all task list")
    void getTasks() {

        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(1);
    }

    @Test
    @DisplayName("Create a task with title")
    void createTask() {

        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    @DisplayName("Get task by valid id")
    void getTask() {

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("Get task by invalid id")
    void getTaskByInvalidId() {

        assertThatThrownBy(() -> taskService.getTask(0L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Update task title with given id")
    void updateTask() {

        Task task = new Task();
        task.setTitle(TASK_TITLE + UPDATE_POSTFIX);

        Task updatedTask = taskService.updateTask(1L, task);
        assertThat(updatedTask.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    @DisplayName("Delete task by id")
    void deleteTask() {

        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);
        int newSize = taskService.getTasks().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }
}
