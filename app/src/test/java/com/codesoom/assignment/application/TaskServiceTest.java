package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private static final String TASK_TITLE = "test";
    private static final String TASK_CREATE_PREFIX = "new";
    private static final String TASK_UPDATE_PREFIX = "fix";
    private static final Long EXIST_ID = 1L;
    private static final Long WRONG_ID = -1L;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        Task task = new Task();

        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    @DisplayName("모든 Task 목록 가져오기")
    void getTaskList() {
        assertThat(taskService.getTasks()).isNotEmpty();
    }

    @Test
    @DisplayName("Valid Id 값으로 Task 가져오기")
    void getTaskWithValidId() {
        assertThat(taskService.getTask(EXIST_ID).getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("Invalid Id 값으로 Task 가져오기")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(WRONG_ID)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("새 Task 만들기")
    void createTask() {
        Task task = new Task();

        task.setTitle(TASK_CREATE_PREFIX + TASK_TITLE);
        Task newTask = taskService.createTask(task);

        assertThat(newTask.getTitle()).isEqualTo(TASK_CREATE_PREFIX + TASK_TITLE);
    }

    @Test
    @DisplayName("Valid Id 값으로 Task 수정하기")
    void updateTaskWithValidId() {
        Task task = taskService.getTask(EXIST_ID);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);

        task.setTitle(TASK_UPDATE_PREFIX + TASK_TITLE);
        Task updateTask = taskService.updateTask(EXIST_ID, task);

        assertThat(updateTask.getTitle()).isEqualTo(TASK_UPDATE_PREFIX + TASK_TITLE);
    }

    @Test
    @DisplayName("Invalid Id 값으로 Task 수정하기")
    void updateTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.updateTask(WRONG_ID, new Task())).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Valid Id 값으로 Task 삭제하기")
    void deleteTaskWithValidId() {
        Task task = taskService.getTask(EXIST_ID);
        Task deleteTask = taskService.deleteTask(EXIST_ID);

        assertThat(deleteTask).isEqualTo(task);
        assertThatThrownBy(() -> taskService.getTask(EXIST_ID)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Invalid Id 값으로 Task 삭제하기")
    void deleteTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.deleteTask(WRONG_ID)).isInstanceOf(TaskNotFoundException.class);
    }
}
