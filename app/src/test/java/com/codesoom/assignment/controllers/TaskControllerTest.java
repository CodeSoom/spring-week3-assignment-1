package com.codesoom.assignment.controllers;

import com.codesoom.assignment.BaseTaskTest;
import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest extends BaseTaskTest {

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Test
    @DisplayName("할일이 없을 때 목록 조회")
    void readEmptyTasks() {
        List<Task> tasks = taskController.list();

        assertThat(tasks).hasSize(0);
    }

    @Test
    @DisplayName("할일이 있을 때 할일 목록 조회")
    void readTasks() {
        taskController.create(generateNewTask(TASK_TITLE_1));

        List<Task> tasks = taskController.list();
        assertThat(tasks).hasSize(1);
    }

    @Test
    @DisplayName("신규 할일 생성")
    void createTask() {
        Task created = taskController.create(generateNewTask(TASK_TITLE_1));

        assertThat(created.getId()).isEqualTo(TASK_ID_1);
        assertThat(created.getTitle()).isEqualTo(TASK_TITLE_1);
    }

    @Test
    @DisplayName("존재하는 할일 조회")
    void readTask() {
        Task created = taskController.create(generateNewTask(TASK_TITLE_1));
        Task found = taskController.detail(1L);

        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(created);
    }

    @Test
    @DisplayName("존재하지 않은 할일 조회")
    void readNotExistTask() {
        assertThatThrownBy(() -> taskController.detail(1L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
    }

    @Test
    @DisplayName("존재하는 할일의 제목을 수정 > put")
    void editExistTaskTitle() {
        taskController.create(generateNewTask(TASK_TITLE_1));
        Task updatedTask = taskController.update(TASK_ID_1, generateNewTask(TASK_TITLE_2));

        assertThat(updatedTask.getTitle()).isNotEqualTo(TASK_TITLE_1);
        assertThat(updatedTask.getTitle()).isEqualTo(TASK_TITLE_2);
    }

    @Test
    @DisplayName("존재하지 않는 할일의 제목을 수정 > put")
    void editNotExistTaskTitle() {
        assertThatThrownBy(() -> {
            taskController.update(TASK_ID_1, generateNewTask(TASK_TITLE_2));
        }).isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
    }

    @Test
    @DisplayName("존재하는 할일의 제목을 수정 > patch")
    void patchExistTaskTitle() {
        taskController.create(generateNewTask(TASK_TITLE_1));
        Task updatedTask = taskController.patch(TASK_ID_1, generateNewTask(TASK_TITLE_2));

        assertThat(updatedTask.getTitle()).isNotEqualTo(TASK_TITLE_1);
        assertThat(updatedTask.getTitle()).isEqualTo(TASK_TITLE_2);
    }

    @Test
    @DisplayName("존재하지 않는 할일의 제목을 수정 > patch")
    void patchNotExistTaskTitle() {
        assertThatThrownBy(() -> {
            taskController.patch(TASK_ID_1, generateNewTask(TASK_TITLE_2));
        }).isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
    }

    @Test
    @DisplayName("존재하는 할일을 삭제")
    void deleteExistTask() {
        Task created = taskController.create(generateNewTask(TASK_TITLE_1));
        taskController.delete(created.getId());

        List<Task> tasks = taskController.list();

        assertThat(tasks).hasSize(0);
    }

    @Test
    @DisplayName("존재하지 않는 할일을 삭제")
    void deleteNotExistTask() {
        assertThatThrownBy(() -> {
            taskController.delete(TASK_ID_1);
        }).isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);

    }
}
