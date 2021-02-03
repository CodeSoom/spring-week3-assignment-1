package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskControllerTest {

    final Long ID = 1L;
    final String TITLE = "old title";
    TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    private Task createTask() {
        Task task = new Task();
        task.setTitle(TITLE);
        Task createdTask = taskController.create(task);
        return createdTask;
    }

    @Test
    void list() {
        assertThat(taskController.list()).hasSize(0);
    }

    @Test
    @DisplayName("detail() - Id 존재")
    void detailExistId() {
        createTask();

        Task detail = taskController.detail(ID);

        assertThat(detail.getId()).isEqualTo(ID);
        assertThat(detail.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("detail() - Id 존재하지 않으면 TaskNotFoundException을 던짐")
    void detailDoesNotExistId() {
        assertThrows(TaskNotFoundException.class, () -> taskController.detail(ID));
    }

    @Test
    @DisplayName("create() - 요청에 title이 존재")
    void create() {
        Task task = new Task();
        task.setTitle(TITLE);
        taskController.create(task);

        assertEquals(taskService.getTasks().size(), 1);
    }

    @Test
    @DisplayName("update() - 바꿀 task와 존재하는 id update요청")
    void update() {
        createTask();

        Task updateTask = new Task();
        updateTask.setTitle("new title");
        Task updatedTask = taskController.update(ID, updateTask);

        assertThat(updatedTask.getTitle()).isEqualTo(taskController.detail(ID).getTitle());
    }

    @Test
    @DisplayName("update() - 존재하지 않는 id요청으로 TaskNotFoundException을 던짐")
    void updateDoesNotExistId() {
        Task updateTask = new Task();
        updateTask.setTitle("new title");

        assertThrows(TaskNotFoundException.class, () -> taskController.update(11L, updateTask));
    }

    @Test
    @DisplayName("patch() - 바꿀 task와 존재하는 id update요청")
    void patch() {
        createTask();

        Task updateTask = new Task();
        updateTask.setTitle("new title");
        Task patchedTask = taskController.patch(ID, updateTask);

        assertThat(patchedTask.getTitle()).isEqualTo(taskController.detail(ID).getTitle());
    }

    @Test
    @DisplayName("patch() - 존재하지 않는 id로 호출, TaskNotFoundException을 던짐")
    void patchDoesNotExistId() {
        Task updateTask = new Task();
        updateTask.setTitle("new title");

        assertThrows(TaskNotFoundException.class, () -> taskController.update(11L, updateTask));
    }

    @Test
    @DisplayName("delete() - 존재하는 id로 호출 성공")
    void delete() {
        createTask();
        assertEquals(taskController.list().size(), 1);

        taskService.deleteTask(1L);

        assertEquals(taskController.list().size(), 0);
    }

    @Test
    @DisplayName("delete() - 존재하지 않는 id로 호출, TaskNotFoundException을 던짐")
    void deleteDoesNotExistId() {
        assertThrows(TaskNotFoundException.class, () -> taskController.detail(1L));
    }
}
