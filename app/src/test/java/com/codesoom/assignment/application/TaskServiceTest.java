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

    private static final String TASK_TITLE = "Test1";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    void getList() {
        assertThat(taskService.getTasks()).hasSize(1);
        assertThat(taskService.getTask(1L)).isEqualTo(TASK_TITLE);
    }

    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task source = new Task();
        source.setTitle("Test2");

        taskService.createTask(source);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void updateTaks() {
        String updateTitle = "Test2";
        Task source = new Task();
        source.setTitle(updateTitle);

        Task task = taskService.updateTask(1L, source);

        assertThat(task.getTitle()).isEqualTo(updateTitle);
    }

    @Test
    void deleteTaskSomething() {
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(0);
    }

    @Test
    void deleteTaskNothing() {
        assertThatThrownBy(() -> taskService.deleteTask(404L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}