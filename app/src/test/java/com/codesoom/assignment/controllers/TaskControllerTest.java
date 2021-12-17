package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    private final static String NEW_TITLE = "new task";
    private final static String TITLE_POSTFIX = " spring";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        Task source = new Task();
        source.setTitle(NEW_TITLE);

        taskService.createTask(source);
    }

    @DisplayName("list()는 저장하고 있는 할 일 목록을 반환한다")
    @Test
    void list() {
        List<Task> tasks = controller.list();

        assertThat(tasks).hasSize(1);
    }

    @DisplayName("detail()는")
    @Test
    void detail_ok() {
        Long taskId = 1L;
        Task task = controller.detail(taskId);

        assertThat(task).isNotNull();
    }

    @DisplayName("잘못된 할일을 조회하면 예외가 터진다")
    @Test
    void detail_error() {
        Long taskId = 100L;
        assertThatThrownBy(() -> controller.detail(taskId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("할일을 생성하면 할일 목록이 증가한다")
    @Test
    void create() {
        int oldSize = controller.list().size();

        Task source = new Task();
        controller.create(source);

        int newSize = controller.list().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @DisplayName("할일을 수정하면 데이터가 수정된다")
    @Test
    void update() {
        Long taskId = 1L;
        Task source = new Task();
        source.setTitle(NEW_TITLE + TITLE_POSTFIX);

        controller.update(taskId, source);
        Task task = controller.detail(taskId);

        assertThat(task.getId()).isEqualTo(taskId);
        assertThat(task.getTitle()).isEqualTo(NEW_TITLE + TITLE_POSTFIX);
    }

    @DisplayName("할일을 삭제하면 할일 목록의 크기가 줄어든다")
    @Test
    void delete() {
        int oldSize = controller.list().size();

        Long taskId = 1L;
        controller.delete(taskId);

        int newSize = controller.list().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }
}
