package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    private TaskService taskService;
    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    @DisplayName("TaskService 클래스의  getTasks 메소드는 List<task>를 반환한다")
    void getTasks() {
        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(1);

        Task task = taskService.getTasks().get(0);
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("TaskService 클래스의 getTask 메소드는 id가 있다면 해당 Task를 반환한다")
    void getTaskWithValidId() {
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("TaskService 클래스의 getTask 메소드는 id가 없다면 NOT_FOUND를 반환한다")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("TaskService 클래스의 createTask 메소드는 title을 입력받아 Task를 생성한다")
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    @DisplayName("TaskService 클래스의 updateTask 메소드는 id가 있다면 해당 Task를 수정한다")
    void updateTaskWithValidId() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    @DisplayName("TaskService 클래스의 updateTask 메소드는 id가 없다면 NOT_FOUND를 반환한다")
    void updateTaskWithInvalidId() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);

        assertThatThrownBy(() -> taskService.updateTask(100L, source))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("TaskService 클래스의 deleteTask 메소드는 id가 있다면 해당 Task를 삭제한다")
    void deleteTaskWithValidId() {
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }

    @Test
    @DisplayName("TaskService 클래스의 deleteTask 메소드는 id가 있다면 NOT_FOUND를 반환한다")
    void deleteTaskWihInvalidId() {
        assertThatThrownBy(() -> taskService.deleteTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
