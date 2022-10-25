package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskGenerator;
import com.codesoom.assignment.exceptions.NegativeIdException;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 클래스의")
class TaskControllerTest {

    private static final String TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX = "Task not found: ";
    private static final String INVALID_ID_EXCEPTION_MESSAGE_POSTFIX = ": Id는 0 또는 양수만 허용됩니다.";

    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);
    }

    private void addOneTask() {
        final Task task = TaskGenerator.generateTaskWithRandomTitle();
        controller.create(task);
    }

    private void addNumberOfTasks(int number) {
        for (int i = 0; i < number; i++) {
            addOneTask();
        }
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_List_Method {

        @Nested
        @DisplayName("등록된 task가 없을 때")
        class Context_When_None_Tasks_Registered {

            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_returns_an_empty_list() {
                assertThat(controller.list()).isEmpty();
            }
        }

        @Nested
        @DisplayName("등록된 task가 있을 때")
        class Context_When_Tasks_Registered {

            @Test
            @DisplayName("비어 있지 않은 리스트를 반환한다.")
            void it_returns_a_not_empty_list() {
                addOneTask();
                assertThat(controller.list()).isNotEmpty();
            }

            @ParameterizedTest(name = "{0}개의 task를 등록했으면, 길이가 {0}인 list를 반환한다.")
            @ValueSource(ints = {1, 14, 48, 342})
            void it_returns_a_list_with_length_equals_to_tasks_registered(int number) {
                addNumberOfTasks(number);

                final List<Task> taskList = taskService.getTasks();
                assertThat(taskList.size()).isEqualTo(number);
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_Detail_Method {

        @Nested
        @DisplayName("음수의 id를 인자로 호출하면")
        class Context_With_Temporary_Negative_Id {

            @Test
            @DisplayName("InvalidIdException을 발생시킨다.")
            void it_throws_an_InvalidIdException() {
                final Long tempId = -1L;

                assertThatThrownBy(() -> controller.detail(tempId))
                        .isInstanceOf(NegativeIdException.class)
                        .hasMessage(tempId + INVALID_ID_EXCEPTION_MESSAGE_POSTFIX);
            }
        }

        @Nested
        @DisplayName("등록된 task가 없을 때")
        class Context_When_None_Tasks_Registered {

            @Nested
            @DisplayName("임의의 양수 id를 인자로 호출하면")
            class Context_With_Temporary_Positive_Id {

                @Test
                @DisplayName("TaskNotFoundException를 발생시킨다.")
                void it_throws_a_TaskNotFoundException() {
                    final Long tempId = -1L;
                    assertThatThrownBy(() -> controller.detail(tempId))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + tempId);
                }
            }
        }

        @Nested
        @DisplayName("등록된 task가 있을 때")
        class Context_With_Tasks_Registered {

            @BeforeEach
            void setUp() {
                addOneTask();
            }

            @Nested
            @DisplayName("매핑된 task가 있는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Exists {

                @Test
                @DisplayName("id에 매핑된 task를 리턴한다.")
                void it_returns_a_task_mapped_to_id() {
                    final Long id = controller.list().get(0).getId();

                    assertThat(controller.detail(id).getId()).isEqualTo(id);
                }
            }

            @Nested
            @DisplayName("매핑된 task가 없는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Is_None {

                @Test
                @DisplayName("TaskNotFoundException을 발생시킨다.")
                void it_throws_a_TaskNotFoundException() {
                    final Long id = Long.MAX_VALUE;

                    assertThatThrownBy(() -> controller.detail(id))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
                }
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_Create_Method {

        @Nested
        @DisplayName("null을 인자로 호출하면")
        class Context_With_Task_Null {

            @Test
            @DisplayName("NullTaskException을 발생시킨다.")
            void it_throws_a_NullTaskException() {
                assertThatThrownBy(() -> controller.create(null))
                        .isInstanceOf(NullTaskException.class);
            }
        }

        @Nested
        @DisplayName("title이 null 또는 빈 문자열인 task 객체를 인자로 호출하면")
        class Context_With_Task_Title_Is_Null_Or_Empty_String {

            @ParameterizedTest(name = "title이 {0}이면 InvalidTaskTitleException을 발생시킨다.")
            @NullAndEmptySource
            void it_throws_a_NullPointerException(String title) {
                final Task task = TaskGenerator.generateTask(title);

                assertThatThrownBy(() -> controller.create(task))
                        .isInstanceOf(InvalidTaskTitleException.class);
            }
        }

        @Test
        @DisplayName("양수의 id 값이 부여된 새로운 task를 리턴한다.")
        void it_returns_a_task_which_has_allocated_id() {
            final Task task = TaskGenerator.generateTaskWithRandomTitle();
            assertThat(task.getId())
                    .isNull();

            final Task addedTask = controller.create(task);

            assertThat(addedTask)
                    .isNotNull()
                    .isInstanceOf(Task.class);

            assertThat(addedTask.getId())
                    .isNotNull()
                    .isPositive();
        }

        @Test
        @DisplayName("등록된 task의 개수를 1만큼 증가시킨다.")
        void it_increments_the_number_tasks_by_one() {
            final int oldSize = controller.list().size();

            addOneTask();

            final int newSize = controller.list().size();

            assertThat(newSize - oldSize).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_Update_Method {

        @Nested
        @DisplayName("음수인 값을 id 인자로 호출하면")
        class Context_With_Negative_Id_value {

            @Test
            @DisplayName("InvalidTaskIdException을 발생시킨다.")
            void it_throws_InvalidTaskIdException() {
                final Task task = TaskGenerator.generateTaskWithRandomTitle();

                assertThatThrownBy(() -> controller.update(-1L, task))
                        .isInstanceOf(InvalidTaskIdException.class);
            }
        }

        @Nested
        @DisplayName("null을 task 인자로 호출하면")
        class Context_With_Null_Task {

            @Test
            @DisplayName("NullTaskException을 발생시킨다.")
            void it_throws_a_NullTaskException() {
                addOneTask();
                final Long id = controller.list().get(0).getId();

                assertThatThrownBy(() -> controller.update(id, null))
                        .isInstanceOf(NullTaskException.class);
            }
        }

        @Nested
        @DisplayName("title이 null 또는 빈 문자열인 task를 인자로 호출하면")
        class Context_With_A_Task_Having_Title_As_Null_Or_Empty_String {

            @ParameterizedTest(name = "title이 {0}일 때, InvalidTaskTitleException을 발생시킨다.")
            @NullAndEmptySource
            void it_throws_an_InvalidTaskTitleException(String title) {
                addOneTask();
                final Long id = controller.list().get(0).getId();
                final Task task = TaskGenerator.generateTask(title);

                assertThatThrownBy(() -> controller.update(id, task))
                        .isInstanceOf(InvalidTaskTitleException.class);
            }
        }

        @Nested
        @DisplayName("등록된 task가 없을 때")
        class Context_When_None_Tasks_Registered {

            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다.")
            void it_throws_a_TaskNotFoundException() {
                final Task task = TaskGenerator.generateTaskWithRandomTitle();
                assertThatThrownBy(() -> controller.update(1L, task))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("등록된 task가 있을 때")
        class Context_When_Tasks_Registered_Exists {

            @Nested
            @DisplayName("매핑된 task가 없는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Is_None {

                @Test
                @DisplayName("TaskNotFoundException을 발생시킨다.")
                void it_throws_a_TaskNotFoundException() {
                    addOneTask();
                    final Task task = TaskGenerator.generateTaskWithRandomTitle();
                    assertThatThrownBy(() -> controller.update(Long.MAX_VALUE, task))
                            .isInstanceOf(TaskNotFoundException.class);
                }
            }

            @Nested
            @DisplayName("매핑된 task가 있는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Exists {

                @Test
                @DisplayName("id에 매핑된 task의 title이 변경된다.")
                void it_changes_title_of_task_mapped_to_id() {
                    addOneTask();
                    final Long id = controller.list().get(0).getId();

                    final String oldTitle = controller.detail(id).getTitle();
                    final String newTitle = controller.update(id, TaskGenerator.generateTaskWithRandomTitle()).getTitle();

                    assertThat(newTitle).isNotEqualTo(oldTitle);
                }
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_Delete_Method {

        @Nested
        @DisplayName("음수인 id를 인자로 호출하면")
        class Context_With_Negative_id {

            @Test
            @DisplayName("InvalidTaskIdException가 발생한다.")
            void it_throws_an_InvalidTaskIdException() {
                assertThatThrownBy(() -> controller.delete(-1L))
                        .isInstanceOf(InvalidTaskIdException.class);
            }
        }

        @Nested
        @DisplayName("등록된 task가 없을 때")
        class Context_When_None_Tasks_Registered {

            @Test
            @DisplayName("TaskNotFoundException를 발생시킨다.")
            void it_throws_a_TaskNotFoundException() {
                final Long id = 1L;
                assertThatThrownBy(() -> controller.delete(id))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
            }
        }

        @Nested
        @DisplayName("매핑된 task가 없는 id를 인자로 호출하면")
        class Context_With_Id_Tasks_Mapped_To_Which_Is_None {

            @Test
            @DisplayName("TaskNotFoundException를 발생시킨다.")
            void it_throws_a_TaskNotFoundException() {
                final Long id = Long.MAX_VALUE;
                assertThatThrownBy(() -> controller.delete(id))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
            }
        }

        @Nested
        @DisplayName("매핑된 task가 있는 id를 인자로 호출하면")
        class Context_With_Id_Tasks_Mapped_To_Which_Exists {

            @Test
            @DisplayName("id에 매핑된 task는 삭제된다.")
            void it_deletes_the_task_mapped_to_id() {
                addOneTask();
                final Long id = controller.list().get(0).getId();
                controller.delete(id);

                assertThatThrownBy(() -> controller.detail(id))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
            }

            @Test
            @DisplayName("등록된 task의 개수는 1만큼 줄어든다.")
            void it_decrements_the_number_of_tasks_registered_by_one() {
                addOneTask();
                final int oldSize = controller.list().size();

                final Long id = controller.list().get(0).getId();
                controller.delete(id);

                final int newSize = controller.list().size();
                assertThat(newSize - oldSize).isEqualTo(-1);
            }
        }
    }
}
