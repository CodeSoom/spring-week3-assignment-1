package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("할 일에 대한 유닛 테스트")
class TaskControllerTest {

    private static final String TEST_TASK_TITLE = "테스트";
    private static final String TEST_TASK_UPDATE_TITLE_POSTFIX = "_수정";

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Nested
    @DisplayName("할 일 목록 조회 시")
    class Describe_list {

        @Nested
        @DisplayName("할 일 목록 수 만큼")
        class Context_hasTaskCount {

            final int givenTaskCount = 10;

            @BeforeEach
            void given() {
                IntStream.rangeClosed(1, givenTaskCount)
                        .forEach((index) -> generateTask(String.format("%s_%s", TEST_TASK_TITLE, index)));
            }

            List<Task> subject() {
                return taskController.list();
            }

            @Test
            @DisplayName("할 일 목록을 리턴 한다.")
            void it_return_tasks() {
                assertThat(subject()).hasSize(givenTaskCount);
            }
        }
    }

    @Nested
    @DisplayName("할 일 상세 조회 시")
    class Describe_detail {

        @Nested
        @DisplayName("일치하는 할일 이 있다면")
        class Context_existsTask {

            Task givenTask;

            @BeforeEach
            void given() {
                givenTask = generateTask(TEST_TASK_TITLE);
            }

            Task subject() {
                return taskController.detail(givenTask.getId());
            }

            @Test
            @DisplayName("할 일을 리턴 한다.")
            void it_return_task() {
                assertThat(subject()).isEqualTo(givenTask);
            }
        }

        @Nested
        @DisplayName("일치 하는 할일 이 없다면")
        class Context_notExistsTask {

            final Long notExistsTaskId = 999L;

            @Test
            @DisplayName("예외를 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.detail(notExistsTaskId)
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("할 일 변경 시")
    class Describe_patch {

        final String title = TEST_TASK_TITLE + TEST_TASK_UPDATE_TITLE_POSTFIX;
        final Task source = new Task(title);

        @Nested
        @DisplayName("일치하는 할일 이 있다면")
        class Context_existsTask {

            Task givenTask;

            @BeforeEach
            void given() {
                givenTask = generateTask(TEST_TASK_TITLE);
            }

            Task subject() {
                return taskController.patch(givenTask.getId(), source);
            }

            @Test
            @DisplayName("할 일을 수정 하고 할 일을 리턴 한다.")
            void it_patch_and_return_task() {
                assertAll(
                        () -> assertThat(subject()).isEqualTo(givenTask),
                        () -> assertThat(subject().getTitle()).isEqualTo(title)
                );
            }
        }

        @Nested
        @DisplayName("일치하는 할일 이 없다면")
        class Context_notExistsTask {

            final Long notExistsTaskId = 999L;

            @Test
            @DisplayName("예외를 던진다")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.patch(notExistsTaskId, source)
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("할 일 대체시")
    class Describe_update {

        final String title = TEST_TASK_TITLE + TEST_TASK_UPDATE_TITLE_POSTFIX;
        final Task source = new Task(title);

        @Nested
        @DisplayName("일치하는 할일 이 있다면")
        class Context_existsTask {

            Task givenTask;

            @BeforeEach
            void given() {
                givenTask = generateTask(TEST_TASK_TITLE);
            }

            Task subject() {
                return taskController.update(givenTask.getId(), source);
            }

            @Test
            @DisplayName("할 일을 대체하고 대체된 할 일을 리턴한다.")
            void it_update_and_return_task() {
                Task task = subject();
                assertThat(task.getTitle()).isEqualTo(title);
            }
        }

        @Nested
        @DisplayName("일치하는 할일 이 없다면")
        class Context_notExistsTask {

            final Long notExistsTaskId = 999L;

            @Test
            @DisplayName("예외를 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.update(notExistsTaskId, source)
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("할 일 등록 시")
    class Describe_save {
        @Nested
        @DisplayName("유효한 할 일 값이 주어진다면")
        class Context_valid {

            final Task source = new Task(TEST_TASK_TITLE);

            @Test
            @DisplayName("할 일을 등록하고 할 일을 리턴 한다.")
            void it_save_and_return_task() {

                Task savedTask = taskController.create(source);

                assertAll(
                        () -> assertThat(savedTask.getId()).isNotNull(),
                        () -> assertThat(savedTask.getTitle()).isEqualTo(TEST_TASK_TITLE)
                );
            }
        }
    }

    @Nested
    @DisplayName("할 일 삭제 요청시")
    class Describe_delete {

        @Nested
        @DisplayName("주어진 아이디와 일치하는 할일 이 있다면")
        class Context_existsTask {

            Long taskId;

            @BeforeEach
            void setUp() {
                Task savedTask = generateTask(TEST_TASK_TITLE);
                taskId = savedTask.getId();
            }

            @Test
            @DisplayName("할 일을 삭제하고 예외를 던지지 않는다.")
            void it_deleteTask() {
                assertDoesNotThrow(
                        () -> taskController.delete(taskId)
                );
            }
        }

        @Nested
        @DisplayName("주어진 아이디와 일치하는 할일 이 없다면")
        class Context_notExistsTask {

            final Long notExistsTaskId = 999L;

            @Test
            @DisplayName("예외를 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(() -> taskController.detail(notExistsTaskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    private Task generateTask(String title) {
        Task source = new Task();
        source.setTitle(title);
        return taskController.create(source);
    }
}