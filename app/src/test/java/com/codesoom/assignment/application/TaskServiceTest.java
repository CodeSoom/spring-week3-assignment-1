package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    private final long INVALID_TASK_ID = 0L;
    private TaskService taskService;
    private Task testTask;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        testTask = taskService.createTask(new Task(1L, "test1"));
    }

    @Test
    void getTasks() {
        // given
        taskService.createTask(new Task(2L, "test2"));

        // when
        List<Task> tasks = taskService.getTasks();

        // then
        assertThat(tasks.size()).isEqualTo(2);
    }

    @Test
    void getTaskWithValidId() {
        // given
        // when
        Long id = testTask.getId();
        Task task = taskService.getTask(id);

        // then
        assertThat(task.getId()).isEqualTo(id);
        assertThat(task.getTitle()).isEqualTo(testTask.getTitle());
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(
                () -> taskService.getTask(INVALID_TASK_ID)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() {
        // given
        Task newTask = new Task(null, "task2");

        // when
        Task createTask = taskService.createTask(newTask);

        // then
        assertThat(createTask.getId()).isEqualTo(2L);
        assertThat(createTask.getTitle()).isEqualTo(newTask.getTitle());
    }

    @Test
    void updateTaskWithValidId() {
        // given
        Long id = testTask.getId();
        Task task = new Task(null, "update Task");

        // when
        Task updateTask = taskService.updateTask(id, task);

        // then
        assertThat(updateTask.getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    void updateTaskWithInvalidId() {
        Task task = new Task(null, "update Task");
        assertThatThrownBy(
                () -> taskService.updateTask(INVALID_TASK_ID, task)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTaskWithValidId() {
        // given
        Long id = testTask.getId();

        // when
        taskService.deleteTask(id);

        // then
        assertThatThrownBy(
                () -> taskService.getTask(id)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTaskWithInvalidId() {
        assertThatThrownBy(
                () -> taskService.deleteTask(INVALID_TASK_ID)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }
}