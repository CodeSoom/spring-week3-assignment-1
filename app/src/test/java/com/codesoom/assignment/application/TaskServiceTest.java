package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;

class TaskServiceTest {

    private Task task;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        task = new Task();
        taskService = new TaskService();
    }

    @Test
    void getTasks() {
        taskService.createTask(new Task());

        assertThat(taskService.getTasks()).hasSize(1);

        taskService.createTask(new Task());
        taskService.createTask(new Task());

        assertThat(taskService.getTasks()).hasSize(3);

        taskService.deleteTask(1L);
        taskService.deleteTask(2L);
        taskService.deleteTask(3L);

        assertThat(taskService.getTasks()).hasSize(0);
    }

    @Test
    void getTaskWithValidId() {

        taskService.createTask(new Task("title 1")); // id: 1
        taskService.createTask(new Task("title 2")); // id: 2
        taskService.createTask(new Task("title 3")); // id: 3



    }
    //
    // @Test
    // void getTaskWithInvalidId() {
    //     assertThatThrownBy(() -> taskService.getTask(100L))
    //         .isInstanceOf(TaskNotFoundException.class);
    // }
    //
    // @Test
    // void createTask() {
    //     task.setTitle(TASK_TITLE);
    //     taskService.createTask(task);
    //     assertThat(taskService.getTasks()).hasSize(1);
    // }
    //
    // @Test
    // void deleteTask() {
    //     int oldSize = taskService.getTasks().size();
    //
    //     taskService.deleteTask(1L);
    //
    //     int newSize = taskService.getTasks().size();
    //
    //     assertThat(oldSize - newSize).isEqualTo(1);
    // }
    //
    // @Test
    // void updateTask() {
    //     Task source = new Task();
    //     source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
    //     taskService.updateTask(1L, source);
    //
    //     Task task = taskService.getTask(1L);
    //     assertThat(task.getTitle()).isEqualTo(TASK_TITLE + "!!!");
    // }

}
