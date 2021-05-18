package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/tasks API")
class TaskControllerTest {
    private final Long taskId1 = 1L;
    private final Long taskId2 = 2L;
    private final String taskTitle1 = "Test1";
    private final String taskTitle2 = "Test2";
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        final TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(taskTitle1);
        taskController.create(task);
    }

    @Test
    @DisplayName("GET 전체 할 일 목록을 조회한다.")
    void listTasks() {
        Task task = new Task();
        task.setId(taskId1);
        task.setTitle(taskTitle1);

        assertThat(taskController.list()).hasSize(1);
        assertThat(taskController.detail(taskId1)
                                 .getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    @DisplayName("POST /{id} 할 일을 추가한다.")
    void createNewTask() {
        Task task = new Task();
        task.setTitle(taskTitle2);
        taskController.create(task);

        assertThat(taskController.list()).hasSize(2);
        assertThat(taskController.detail(taskId2)
                                 .getTitle()).isEqualTo(taskTitle2);
    }

    @Test
    @DisplayName("PUT /{id} 지정한 할 일을 갱신한다.")
    void updateTask() {
        assertThat(taskController.list()).hasSize(1);

        Task newTask = new Task();
        newTask.setTitle(taskTitle2);
        taskController.update(taskId1, newTask);
        assertThat(taskController.detail(taskId1)
                                 .getTitle()).isEqualTo(taskTitle2);
    }

    @Test
    @DisplayName("DELETE /{id} 지정한 할 일을 삭제한다.")
    void deleteTask() {
        taskController.delete(taskId1);
        assertThat(taskController.list()).isEmpty();
    }
}
