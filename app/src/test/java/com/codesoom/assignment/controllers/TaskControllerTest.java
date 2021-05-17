package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Test
    void getEmptyTaskList() {
        List<Task> tasks = taskController.list();
        assertThat(tasks).isEmpty();
    }

    @Test
    void getTaskListWithTwoTask() {
        Task task1 = new Task();

        task1.setTitle("task1");
        taskService.createTask(task1);

        Task task2 = new Task();
        task2.setTitle("task2");
        taskService.createTask(task2);

        List<Task> tasks = taskController.list();
        assertThat(tasks)
                .hasSize(2)
                .contains(task1, task2);
    }

    @Test
    void getTaskById() {
        Task source = new Task();

        source.setTitle("task1");
        taskService.createTask(source);

        Task task = taskController.detail(1L);
        assertThat(task)
                .isEqualTo(source);
    }

    @Test
    void getTaskWithInvalidId() {
        Task source = new Task();

        source.setTitle("task1");
        taskService.createTask(source);

        long non_existent_id = 42L;
        Throwable thrown = catchThrowable(() -> { taskController.detail(non_existent_id); });
        assertThat(thrown)
                .hasMessageContaining("Task not found");
    }


    @Test
    void create() {
        Task source = new Task();

        source.setTitle("task1");

        Task task = taskController.create(source);
        assertThat(task)
                .isEqualTo(source);
    }

    @Test
    void update() {
        Task source = new Task();

        source.setTitle("task1");
        taskService.createTask(source);

        source.setTitle("task2");

        long created_task_id = 1L;
        Task task = taskController.update(created_task_id, source);
        assertThat(task)
                .isEqualTo(source);
    }

    @Test
    void patch() {
        Task source = new Task();

        source.setTitle("task1");
        taskService.createTask(source);

        source.setTitle("task2");

        long created_task_id = 1L;
        Task test = taskController.patch(created_task_id, source);
        assertThat(test)
                .isEqualTo(source);
    }

    @Test
    void delete() {
        Task source1 = new Task();

        source1.setTitle("task1");
        taskService.createTask(source1);

        Task source2 = new Task();
        source2.setTitle("task2");
        taskService.createTask(source2);

        long created_task_id = 1L;
        taskController.delete(created_task_id);

        assertThat(taskController.list())
                .hasSize(1)
                .doesNotContain(source1);
    }
}