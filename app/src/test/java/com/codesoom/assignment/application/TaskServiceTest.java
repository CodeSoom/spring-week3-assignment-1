package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    private final static String NEW_TITLE = "new task";
    private final static String TITLE_POSTFIX = " spring";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task source = new Task();
        source.setId(1L);
        source.setTitle(NEW_TITLE);
        Task task = taskService.createTask(source);
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(1);
    }

    @Test
    void getTask() {
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() {
        Task source = new Task();
        source.setId(2L);
        source.setTitle(NEW_TITLE + TITLE_POSTFIX);
        Task task = taskService.createTask(source);

        assertThat(task.getId()).isEqualTo(2L);
        assertThat(task.getTitle()).isEqualTo(NEW_TITLE + TITLE_POSTFIX);
    }

    @Test
    void updateTask() {
        Task source = new Task();
        source.setTitle(NEW_TITLE + TITLE_POSTFIX);
        Task task = taskService.updateTask(1L, source);

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(NEW_TITLE + TITLE_POSTFIX);

    }

    @Test
    void deleteTask() {
        Task task = taskService.deleteTask(1L);
        List<Task> tasks = taskService.getTasks();

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(tasks).hasSize(0);
    }
}