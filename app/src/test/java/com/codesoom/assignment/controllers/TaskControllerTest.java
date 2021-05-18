package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("/tasks")
class TaskControllerTest {
    private final Long validTaskId = 1L;
    private final Long addtionalValidTaskId = 2L;
    private final Long invalidTaskId = 100L;
    private final String validTaskTitle = "Test1";
    private final String additionalValidTaskTitle = "Test2";
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        final TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(validTaskTitle);
        taskController.create(task);
    }

    @Test
    @DisplayName("전체 할 일 목록을 조회한다.")
    void listTasks() {
        assertThat(taskController.list()).hasSize(1);
        assertThat(taskController.detail(validTaskId)
                                 .getTitle()).isEqualTo(validTaskTitle);
    }

    @Test
    @DisplayName("할 일 목록에 등록된 할 일을 조회한다.")
    void getTaskExists() {
        assertThat(taskController.detail(validTaskId)
                                 .getTitle()).isEqualTo(validTaskTitle);
    }

    @Test
    @DisplayName("할 일 목록에 없는 할 일을 조회한다.")
    void taskNotFound() {
        assertThatThrownBy(() -> taskController.detail(invalidTaskId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일을 추가한다.")
    void createNewTask() {
        Task task = new Task();
        task.setTitle(additionalValidTaskTitle);

        taskController.create(task);

        assertThat(taskController.list()).hasSize(2);
        assertThat(taskController.detail(addtionalValidTaskId)
                                 .getTitle()).isEqualTo(additionalValidTaskTitle);
    }

    @Test
    @DisplayName("지정한 할 일을 갱신한다.")
    void updateTask() {
        Task newTask = new Task();
        newTask.setTitle(additionalValidTaskTitle);

        taskController.update(validTaskId, newTask);

        assertThat(taskController.detail(validTaskId)
                                 .getTitle()).isEqualTo(additionalValidTaskTitle);
    }

    @Test
    @DisplayName("지정한 할 일을 삭제한다.")
    void deleteTask() {
        taskController.delete(validTaskId);

        assertThat(taskController.list()).isEmpty();
    }
}
