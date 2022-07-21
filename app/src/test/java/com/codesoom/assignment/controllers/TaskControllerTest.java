package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

class TaskControllerTest {
    abstract class Context_didCreateTwoTasks {
        @BeforeEach
        void context() {
            final Task task1 = new Task();
            task1.setTitle(EXAMPLE_TITLE + 1);
            final Task task2 = new Task();
            task2.setTitle(EXAMPLE_TITLE + 2);
            controller.create(task1);
            controller.create(task2);
        }
    }

    public static final String EXAMPLE_TITLE = "title";
    private TaskController controller;

    @BeforeEach
    void setup() {
        controller = new TaskController(new TaskService());
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("할일이 생성되지 않았을 때")
        class Context_didNotCreateTask {
            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returnsEmptyList() {
                List<Task> result = controller.list();

                assertThat(result).isEmpty();
            }
        }

        @Nested
        @DisplayName("할일을 생성했었을 때")
        class Context_didCreateContext extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("생성된 할일 목록을 반환한다")
            void it_returnsTasks() {
                List<Task> result = controller.list();

                assertThat(result).isNotEmpty();
                assertThat(result.get(0).getTitle()).isEqualTo(EXAMPLE_TITLE + 1);
                assertThat(result.get(0).getId()).isEqualTo(1L);
                assertThat(result.get(1).getTitle()).isEqualTo(EXAMPLE_TITLE + 2);
                assertThat(result.get(1).getId()).isEqualTo(2L);
            }
        }
    }

    @DisplayName("찾을 수 없는 id로 조회했을 때, 할일을 찾을 수 없다는 에러 던져짐")
    @Test
    void whenGetDetailWithNotFindableTaskId_thenThrowTaskNotFound() {
        // when
        Throwable thrown = catchThrowable(() -> { controller.detail(0L); });

        // then
        then(thrown).isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("찾을 수 있는 id로 조회했을 때, 할일 반환")
    @Test
    void givenCreatedTask_whenGetDetailWithFindableTaskId_thenReturnTask() {
        // given
        Task task = new Task();
        task.setTitle(EXAMPLE_TITLE);
        controller.create(task);

        // when
        Task result = controller.detail(1L);

        // then
        then(result.getId()).isEqualTo(1L);
        then(result.getTitle()).isEqualTo(EXAMPLE_TITLE);
    }

    @DisplayName("찾을 수 없는 id로 업데이트 했을 때, 할일을 찾을 수 없다고 에러 던져짐")
    @Test
    void whenUpdateTaskWithNotFindableTaskId_thenThrowTaskNotFound() {
        // when
        Task newTask = new Task();
        newTask.setTitle(EXAMPLE_TITLE);
        Throwable thrown = catchThrowable(() -> { controller.update(0L, newTask); });

        // then
        then(thrown).isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("찾을 수 있는 id로 업데이트 했을 때, 업데이트된 할일 반환")
    @Test
    void givenCreateTask_whenUpdateTaskWithFindableTaskId_thenReturnUpdatedTask() {
        // given
        Task oldTask = new Task();
        oldTask.setTitle(EXAMPLE_TITLE);
        controller.create(oldTask);

        // when
        Task newTask = new Task();
        newTask.setTitle(EXAMPLE_TITLE + 1);
        Task updatedTask = controller.update(1L, newTask);

        // then
        then(updatedTask.getId()).isEqualTo(1L);
        then(updatedTask.getTitle()).isEqualTo(EXAMPLE_TITLE + 1);
    }

    @DisplayName("찾을 수 없는 할일 id로 삭제 했을 때, 할일을 찾을 수 없다고 에러 던져짐")
    @Test
    void whenDeleteTaskWithNotFindableTaskId_thenThrowTaskNotFound() {
        // when
        Throwable thrown = catchThrowable(() -> { controller.delete(0L); });

        // then
        then(thrown).isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("찾을 수 있는 id로 업데이트 했을 때, 할일 삭제됨")
    @Test
    void givenCreateTask_whenDeleteTaskWithFindableTaskId_thenDeleted() {
        // given
        Task oldTask = new Task();
        oldTask.setTitle(EXAMPLE_TITLE);
        controller.create(oldTask);

        // when
        controller.delete(1L);

        // then
        assertThat(controller.list()).isEmpty();
    }
}
