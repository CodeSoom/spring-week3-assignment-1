package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.web.bind.annotation.PatchMapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskControllerTest 클래스")
class TaskControllerTest {
    private TaskController controller;

    @BeforeEach
    void setUp() {
        controller = new TaskController(new TaskService());
    }

    void createTestTask(String source) {
        Task task = new Task();
        task.setTitle(source);

        controller.create(task);
    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {

        @Nested
        @DisplayName("Task가 없을 경우")
        class Context_not_exist_task {
            @Test
            @DisplayName("빈 배열을 리턴한다.")
            void it_return_nothing() {
                assertThat(controller.list()).isEmpty();
            }
        }

        @Nested
        @DisplayName("Task가 존재할 경우")
        class Context_exist_task {
            @Test
            @DisplayName("Task 전체 list를 리턴한다.")
            void it_return_all() {
                createTestTask("test");
                createTestTask("test2");

                assertThat(controller.list()).hasSize(2);
            }
        }
    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {

        @Nested
        @DisplayName("올바르지 않은 ID값이 주어지면")
        class Context_wrong_ID {
            @Test
            @DisplayName("TaskNotFound에러를 발생시킨다.")
            void it_throw_exception() {
                assertThatThrownBy(() -> controller.detail(1L)).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("올바른 ID값이 주어지면")
        class Context_right_ID {
            @Test
            @DisplayName("해당 ID의 Task를 리턴한다.")
            void it_return_task() {
                createTestTask("test");
                createTestTask("test2");

                assertThat(controller.detail(1L).getTitle()).isEqualTo("test");
                assertThat(controller.detail(2L).getTitle()).isEqualTo("test2");
            }
        }
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        @Nested
        @DisplayName("title 값이 주어지면")
        class Context_right_title {
            @Test
            @DisplayName("Task를 생성하고 해당 Task를 리턴한다.")
            void it_create_task_and_return() {
                Task task = new Task();
                task.setTitle("test");

                assertThat(controller.create(task).getTitle()).isEqualTo("test");
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {

        @Nested
        @DisplayName("올바르지 않은 ID값이 주어지면")
        class Context_wrong_ID {
            @Test
            @DisplayName("TaskNotFound 에러를 발생시킨다.")
            void it_throw_Exception() {
                createTestTask("test");

                Task task = new Task();
                task.setTitle("New test");

                assertThatThrownBy(() -> controller.update(2L, task)).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("올바른 ID값과 title값이 주어지면")
        class Context_right_ID_and_title {
            @Test
            @DisplayName("title의 값을 변경하고, 해당 Task를 리턴한다.")
            void it_update_task() {
                createTestTask("test");

                Task task = new Task();
                task.setTitle("New test");

                assertThat(controller.update(1L, task).getTitle()).isEqualTo("New test");
            }
        }
    }

    @Nested
    @DisplayName("patch 메서드는")
    class Describe_patch {

        @Nested
        @DisplayName("올바르지 않은 ID값이 주어지면")
        class Context_wrong_ID {
            @Test
            @DisplayName("TaskNotFound 에러를 발생시킨다.")
            void it_throw_Exception() {
                createTestTask("test");

                Task task = new Task();
                task.setTitle("New test");

                assertThatThrownBy(() -> controller.update(2L, task)).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("올바른 ID값과 title값이 주어지면")
        class Context_right_ID_and_title {
            @Test
            @DisplayName("title의 값을 변경하고, 해당 Task를 리턴한다.")
            void it_update_task() {
                createTestTask("test");

                Task task = new Task();
                task.setTitle("New test");

                assertThat(controller.update(1L, task).getTitle()).isEqualTo("New test");
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {

        @Nested
        @DisplayName("올바르지 않은 ID값이 주어지면")
        class Context_wrong_ID {
            @Test
            @DisplayName("TaskNotFound 에러를 발생시킨다.")
            void it_throw_Exception() {
                assertThatThrownBy(() -> controller.delete(1L)).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("올바른 ID값이 주어지면")
        class Context_right_ID {
            @Test
            @DisplayName("해당 Task를 삭제한다.")
            void it_delete_task() {
                createTestTask("test");

                assertThat(controller.detail(1L).getTitle()).isEqualTo("test");

                controller.delete(1L);

                assertThat(controller.list()).isEmpty();
            }
        }
    }
}
