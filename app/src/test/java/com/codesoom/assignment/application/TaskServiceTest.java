package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class TaskServiceTest {
    private TaskService service;

    @BeforeEach
    void setup() {
        service = new TaskService();
    }

    @DisplayName("task를 생성하지 않았을 때, getTask() 요청하면, 빈 배열 반환")
    @Test
    void givenDidNotCreateTask_whenGetTasks_thenReturnEmptyList() {
        // when
        final List<Task> tasks = service.getTasks();

        // then
        assertThat(tasks).isEmpty();
    }

    @DisplayName("task를 생성했을 때, getTasks() 요청하면, 생성했던 task들 반환")
    @Test
    void givenDidCreateTasks_whenGetTasks_thenReturnTasks() {
        // given
        final Task task1 = new Task();
        task1.setTitle("title1");
        final Task task2 = new Task();
        task2.setTitle("title2");
        service.createTask(task1);
        service.createTask(task2);

        // when
        final List<Task> tasks = service.getTasks();

        // then
        assertThat(tasks).hasSize(2);
        final Task resultTask1 = tasks.get(0);
        final Task resultTask2 = tasks.get(1);
        assertThat(resultTask1.getId()).isEqualTo(1L);
        assertThat(resultTask1.getTitle()).isEqualTo("title1");
        assertThat(resultTask2.getId()).isEqualTo(2L);
        assertThat(resultTask2.getTitle()).isEqualTo("title2");
    }

    @DisplayName("task를 생성했을 때, getTask() 요청하면, 생성한 task 반환")
    @Test
    void givenDidCreateTasks_whenGetTask_thenReturnTask() {
        // given
        final Task task = new Task();
        task.setTitle("title");
        service.createTask(task);

        // when
        final Task result = service.getTask(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("title");
    }

    @DisplayName("없는 task id로 getTask() 요청하면, TaskNotFoundException 예외 발생")
    @Test
    void whenGetTaskWithNotExistedId_thenThrowTaskNotFoundException() {
        // when
        Throwable thrown = catchThrowable(() -> { service.getTask(1L); });

        // then
        assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("없는 task id로 updateTask() 요청하면, TaskNotFoundException 예외 발생")
    @Test
    void whenUpdateTaskWithNotExistedId_thenThrowTaskNotFoundException() {
        // when
        Throwable thrown = catchThrowable(() -> { service.updateTask(1L, new Task()); });

        // then
        assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("존재하는 task id로 updateTask() 요청하면, task 업데이트 됨")
    @Test
    void givenCreateTask_whenUpdateTaskWithExistedId_thenTaskUpdated() {
        // given
        Task task = new Task();
        task.setTitle("title");
        service.createTask(task);

        // when
        service.updateTask(1L, task);

        // then
        Task updatedTask = service.getTask(1L);
        assertThat(updatedTask.getId()).isEqualTo(1L);
        assertThat(updatedTask.getTitle()).isEqualTo("title");
    }

    @DisplayName("없는 task id로 deleteTask() 요청하면, TaskNotFoundException 예외 발생")
    @Test
    void whenDeleteTaskWithNotExistedId_thenThrowTaskNotFoundException() {
        // when
        Throwable thrown = catchThrowable(() -> { service.deleteTask(1L); });

        // then
        assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("존재하는 task id로 deleteTask() 요청하면, task 삭제됨")
    @Test
    void givenCreateTask_whenDeleteTaskWithExistedId_thenTaskIsDeleted() {
        // given
        Task task = new Task();
        task.setTitle("title");
        service.createTask(task);

        // when
        service.deleteTask(1L);

        // then
        Throwable thrown = catchThrowable(() -> { service.getTask(1L); });
        assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
    }
}
