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
    private TaskController taskController;
    private Task testTask;
    private final Long INVALID_TASK_ID = 0L;

    @BeforeEach
    void setUp() {
        taskController = new TaskController(new TaskService());
        testTask = taskController.create(new Task(1L, "test1"));
    }

    @Test
    @DisplayName("task리스트를 요청한 경우 task를 모두 반환한다")
    void list() {
        // given
        taskController.create(new Task(2L, "test2"));

        // when
        List<Task> tasks = taskController.list();

        // then
        assertThat(tasks.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("유효한 task의 id로 task를 조회한 경우 task의 데이터를 반환한다")
    void detailWithValidId() {
        // given
        Long id = testTask.getId();

        // when
        Task task = taskController.detail(id);

        // then
        assertThat(task.getId()).isEqualTo(id);
        assertThat(task.getTitle()).isEqualTo(testTask.getTitle());
    }

    @Test
    @DisplayName("유효하지 않은 task id로 task를 조회한 경우 TaskNotFoundException 예외를 던진다")
    void detailWithInvalidId() {
        assertThatThrownBy(
                () -> taskController.detail(INVALID_TASK_ID)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("새로운 task를 생성하려는 경우 생성 후 task의 데이터를 반환한다")
    void create() {
        // given
        Task newTask = new Task(null, "task2");

        // when
        Task createTask = taskController.create(newTask);

        // then
        assertThat(createTask.getId()).isEqualTo(2L);
        assertThat(createTask.getTitle()).isEqualTo(newTask.getTitle());
    }

    @Test
    @DisplayName("유효한 task id로 task의 수정을 요청한 경우 task의 데이터를 수정 후 반환한다")
    void updateWithValidId() {
        // given
        Long id = testTask.getId();
        Task task = new Task(null, "update Task");

        // when
        Task updateTask = taskController.update(id, task);

        // then
        assertThat(updateTask.getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    @DisplayName("유효하지 않은 task id로 task의 수정을 요청한 경우 TaskNotFoundException 예외를 던진다")
    void updateWithInvalidId() {
        Task task = new Task(null, "update Task");
        assertThatThrownBy(
                () -> taskController.update(INVALID_TASK_ID, task)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("유효한 task id로 task의 부분수정을 요청한 경우 task의 데이터를 수정 후 반환한다")
    void patchWithValidId() {
        // given
        Long id = testTask.getId();
        Task task = new Task(null, "patch Task");

        // when
        Task updateTask = taskController.patch(id, task);

        // then
        assertThat(updateTask.getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    @DisplayName("유효하지 않은 task id로 task의 부분수정을 요청한 경우 TaskNotFoundException 예외를 던진다")
    void patchWithInvalidId() {
        Task task = new Task(null, "patch Task");
        assertThatThrownBy(
                () -> taskController.patch(INVALID_TASK_ID, task)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("유효한 task id로 task의 삭제를 요청한 경우 task의 데이터를 삭제 후 빈값을 반환한다")
    void deleteTaskWithValidId() {
        // given
        Long id = testTask.getId();

        // when
        taskController.delete(id);

        // then
        assertThatThrownBy(
                () -> taskController.detail(id)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("유효하지 않은 task id로 task의 삭제를 요청한 경우 TaskNotFoundException 예외를 던진다")
    void deleteTaskWithInvalidId() {
        assertThatThrownBy(
                () -> taskController.delete(INVALID_TASK_ID)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }
}