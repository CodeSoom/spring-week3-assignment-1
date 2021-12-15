package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    private static final String TASK_TITLE = "test";
    private static final String NEW_TITLE = "new test";
    private static final String UPDATE_POSTFIX = "New";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        controller.create(task);
    }

    @Test
    @DisplayName("list 메소드는 Task 목록을 리턴한다.")
    void list() {
        assertThat(controller.list()).isNotEmpty();
    }

    @Test
    @DisplayName("detail 메소드는 id에 해당하는 Task를 리턴한다.")
    void detailWithValidId() {
        assertThat(controller.detail(1L).getId()).isEqualTo(1L);
        assertThat(controller.detail(1L).getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("detail 메소드는 id가 없다면 예외를 던진다.")
    void detailWithInvalidId() {
        assertThatThrownBy(() -> controller.detail(0L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("create 메소드는 새로운 task를 생성하고 task를 리턴한다.")
    void create() {
        int originSize = controller.list().size();

        Task task = new Task();
        task.setTitle(NEW_TITLE);

        controller.create(task);

        int newSize = controller.list().size();

        assertThat(newSize - originSize).isEqualTo(1);
    }

    @Test
    @DisplayName("update 메소드는 기존 id의 task의 title을 수정하고 task를 리턴한다.")
    void updateWithValidId() {
        Task source = new Task();
        source.setTitle(UPDATE_POSTFIX + TASK_TITLE);
        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(UPDATE_POSTFIX + TASK_TITLE);
    }

    @Test
    @DisplayName("patch 메소드는 기존 id의 task의 title을 수정하고 task를 리턴한다.")
    void patchWithValidId() {
        Task source = new Task();
        source.setTitle(UPDATE_POSTFIX + NEW_TITLE);
        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(UPDATE_POSTFIX + NEW_TITLE);
    }

    @Test
    @DisplayName("delete 메소드는 해당 id의 task를 삭제한다. ")
    void delete() {
        int originSize = controller.list().size();

        controller.delete(1L);

        int newSize = controller.list().size();

        assertThat(newSize - originSize).isEqualTo(0);
    }
}
