package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private TaskService taskService;

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService();

        // fixtures
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Test
    @DisplayName("Task 리스트 가져오기")
    void getTasks() {
        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(1);

        Task found = tasks.get(0);
        assertThat(found.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("존재하는 Task ID로 Task 정보 가져오기")
    void getTaskWithValidId() {
        Task found = taskService.getTask(1L);
        assertThat(found.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("존재하지 않는 Task ID로 Task 정보 가져오기")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(10L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Task 생성하기")
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하는 Task ID로 Task 수정하기")
    void updateTaskWithValidId() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);

        taskService.updateTask(1L, source);

        Task found = taskService.getTask(1L);
        assertThat(found.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    @DisplayName("존재하지 않는 Task ID로 Task 수정하기")
    void updateTaskWithInvalidId() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);

        assertThatThrownBy(() -> taskService.updateTask(10L, source)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("존재하는 Task ID로 Task 삭제하기")
    void deleteTaskWithValidId() {
        int oldSize = taskService.getTasks().size();

       taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 Task ID로 Task 삭제하기")
    void deleteTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.deleteTask(10L)).isInstanceOf(TaskNotFoundException.class);
    }
}
