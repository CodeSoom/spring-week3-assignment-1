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
        // given

        // when

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

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        // when
        int newSize = taskService.getTasks().size();

        // then
        assertThat(newSize - oldSize).isEqualTo(1);
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
        assertThat(updatedTask.getTitle()).isEqualTo("updateTitle");
    }

    @Test
    void updateTaskWithInvalidId() throws Exception {
        // then
        assertThatThrownBy(() -> taskService.updateTask(1L, new Task()))
                .isInstanceOf(TaskNotFoundException.class);
    }



    @Test
    void deleteTaskWithInvalidId() throws Exception {
        // then
        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(TaskNotFoundException.class);
    }

}