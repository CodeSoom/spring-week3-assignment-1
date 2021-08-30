package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.codesoom.assignment.TaskIdGenerator;
import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TaskControllerTest {

    private static final String TASK_TITLE = "task";
    private static final String NEW_TASK_TITLE = "new";

    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // subject
        taskService = spy(new TaskService(new TaskIdGenerator()));
        controller = new TaskController(taskService);

        // fixtures
        controller.create(new Task(1L, TASK_TITLE));
    }

    @Test
    @DisplayName("할 일 목록을 조회한다")
    void getTasks() {
        controller.list();

        verify(taskService).getTasks();
    }

    @Test
    @DisplayName("할 일을 생성한다")
    void createNewTask() {
        Task source = new Task();

        controller.create(source);

        verify(taskService).createTask(source);
    }

    @Nested
    @DisplayName("단일 조회 시")
    class Describe_detail {

        @Nested
        @DisplayName("존재하는 할 일 이라면")
        class Context_existTask {

            @Test
            @DisplayName("할 일을 반환한다")
            void it_detail() {
                long id = 1L;

                controller.detail(id);

                verify(taskService).getTask(id);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 할 일 이라면")
        class Context_notExistTask {

            @Test
            @DisplayName("예외를 던진다")
            void it_throws() {
                long id = 2L;

                assertThatThrownBy(() -> controller.detail(id))
                    .isInstanceOf(TaskNotFoundException.class);

                verify(taskService).getTask(id);
            }
        }
    }

    @Nested
    @DisplayName("수정 시")
    class Describe_updateTask {

        @Nested
        @DisplayName("존재하는 할 일 이라면")
        class Context_existTask {

            @Test
            @DisplayName("수정된 할 일을 반환한다")
            void it_update() {
                Task newTask = new Task();
                newTask.setTitle(NEW_TASK_TITLE);

                controller.update(1L, newTask);

                verify(taskService).updateTask(1L, newTask);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 할 일 이라면")
        class Context_notExistTask {

            @Test
            @DisplayName("예외를 던진다")
            void it_throws() {
                long id = 2L;
                Task source = new Task();

                assertThatThrownBy(() -> controller.update(id, source))
                    .isInstanceOf(TaskNotFoundException.class);

                verify(taskService).updateTask(id, source);
            }
        }
    }

    @Nested
    @DisplayName("삭제 시")
    class Describe_delete {

        @Nested
        @DisplayName("존재하는 할 일 이라면")
        class Context_existTask {

            @Test
            @DisplayName("삭제한다")
            void it_delete() {
                long id = 1L;
                controller.delete(id);

                verify(taskService).deleteTask(id);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 할 일 이라면")
        class Context_notExistTask {

            @Test
            @DisplayName("예외를 던진다")
            void it_throws() {
                long id = 2L;

                assertThatThrownBy(() -> controller.delete(id))
                    .isInstanceOf(TaskNotFoundException.class);

                verify(taskService).deleteTask(id);
            }
        }
    }
}
