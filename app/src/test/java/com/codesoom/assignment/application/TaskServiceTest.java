package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스의")
public class TaskServiceTest {
    private final Long GIVEN_SAVED_TASK_ID = 1L;
    private final Long GIVEN_UNSAVED_TASK_ID = 100L;
    private final String GIVEN_TASK_TITLE = "Test";
    private final String GIVEN_MODIFY_TASK_TITLE = "Modified";

    private final int REPEAT_TIME = 3;

    private TaskService taskService;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        task = new Task();
        task.setTitle(GIVEN_TASK_TITLE);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("task가 없다면")
        class Context_without_any_task {
            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_return_empty() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }

        @Nested
        @DisplayName("task가 1개 이상있다면")
        class Context_with_more_than_task {
            @BeforeEach
            void setAddedTask(RepetitionInfo repetitionInfo) {
                IntStream.rangeClosed(1, repetitionInfo.getCurrentRepetition())
                        .forEach(i -> taskService.createTask(task));
            }

            @RepeatedTest(REPEAT_TIME)
            @DisplayName("크기가 1이상인 리스트를 리턴한다.")
            void it_return_list_having_task_one_or_more() {
                assertThat(taskService.getTasks().size()).isGreaterThanOrEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {
        @BeforeEach
        void setAddedTask() {
            taskService.createTask(task);
        }

        @Nested
        @DisplayName("저장된 id를 가지고 있다면")
        class Context_with_saved_id {
            private Task gotten;

            @BeforeEach
            void setGottenTask() {
                gotten = taskService.getTask(GIVEN_SAVED_TASK_ID);
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
                        () -> taskService.getTask(GIVEN_UNSAVED_TASK_ID),
                        "task를 찾을 수 없다는 예외를 던져야 합니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        private int size;
        private Task added;

        @BeforeEach
        void setAddedTask() {
            size = taskService.getTasks().size();
            added = taskService.createTask(task);
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
            assertThat(taskService.getTasks().size()).isEqualTo(size + 1);
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        private Task modifying;

        @BeforeEach
        void setAddedAndModifiedTask() {
            taskService.createTask(task);

            modifying = new Task();
            modifying.setTitle(GIVEN_MODIFY_TASK_TITLE);
        }

        @Nested
        @DisplayName("저장된 id를 가지고 있다면")
        class Context_with_saved_id {
            @Test
            @DisplayName("task를 수정하고, 수정된 task를 리턴한다.")
            void it_return_modified_task() {
                final Task modified = taskService.updateTask(GIVEN_SAVED_TASK_ID, modifying);

                assertThat(modified.getClass()).isEqualTo(Task.class);
                assertThat(modified.getId()).isEqualTo(GIVEN_SAVED_TASK_ID);
                assertThat(modified.getTitle()).isNotEqualTo(GIVEN_TASK_TITLE);
                assertThat(modified.getTitle()).isEqualTo(GIVEN_MODIFY_TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskService.updateTask(GIVEN_UNSAVED_TASK_ID, modifying),
                        "task를 찾을 수 없다는 예외를 던져야 합니다"
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @BeforeEach
        void setAddedTask() {
            taskService.createTask(task);
        }

        @Nested
        @DisplayName("저장된 id를 가지고 있다면")
        class Context_with_saved_id {
            private int size;
            private Task deleted;

            @BeforeEach
            void setDeletedTask() {
                size = taskService.getTasks().size();
                deleted = taskService.deleteTask(GIVEN_SAVED_TASK_ID);
            }

            @Test
            @DisplayName("task를 삭제하고, 삭제된 task를 리턴한다.")
            void it_return_deleted_task() {
                assertThat(deleted.getClass()).isEqualTo(Task.class);
                assertThat(deleted.getId()).isEqualTo(GIVEN_SAVED_TASK_ID);
                assertThat(deleted.getTitle()).isEqualTo(GIVEN_TASK_TITLE);
            }

            @Test
            @DisplayName("task 리스트의 크기를 1 감소시킨다.")
            void it_count_down_1_task_list_size() {
                assertThat(taskService.getTasks().size()).isEqualTo(size - 1);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskService.deleteTask(GIVEN_UNSAVED_TASK_ID),
                        "task를 찾을 수 없다는 예외를 던져야 합니다"
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
