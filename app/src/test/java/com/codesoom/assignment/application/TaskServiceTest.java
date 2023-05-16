package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TaskServiceTest {

    private TaskService taskService;
    private static final String TASK_TITLE_ONE = "test1";
    private static final String TASK_TITLE_TWO = "test2";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task1 = new Task();
        task1.setTitle(TASK_TITLE_ONE);
        taskService.createTask(task1);

        Task task2 = new Task();
        task2.setTitle(TASK_TITLE_TWO);
        taskService.createTask(task2);
    }

    @Test
    @DisplayName("Task List 반환 테스트")
    void getTasksTest() {
        // Given - setUp()

        // When
        List<Task> tasks = taskService.getTasks();

        // Then
        assertThat(tasks).hasSize(2);
    }

    @Test
    @DisplayName("Task 반환 성공 테스트")
    void getTaskSuccessTest() {
        // Given - setUp()

        // When
        String taskTitle1 = taskService.getTask(1L).getTitle();
        String taskTitle2 = taskService.getTask(2L).getTitle();

        // Then
        assertThat(taskTitle1).isEqualTo(TASK_TITLE_ONE);
        assertThat(taskTitle2).isEqualTo(TASK_TITLE_TWO);
    }

    @Test
    @DisplayName("Task 반환 실패 테스트")
    void getTaskFailTest() {
        assertThatThrownBy(() -> taskService.getTask(3L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Task 생성 테스트")
    void createTest() {
        // Given
        Task task = new Task();
        task.setTitle("create");
        taskService.createTask(task);

        // When
        String taskTitle = taskService.getTask(3L).getTitle();

        // Then
        assertThat(taskTitle).isEqualTo("create");
    }

    @Test
    @DisplayName("Task Update 성공 테스트")
    void updateTaskSuccessTest() {
        // Given
        Task task = new Task();
        task.setTitle("update");
        taskService.updateTask(1L, task);

        // When
        String taskTitle = taskService.getTask(1L).getTitle();

        // Then
        assertThat(taskTitle).isEqualTo("update");
    }

    @Test
    @DisplayName("Task Update 실패 테스트")
    void updateTaskFailTest() {
        assertThatThrownBy(() -> taskService.updateTask(3L, new Task()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Task Delete 성공 테스트")
    void deleteTaskSuccessTest() {
        // Given - setUp()

        // When
        String taskTitle = taskService.deleteTask(1L).getTitle();

        // Then
        assertThat(taskTitle).isEqualTo(TASK_TITLE_ONE);
    }

    @Test
    @DisplayName("Task Delete 실패 테스트")
    void deleteTaskFailTest() {
        assertThatThrownBy(() -> taskService.deleteTask(3L))
                .isInstanceOf(TaskNotFoundException.class);
    }

}
