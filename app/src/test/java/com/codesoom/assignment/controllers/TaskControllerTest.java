package com.codesoom.assignment.controllers;

import com.codesoom.assignment.appllication.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskController controller;

    private final String TEST_TITLE = "TEST_TITLE";
    private final String UPDATE_TITLE = "UPDATE_TITLE";

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        controller = new TaskController(taskService);

        Task task = new Task();
        task.updateTitle(TEST_TITLE);
        controller.create(task);
    }

    @Test
    @DisplayName("tasks.size()")
    void getList() {
        assertThat(controller.getTaskList()).hasSize(1);
    }

    @Test
    @DisplayName("특정 task객체의 title가져오기")
    void detailTask() {
        assertThat(controller.getTaskById(1L).getTitle()).isEqualTo(TEST_TITLE);
    }

    @Test
    @DisplayName("tasks에 task추가")
    void createNewTask() {
        Task task = new Task();
        task.updateTitle("Test");
        controller.create(task);

        assertThat(controller.getTaskList()).hasSize(2);
        assertThat(controller.getTaskList().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("task의 title수정")
    void updateTask() {
        Task source = new Task();
        source.updateTitle(UPDATE_TITLE);

        assertThat(controller.patch(1L, source).getTitle()).isEqualTo(UPDATE_TITLE);
        assertThat(controller.put(1L, source).getTitle()).isEqualTo(UPDATE_TITLE);

    }

    @Test
    @DisplayName("tasks에서 task삭제")
    void deleteTask() {
        controller.delete(1L);
        assertThat(controller.getTaskList()).hasSize(0);
    }

}

