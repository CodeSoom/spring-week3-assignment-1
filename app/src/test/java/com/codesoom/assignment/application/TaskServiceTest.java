package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;
    private final static String TASK_TITLE = "Task title";
    private final static String TASK_NOT_FOUND_MESSAGE = "Task not found: %d";

    @BeforeEach
    void setUp() {
        this.taskService = new TaskService();

        // given
        Task resource = new Task();
        resource.setTitle(TASK_TITLE);
        taskService.createTask(resource);
    }

    @Test
    @DisplayName("모든 Task 찾기")
    void getTasks() {
        assertThat(taskService.getTasks()).hasSize(1);
        assertThat(taskService.getTasks()).isInstanceOf(List.class);
    }


    @Test
    @DisplayName("id로 Task 찾기")
    void getTaskWithValidId() {
        // when
        Task task = taskService.getTask(1L);

        // then
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("존재하지 않는 id로 Task 찾기")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);

        assertThatExceptionOfType(TaskNotFoundException.class)
                .isThrownBy(() -> taskService.getTask(100L))
                .withMessage(String.format(TASK_NOT_FOUND_MESSAGE, 100L));
    }

    @Test
    @DisplayName("새로운 Task 저장하기")
    void createTask() {
        // given
        String newTitle = "New " +TASK_TITLE;
        int oldSize = taskService.getTasks().size();
        Task newTask = new Task();
        newTask.setTitle(newTitle);

        // when
        Task task = taskService.createTask(newTask);

        // then
        Task findTask = taskService.getTask(task.getId());
        int newSize = taskService.getTasks().size();

        assertThat(findTask.getTitle()).isEqualTo(newTitle);
        assertThat(newSize).isEqualTo(oldSize + 1);
    }

    @Test
    @DisplayName("Task 수정하기")
    void updateTaskWithValidId() {
        // given
        String updatedTitle = "Updated " +TASK_TITLE;
        Task resource = new Task();
        resource.setTitle(updatedTitle);

        // when
        Task task = taskService.updateTask(1L, resource);

        // then
        assertThat(task.getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    @DisplayName("존재하지 않는 Task 수정하기")
    void updateTaskWithInvalidId() {
        // given
        String updatedTitle = "Updated " +TASK_TITLE;
        Task resource = new Task();
        resource.setTitle(updatedTitle);

        // then
        assertThatThrownBy(() -> taskService.updateTask(100L, resource))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Task 삭제하기")
    void deleteTaskWithValidId() {
        // given
        int oldSize = taskService.getTasks().size();

        // when
        taskService.deleteTask(1L);

        // then
        int newSize = taskService.getTasks().size();

        assertThatThrownBy(() -> taskService.getTask(1L))
                .isInstanceOf(TaskNotFoundException.class);
        assertThat(newSize).isEqualTo(oldSize - 1);
    }

    @Test
    @DisplayName("존재하지 않는 Task 삭제하기")
    void deleteTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.deleteTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}