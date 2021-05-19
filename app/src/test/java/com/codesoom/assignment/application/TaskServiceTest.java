package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TaskServiceTest {
    private static final Long VALID_TASK_ID = 1L;
    private static final Long INVALID_TASK_ID = 100L;
    private static final String VALID_TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService();

        // fixture
        Task task = new Task();
        task.setTitle(VALID_TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    @DisplayName("전체 할 일 목록을 조회한다.")
    void getTasks() {
        List<Task> tasks = taskService.getTasks();
        assertAll(
                () -> assertThat(tasks).hasSize(1),
                () -> assertThat(tasks.get(0)
                                      .getTitle()).isEqualTo(VALID_TASK_TITLE));
    }

    @Test
    @DisplayName("할 일 목록에 등록된 할 일을 조회한다.")
    void getTaskWithValidId() {
        Task task = taskService.getTask(VALID_TASK_ID);
        assertThat(task.getTitle())
                .isEqualTo(VALID_TASK_TITLE);
    }

    @Test
    @DisplayName("할 일 목록에 없는 할 일을 조회한다.")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(INVALID_TASK_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("새로운 할 일을 등록한다.")
    void createTask() {
        int oldSize = taskService.getTasks()
                                 .size();

        Task task = new Task();
        task.setTitle(VALID_TASK_TITLE);

        taskService.createTask(task);

        int newSize = taskService.getTasks()
                                 .size();

        assertThat(newSize - oldSize)
                .isEqualTo(1);
    }

    @Test
    @DisplayName("지정한 할 일을 갱신한다.")
    void updateTask() {
        Task source = new Task();
        source.setTitle(VALID_TASK_TITLE + UPDATE_POSTFIX);

        taskService.updateTask(VALID_TASK_ID, source);

        Task task = taskService.getTask(VALID_TASK_ID);

        assertThat(task.getTitle())
                .isEqualTo(VALID_TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    @DisplayName("지정한 할 일을 삭제한다.")
    void deleteTask() {
        int oldSize = taskService.getTasks()
                                 .size();

        taskService.deleteTask(VALID_TASK_ID);

        int newSize = taskService.getTasks()
                                 .size();

        assertThat(oldSize - newSize)
                .isEqualTo(1);
    }

}
