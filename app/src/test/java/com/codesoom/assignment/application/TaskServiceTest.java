package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    private TaskService taskService;
    private static final String TASK_TITLE = "testTitle";

    @BeforeEach
    void setUp() {
        this.taskService = new TaskService();
    }

    @Test
    void getTasks() {
        // then
        assertThat(taskService.getTasks()).isEmpty();
    }

    @Test
    void getTaskWithValidId() {
        // given
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        // when
        Task found = taskService.getTask(1L);

        // then
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithInvalidId() throws Exception {
        // then
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() throws Exception {
        // given
        int oldSize = taskService.getTasks().size();

        Task task1 = new Task();
        task1.setTitle(TASK_TITLE);
        taskService.createTask(task1);

        Task task2 = new Task();
        taskService.createTask(task2);

        // when
        int newSize = taskService.getTasks().size();
        Task foundTask1 = taskService.getTask(1L);
        Task foundTask2 = taskService.getTask(2L);

        // then
        assertThat(newSize - oldSize).isEqualTo(2);

        assertThat(foundTask1.getTitle()).isNotNull();
        assertThat(foundTask1.getId()).isEqualTo(1L);

        assertThat(foundTask2.getTitle()).isNull();
        assertThat(foundTask2.getId()).isEqualTo(2L);
    }

    @Test
    void updateTaskWithValidId() throws Exception {
        // given
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        // when
        task.setTitle("updateTitle");
        Task updatedTask = taskService.updateTask(1L, task);

        // then
        assertThatThrownBy(() -> taskService.updateTask(2L, task))
                .isInstanceOf(TaskNotFoundException.class);
        assertThat(updatedTask.getTitle()).isEqualTo("updateTitle");
    }

    @Test
    void updateTaskWithInvalidId() throws Exception {
        // then
        assertThatThrownBy(() -> taskService.updateTask(1L, new Task()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTaskWithValidId() throws Exception {
        // given
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
        int oldSize = taskService.getTasks().size();

        // when
        taskService.deleteTask(1L);
        int newSize = taskService.getTasks().size();

        // then
        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(TaskNotFoundException.class);
        assertThat(oldSize).isEqualTo(1);
        assertThat(newSize).isEqualTo(0);
    }


    @Test
    void deleteTaskWithInvalidId() throws Exception {
        // then
        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(TaskNotFoundException.class);
    }

}