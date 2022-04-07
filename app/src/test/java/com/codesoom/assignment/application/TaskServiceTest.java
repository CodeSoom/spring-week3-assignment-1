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
    @DisplayName("getTasks는 모든 할 일 목록을 반환한다.")
    void getTasks() {
        assertThat(taskService.getTasks()).hasSize(1);
        assertThat(taskService.getTasks()).isInstanceOf(List.class);
    }


    @Test
    @DisplayName("getTask는 아이디가 할 일 목록에 존재한다면 할 일을 찾아서 반환한다.")
    void getTaskWithValidId() {
        // when
        Task task = taskService.getTask(1L);

        // then
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("getTask는 존재하지 않는 할 일을 찾을 때 예외를 발생한다.")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);

        assertThatExceptionOfType(TaskNotFoundException.class)
                .isThrownBy(() -> taskService.getTask(100L))
                .withMessage(String.format(TASK_NOT_FOUND_MESSAGE, 100L));
    }

    @Test
    @DisplayName("createTask는 새로운 할 일을 저장하여 반환한다.")
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
    @DisplayName("updateTask는 아이디가 할 일 목록에 존재한다면 할 일을 수정하여 반환한다.")
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
    @DisplayName("updateTask는 존재하지 않는 할 일을 수정할 때 예외를 발생한다.")
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
    @DisplayName("deleteTask는 아이디가 할 일 목록에 존재한다면 할 일을 삭제한다.")
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
    @DisplayName("deleteTask는 존재하지 않는 할 일을 삭제할 때 예외를 발생한다.")
    void deleteTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.deleteTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}