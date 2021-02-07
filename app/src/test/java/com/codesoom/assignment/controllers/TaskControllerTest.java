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
    private final Long givenSavedTaskId = 1L;
    private final Long givenUnsavedTaskId = 100L;
    private final String givenTaskTitle = "Test";
    private final String givenModifyTaskTitle = "Modified";

    private final int repeatTime = 2;

    private TaskController taskController;
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle(givenTaskTitle);

        taskController = new TaskController(new TaskService());
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("저장된 task가 없다면")
        class Context_without_any_task {
            @Test
            @DisplayName("비어있는 리스트를 리턴한다.")
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

            @RepeatedTest(value = repeatTime, name = "크기가 1이상인 리스트를 리턴한다.")
            void it_return_list_having_task_one_or_more() {
                assertThat(taskController.list().size()).isGreaterThanOrEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        private Long givenId;

        @Nested
        @DisplayName("저장된 task의 id를 가지고 있다면")
        class Context_with_saved_id {
            private Task selected;

            @BeforeEach
            void setSavedId() {
                givenId = taskController.create(task).getId();
            }

            @Test
            @DisplayName("task를 리턴한다.")
            void it_return_task() {
                selected = taskController.detail(givenId);

                assertThat(selected.getClass()).isEqualTo(Task.class);
                assertThat(selected.getId()).isEqualTo(givenSavedTaskId);
                assertThat(selected.getTitle()).isEqualTo(givenTaskTitle);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 task의 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @BeforeEach
            void setUnsavedId() {
                givenId = givenUnsavedTaskId;
            }

            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.detail(givenId),
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

        @Nested
        @DisplayName("task를 추가하고,")
        class It_add_task {
            @BeforeEach
            void addTask() {
                size = taskController.list().size();
                added = taskController.create(task);
            }

            @Test
            @DisplayName("추가된 task를 리턴한다.")
            void it_return_added_task() {
                assertThat(added.getClass()).isEqualTo(Task.class);
                assertThat(added.getTitle()).isEqualTo(givenTaskTitle);
            }

            @Test
            @DisplayName("task 리스트의 크기를 1 증가시킨다.")
            void it_count_up_task_list_size() {
                assertThat(taskController.list().size()).isEqualTo(size + 1);
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        private Long givenId;
        private Task modifying;

        @BeforeEach
        void setModifiedTask() {
            modifying = new Task();
            modifying.setTitle(givenModifyTaskTitle);
        }

        @Nested
        @DisplayName("저장된 task의 id를 가지고 있다면")
        class Context_with_saved_id {
            private Task modified;

            @BeforeEach
            void setSavedId() {
                givenId = taskController.create(task).getId();
            }

            @Test
            @DisplayName("task를 수정하고, 수정된 task를 리턴한다.")
            void it_modify_task_and_return_modified_task() {
                modified = taskController.update(givenId, modifying);

                assertThat(modified.getClass()).isEqualTo(Task.class);
                assertThat(modified.getId()).isEqualTo(givenSavedTaskId);
                assertThat(modified.getTitle()).isNotEqualTo(givenTaskTitle);
                assertThat(modified.getTitle()).isEqualTo(givenModifyTaskTitle);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 task의 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @BeforeEach
            void setUnsavedId() {
                givenId = givenUnsavedTaskId;
            }

            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.update(givenId, modifying),
                        "task를 찾을 수 없다는 예외를 던져야 합니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {
        private Long givenId;
        private Task modifying;

        @BeforeEach
        void setModifiedTask() {
            modifying = new Task();
            modifying.setTitle(givenModifyTaskTitle);
        }

        @Nested
        @DisplayName("저장된 taks의 id를 가지고 있다면")
        class Context_with_saved_id {
            private Task modified;

            @BeforeEach
            void setSavedId() {
                givenId = taskController.create(task).getId();
            }

            @Test
            @DisplayName("task를 수정하고, 수정된 task를 리턴한다.")
            void it_modify_task_and_return_modified_task() {
                modified = taskController.patch(givenId, modifying);

                assertThat(modified.getClass()).isEqualTo(Task.class);
                assertThat(modified.getId()).isEqualTo(givenSavedTaskId);
                assertThat(modified.getTitle()).isNotEqualTo(givenTaskTitle);
                assertThat(modified.getTitle()).isEqualTo(givenModifyTaskTitle);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 task의 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @BeforeEach
            void setUnsavedId() {
                givenId = givenUnsavedTaskId;
            }

            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.patch(givenId, modifying),
                        "task를 찾을 수 없다는 예외를 던져야 합니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        private Long givenId;

        @BeforeEach
        void setAddedTask() {
            taskController.create(task);
        }

        @Nested
        @DisplayName("저장된 task의 id를 가지고 있다면")
        class Context_with_saved_id {
            private int size;

            @BeforeEach
            void setSavedId() {
                givenId = givenSavedTaskId;
                size = taskController.list().size();
            }

            @Test
            @DisplayName("task를 삭제하고, task 리스트의 크기를 1 감소시킨다.")
            void it_delete_task_and_count_down_1_task_list_size() {
                taskController.delete(givenId);

                assertThat(taskController.list().size()).isEqualTo(size - 1);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 task의 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @BeforeEach
            void setUnsavedId() {
                givenId = givenUnsavedTaskId;
            }

            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskController.delete(givenId),
                        "task를 찾을 수 없다는 예외를 던져야 합니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
