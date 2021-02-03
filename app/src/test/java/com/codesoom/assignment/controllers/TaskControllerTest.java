package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 클래스의 ")
class TaskControllerTest {
    private static final Long VALID_TASK_ID = 1L;
    private static final Long INVALID_TASK_ID = 100L;
    private static final String TASK_TITLE = "Test";
    private static final String MODIFY_TASK_TITLE = "Modified";

    private TaskController taskController;
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle(TASK_TITLE);

        taskController = new TaskController(new TaskService());
    }

    @Nested
    @DisplayName("list 메소드는 ")
    class Describe_list {
        @Nested
        @DisplayName("task가 없다면 ")
        class Context_without_any_task {
            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_return_empty_list() {
                assertThat(taskController.list()).isEmpty();
            }
        }

        @Nested
        @DisplayName("task가 1개 이상있다면 ")
        class Context_with_task {
            @BeforeEach
            void setAddedTask() {
                taskController.create(task);
            }

            @Test
            @DisplayName("크기가 1이상인 리스트를 리턴한다.")
            void it_return_not_empty_list() {
                assertThat(taskController.list()).isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("detail 메서드는 ")
    class Describe_detail {
        @BeforeEach
        void setAddedTask() {
            taskController.create(task);
        }

        @Nested
        @DisplayName("유효한 id를 가지고 있다면 ")
        class Context_with_valid_id {
            @Test
            @DisplayName("task를 리턴한다.")
            void it_return_task() {
                assertThat(taskController.detail(VALID_TASK_ID).getClass()).isEqualTo(Task.class);
                assertThat(taskController.detail(VALID_TASK_ID).getId()).isEqualTo(VALID_TASK_ID);
                assertThat(taskController.detail(VALID_TASK_ID).getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 id를 가지고 있다면 ")
        class Context_with_invalid_id {
            @Test
            @DisplayName("exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.detail(INVALID_TASK_ID),
                        "task를 찾을 수 없습니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는 ")
    class Describe_create {
        private int size;
        private Task added;

        @BeforeEach
        void setAddedTask() {
            size = taskController.list().size();
            added = taskController.create(task);
        }

        @Test
        @DisplayName("추가된 task를 리턴한다.")
        void it_return_added_task() {
            assertThat(added.getClass()).isEqualTo(Task.class);
            assertThat(added.getId()).isEqualTo(VALID_TASK_ID);
            assertThat(added.getTitle()).isEqualTo(TASK_TITLE);
        }

        @Test
        @DisplayName("task 리스트의 크기를 1 증가시킨다.")
        void it_count_up_task_list_size() {
            assertThat(taskController.list().size()).isEqualTo(size + 1);
        }
    }

    @Nested
    @DisplayName("update 메소드는 ")
    class Describe_update {
        private Task modifying;

        @BeforeEach
        void setAddedAndModifiedTask() {
            taskController.create(task);

            modifying = new Task();
            modifying.setTitle(MODIFY_TASK_TITLE);
        }

        @Nested
        @DisplayName("유효한 id를 가지고 있다면 ")
        class Context_with_valid_id {
            @Test
            @DisplayName("수정된 task를 리턴한다.")
            void it_return_modified_task() {
                Task modified = taskController.update(VALID_TASK_ID, modifying);
                assertThat(taskController.detail(VALID_TASK_ID).getClass()).isEqualTo(Task.class);
                assertThat(taskController.detail(VALID_TASK_ID).getId()).isEqualTo(VALID_TASK_ID);
                assertThat(taskController.detail(VALID_TASK_ID).getTitle()).isNotEqualTo(TASK_TITLE);
                assertThat(taskController.detail(VALID_TASK_ID).getTitle()).isEqualTo(MODIFY_TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 id를 가지고 있다면 ")
        class Context_with_invalid_id {
            @Test
            @DisplayName("exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.update(INVALID_TASK_ID, modifying),
                        "task를 찾을 수 없습니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는 ")
    class Describe_patch {
        private Task modifying;

        @BeforeEach
        void setAddedAndModifiedTask() {
            taskController.create(task);

            modifying = new Task();
            modifying.setTitle(MODIFY_TASK_TITLE);
        }

        @Nested
        @DisplayName("유효한 id를 가지고 있다면 ")
        class Context_with_valid_id {
            @Test
            @DisplayName("수정된 task를 리턴한다.")
            void it_return_modified_task() {
                taskController.patch(VALID_TASK_ID, modifying);
                assertThat(taskController.detail(VALID_TASK_ID).getClass()).isEqualTo(Task.class);
                assertThat(taskController.detail(VALID_TASK_ID).getId()).isEqualTo(VALID_TASK_ID);
                assertThat(taskController.detail(VALID_TASK_ID).getTitle()).isNotEqualTo(TASK_TITLE);
                assertThat(taskController.detail(VALID_TASK_ID).getTitle()).isEqualTo(MODIFY_TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 id를 가지고 있다면 ")
        class Context_with_invalid_id {
            @Test
            @DisplayName("exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.patch(INVALID_TASK_ID, modifying),
                        "task를 찾을 수 없습니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는 ")
    class Describe_delete {
        @BeforeEach
        void setAddedTask() {
            taskController.create(task);
        }

        @Nested
        @DisplayName("유효한 id를 가지고 있다면 ")
        class Context_with_valid_id {
            private int size;

            @BeforeEach
            void setDeletedTask() {
                size = taskController.list().size();
                taskController.delete(VALID_TASK_ID);
            }

            @Test
            @DisplayName("task 리스트의 크기를 1 감소시킨다.")
            void it_count_down_1_task_list_size() {
                assertThat(taskController.list().size()).isEqualTo(size - 1);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 id를 가지고 있다면 ")
        class Context_with_invalid_id {
            @Test
            @DisplayName("exception를 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.delete(INVALID_TASK_ID),
                        "task를 찾을 수 없습니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
