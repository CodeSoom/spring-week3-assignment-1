package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    //getTasks

    //getTask(with id)

    //createTask(with source)

    //updateTask(with id, source)

    //deleteTask(with id)

    private TaskService taskService;
    private static final String TASK_TITLE = "test";

    @BeforeEach
    void setup() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    void getTasks() {
        assertThat(taskService.getTasks()).hasSize(1);
        assertThat(taskService.getTasks()).isNotNull();
    }

    @Test
    void getTaskWithValidId() {
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }


    @Test
    void createTask() {

        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);

        //assertThat(taskService.getTasks()).hasSize(2);
    }

    @Test
    void updateTask() {
        Task source = new Task();
        source.setTitle("New Title");
        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo("New Title");
    }

}
