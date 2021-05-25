package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void SetUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService); // 주입
        Task task = new Task();
        task.setId(1L);
        task.setTitle("TASK 1");
        controller.create(task);
    }

    @Test
    void getTasks() {
        List<Task> tasks = controller.list();
        assertThat(tasks).hasSize(1);
    }

    @Test
    void getTask() {
        List<Task> tasks = controller.list();
        assertThat(tasks.get(0).getTitle()).isEqualTo("TASK 1");
    }

    @Test
    void ValidId() {
        Task task = controller.list().get(0);
        assertThat(task.getTitle()).isEqualTo("TASK 1");
    }

    @Test
    void InvalidId() {
        assertThatThrownBy(() -> controller.detail(2L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask(){
        int oldSize = controller.list().size();
        Task task = new Task();
        task.setTitle("TASK 2");
        controller.create(task);
        int newSize = controller.list().size();
        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void updateTask(){
        Task source = new Task();

        // for update()
        source.setTitle("TASK 2" + "!!");
        controller.update(1L, source);
        Task task = controller.list().get(0);
        assertThat(task.getTitle()).isEqualTo("TASK 2" + "!!");

        // for patch()
        source.setTitle("TASK 2");
        controller.patch(1L, source);
        assertThat(task.getTitle()).isEqualTo("TASK 2");
    }

    @Test
    void deleteTask(){
        int oldSize = controller.list().size();
        controller.delete(1L);
        int newSize = controller.list().size();
        assertThat(oldSize - newSize).isEqualTo(1);
    }
}
