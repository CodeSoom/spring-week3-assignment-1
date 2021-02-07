package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스의")
public class TaskServiceTest {
    private final Long givenSavedTaskId = 1L;
    private final Long givenUnsavedTaskId = 100L;
    private final String givenTaskTitle = "Test";
    private final String givenModifyTaskTitle = "Modified";

    private final int repeatTime = 2;

    private TaskService taskService;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        task = new Task();
        task.setTitle(givenTaskTitle);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("저장된 task가 없다면")
        class Context_without_any_task {
            @Test
            @DisplayName("비어있는 리스트를 리턴한다.")
            void it_return_empty_list() {
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

            @RepeatedTest(value = repeatTime, name = "크기가 1이상인 리스트를 리턴한다.")
            void it_return_list_having_task_one_or_more() {
                assertThat(taskService.getTasks().size()).isGreaterThanOrEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        private Long givenId;

        @Nested
        @DisplayName("저장된 task의 id를 가지고 있다면")
        class Context_with_saved_id {
            private Task selected;

            @BeforeEach
            void setSavedId() {
                givenId = taskService.createTask(task).getId();
            }

            @Test
            @DisplayName("task를 리턴한다.")
            void it_return_task() {
                selected = taskService.getTask(givenId);

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
                        () -> taskService.getTask(givenId),
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

        @Nested
        @DisplayName("task를 추가하고,")
        class It_add_task {
            @BeforeEach
            void addTask() {
                size = taskService.getTasks().size();
                added = taskService.createTask(task);
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
                assertThat(taskService.getTasks().size()).isEqualTo(size + 1);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
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
                givenId = taskService.createTask(task).getId();
            }

            @Test
            @DisplayName("task를 수정하고, 수정된 task를 리턴한다.")
            void it_return_modified_task() {
                modified = taskService.updateTask(givenId, modifying);

                assertThat(modified.getClass()).isEqualTo(Task.class);
                assertThat(modified.getId()).isEqualTo(givenSavedTaskId);
                assertThat(modified.getTitle()).isNotEqualTo(givenTaskTitle);
                assertThat(modified.getTitle()).isEqualTo(givenModifyTaskTitle);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 id를 가지고 있다면")
        class Context_with_unsaved_id {
            @BeforeEach
            void setUnsavedId() {
                givenId = givenUnsavedTaskId;
            }

            @Test
            @DisplayName("task를 찾을 수 없다는 exception을 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskService.updateTask(givenId, modifying),
                        "task를 찾을 수 없다는 예외를 던져야 합니다"
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        private Long givenId;

        @Nested
        @DisplayName("저장된 task의 id를 가지고 있다면")
        class Context_with_saved_id {
            private int size;
            private Task deleted;

            @BeforeEach
            void setSavedId() {
                givenId = taskService.createTask(task).getId();
                size = taskService.getTasks().size();
            }

            @Test
            @DisplayName("task를 삭제하고, 삭제된 task를 리턴한다.")
            void it_delete_task_return_deleted_task() {
                deleted = taskService.deleteTask(givenId);

                assertThat(taskService.getTasks().size()).isEqualTo(size - 1);
                assertThat(deleted.getClass()).isEqualTo(Task.class);
                assertThat(deleted.getId()).isEqualTo(givenSavedTaskId);
                assertThat(deleted.getTitle()).isEqualTo(givenTaskTitle);
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
                        () -> taskService.deleteTask(givenId),
                        "task를 찾을 수 없다는 예외를 던져야 합니다."
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
