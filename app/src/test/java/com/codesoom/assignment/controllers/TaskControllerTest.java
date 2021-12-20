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

    @DisplayName("list 메소드는 저장하고 있는 할 일 목록을 반환한다")
    @Test
    void list() {
        List<Task> tasks = controller.list();

        assertThat(tasks).hasSize(1);
    }

    @DisplayName("detail 메소드는 주어진 아이디의 할 일을 반환한다")
    @Test
    void detail_ok() {
        Long taskId = 1L;
        Task task = controller.detail(taskId);

        assertThat(task.getId()).isEqualTo(taskId);
    }

    @DisplayName("detail 메소드는 찾을 수 없는 아이디의 할 일이면 예외를 던진다")
    @Test
    void detail_error() {
        Long taskId = 100L;

        assertThatThrownBy(() -> controller.detail(taskId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("create 메소드는 할 일을 생성하고 할 일 목록에 추가한다")
    @Test
    void create() {
        int oldSize = controller.list().size();

        Task source = new Task();
        controller.create(source);

        int newSize = controller.list().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @DisplayName("update 메소드는 주어진 아이디의 할 일을 수정한다")
    @Test
    void update() {
        Task source = new Task();
        source.setTitle(NEW_TITLE + TITLE_POSTFIX);

        Long taskId = 1L;
        controller.update(taskId, source);
        Task task = controller.detail(taskId);

        assertThat(task.getTitle()).isEqualTo(NEW_TITLE + TITLE_POSTFIX);
    }

    @DisplayName("delete 메소드는 주어진 아이디의 할 일을 삭제한다")
    @Test
    void delete() {
        int oldSize = controller.list().size();

        Long taskId = 1L;
        controller.delete(taskId);

        int newSize = controller.list().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }
}
