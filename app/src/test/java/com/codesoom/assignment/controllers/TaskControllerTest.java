package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 클래스의")
class TaskControllerTest {
    private final Long GIVEN_SAVED_TASK_ID = 1L;
    private final Long GIVEN_UNSAVED_TASK_ID = 100L;
    private final String GIVEN_TASK_TITLE = "Test";
    private final String GIVEN_MODIFY_TASK_TITLE = "Modified";

    private final int REPEAT_TIME = 3;

    private TaskController taskController;
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle(GIVEN_TASK_TITLE);

        taskController = new TaskController(new TaskService());
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("task가 없다면")
        class Context_without_any_task {
            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_return_empty_list() {
                assertThat(taskController.list()).isEmpty();
            }
        }

        @Nested
        @DisplayName("task가 1개 이상있다면")
        class Context_with_more_than_task {
            @BeforeEach
            void setAddedTask(RepetitionInfo repetitionInfo) {
                IntStream.rangeClosed(1, repetitionInfo.getCurrentRepetition())
                        .forEach(i -> taskController.create(task));
            }

            @RepeatedTest(REPEAT_TIME)
            @DisplayName("크기가 1이상인 리스트를 리턴한다.")
            void it_return_list_having_task_one_or_more() {
                assertThat(taskController.list().size()).isGreaterThanOrEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {
        @BeforeEach
        void setAddedTask() {
            taskController.create(task);
        }

        @Nested
        @DisplayName("저장된 id를 가지고 있다면")
        class Context_with_saved_id {
            private Task gotten;
            @BeforeEach
            void setGottenTask() {
                gotten = taskController.detail(GIVEN_SAVED_TASK_ID);
            }

            @Test
            @DisplayName("task를 리턴한다.")
            void it_return_task() {
                assertThat(gotten.getClass()).isEqualTo(Task.class);
                assertThat(gotten.getId()).isEqualTo(GIVEN_SAVED_TASK_ID);
                assertThat(gotten.getTitle()).isEqualTo(GIVEN_TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.detail(GIVEN_UNSAVED_TASK_ID),
                        "task를 찾을 수 없다는 예외를 던져야 합니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        private int size;
        private Task added;

        @BeforeEach
        void setAddedTask() {
            size = taskController.list().size();
            added = taskController.create(task);
        }

        @Test
        @DisplayName("task를 추가하고, 추가된 task를 리턴한다.")
        void it_return_added_task() {
            assertThat(added.getClass()).isEqualTo(Task.class);
            assertThat(added.getId()).isEqualTo(GIVEN_SAVED_TASK_ID);
            assertThat(added.getTitle()).isEqualTo(GIVEN_TASK_TITLE);
        }

        @Test
        @DisplayName("task 리스트의 크기를 1 증가시킨다.")
        void it_count_up_task_list_size() {
            assertThat(taskController.list().size()).isEqualTo(size + 1);
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        private Task modifying;

        @BeforeEach
        void setAddedAndModifiedTask() {
            taskController.create(task);

            modifying = new Task();
            modifying.setTitle(GIVEN_MODIFY_TASK_TITLE);
        }

        @Nested
        @DisplayName("저장된 id를 가지고 있다면")
        class Context_with_saved_id {
            @Test
            @DisplayName("task를 수정하고, 수정된 task를 리턴한다.")
            void it_return_modified_task() {
                Task modified = taskController.update(GIVEN_SAVED_TASK_ID, modifying);
                assertThat(taskController.detail(GIVEN_SAVED_TASK_ID).getClass()).isEqualTo(Task.class);
                assertThat(taskController.detail(GIVEN_SAVED_TASK_ID).getId()).isEqualTo(GIVEN_SAVED_TASK_ID);
                assertThat(taskController.detail(GIVEN_SAVED_TASK_ID).getTitle()).isNotEqualTo(GIVEN_TASK_TITLE);
                assertThat(taskController.detail(GIVEN_SAVED_TASK_ID).getTitle()).isEqualTo(GIVEN_MODIFY_TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.update(GIVEN_UNSAVED_TASK_ID, modifying),
                        "task를 찾을 수 없다는 예외를 던져야 합니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {
        private Task modifying;

        @BeforeEach
        void setAddedAndModifiedTask() {
            taskController.create(task);

            modifying = new Task();
            modifying.setTitle(GIVEN_MODIFY_TASK_TITLE);
        }

        @Nested
        @DisplayName("저장된 id를 가지고 있다면")
        class Context_with_saved_id {
            @Test
            @DisplayName("task를 수정하고, 수정된 task를 리턴한다.")
            void it_return_modified_task() {
                taskController.patch(GIVEN_SAVED_TASK_ID, modifying);
                assertThat(taskController.detail(GIVEN_SAVED_TASK_ID).getClass()).isEqualTo(Task.class);
                assertThat(taskController.detail(GIVEN_SAVED_TASK_ID).getId()).isEqualTo(GIVEN_SAVED_TASK_ID);
                assertThat(taskController.detail(GIVEN_SAVED_TASK_ID).getTitle()).isNotEqualTo(GIVEN_TASK_TITLE);
                assertThat(taskController.detail(GIVEN_SAVED_TASK_ID).getTitle()).isEqualTo(GIVEN_MODIFY_TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.patch(GIVEN_UNSAVED_TASK_ID, modifying),
                        "task를 찾을 수 없다는 예외를 던져야 합니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @BeforeEach
        void setAddedTask() {
            taskController.create(task);
        }

        @Nested
        @DisplayName("저장된 id를 가지고 있다면")
        class Context_with_saved_id {
            private int size;

            @BeforeEach
            void setDeletedTask() {
                size = taskController.list().size();
                taskController.delete(GIVEN_SAVED_TASK_ID);
            }

            @Test
            @DisplayName("task를 삭제하고, task 리스트의 크기를 1 감소시킨다.")
            void it_count_down_1_task_list_size() {
                assertThat(taskController.list().size()).isEqualTo(size - 1);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.delete(GIVEN_UNSAVED_TASK_ID),
                        "task를 찾을 수 없다는 예외를 던져야 합니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
