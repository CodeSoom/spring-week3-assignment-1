package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaskControllerTest {

    private static final String TASK_TITLE = "task";
    private static final String NEW_TASK_TITLE = "new";

    private TaskController controller;

    @BeforeEach
    void setUp() {
        // subject
        controller = new TaskController(new TaskService());

        // fixtures
        controller.create(new Task(1L, TASK_TITLE));
    }

    @Test
    @DisplayName("할 일 목록을 가져온다")
    void getTasks() {
        List<Task> list = controller.list();

        assertThat(list).hasSize(1);
    }

    @Test
    @DisplayName("할 일을 생성한다")
    void createNewTask() {
        controller.create(new Task());

        List<Task> tasks = controller.list();

        assertThat(tasks).hasSize(2);
    }

    @Test
    @DisplayName("단일 조회 시 존재하지 않는 할 일 이라면 예외를 던진다")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> controller.detail(2L))
            .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("단일 조회 시 존재하는 할 일 이라면 가져온다")
    void getTaskWithValidId() {
        Task task = controller.detail(1L);

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("수정 시 존재하지 않는 할 일 이라면 예외를 던진다")
    void updateTaskWithInvalidId() {
        assertThatThrownBy(() -> controller.update(2L, new Task()))
            .isInstanceOf(TaskNotFoundException.class);
    }


    @Test
    @DisplayName("수정 시 존재하는 할 일 이라면 수정한다")
    void updateTaskWithValidId() {
        Task newTask = new Task();
        newTask.setTitle(NEW_TASK_TITLE);

        Task updatedTask = controller.update(1L, newTask);

        assertThat(updatedTask.getTitle()).isEqualTo(newTask.getTitle());
    }

    @Test
    @DisplayName("삭제 시 존재하지 않는 할 일 이라면 예외를 던진다")
    void deleteTaskWithInvalidId() {
        assertThatThrownBy(() -> controller.delete(2L))
            .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("삭제 시 존재하는 할 일 이라면 삭제한다")
    void deleteTask() {
        controller.delete(1L);

        int size = controller.list().size();

        assertThat(size).isZero();
    }
}
