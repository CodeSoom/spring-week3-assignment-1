package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskGenerator;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스의")
class TaskServiceTest {

    private static final String TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX = "Task not found: ";
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    private void addOneTask() {
        final Task task = TaskGenerator.generateTaskWithRandomTitle();
        taskService.createTask(task);
    }

    private void addNumberOfTasks(int number) {
        for (int i = 0; i < number; i++) {
            addOneTask();
        }
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_GetTasks_Method {

        @Nested
        @DisplayName("등록된 task가 없을 때,")
        class Context_When_None_Tasks_Registered {

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_an_empty_list() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }

        @Nested
        @DisplayName("등록된 task가 있을 때,")
        class Context_When_Tasks_Registered {

            @Test
            @DisplayName("비어 있지 않은 리스트를 반환한다.")
            void it_returns_a_not_empty_list() {
                addNumberOfTasks(1);

                assertThat(taskService.getTasks()).isNotEmpty();
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
    @DisplayName("getTask 메소드는")
    class Describe_GetTask_Method {

        @Nested
        @DisplayName("등록된 task가 업을 때,")
        class Context_When_None_Tasks_Registered {

            @Nested
            @DisplayName("임의의 양수의 id를 인자로 호출하면")
            class Context_With_Temporary_Positive_Id {

                @Test
                @DisplayName("TaskNotFoundException 예외를 발생시킨다.")
                void it_throws_a_TaskNotFoundException() {
                    final Long id = 1L;
                    assertThatThrownBy(() -> taskService.getTask(id))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
                }
            }
        }

        @Nested
        @DisplayName("등록된 task가 있을 때,")
        class Context_When_Tasks_Registered_Exists {

            @BeforeEach
            void setUp() {
                TaskServiceTest.this.addOneTask();
            }

            @Nested
            @DisplayName("매핑된 task가 있는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Exists {

                @Test
                @DisplayName("id에 매핑된 task를 반환한다.")
                void it_returns_a_task_mapped_to_id() {
                    final Long id = taskService.getTasks().get(0).getId();
                    final Task task = taskService.getTask(id);

                    assertThat(task.getId()).isEqualTo(id);
                }
            }

            @Nested
            @DisplayName("매핑된 task가 없는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Is_None {

                @Test
                @DisplayName("TaskNotFoundException를 발생시킨다.")
                void it_throws_a_TaskNotFoundException() {
                    final Long id = Long.MAX_VALUE;

                    assertThatThrownBy(() -> taskService.getTask(id))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
                }
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_CreateTask_Method {

        @Test
        @DisplayName("id값이 부여된 task를 반환한다.")
        void it_returns_a_task_to_which_id_is_allocated() {
            final Task src = TaskGenerator.generateTaskWithRandomTitle();
            assertThat(src.getId()).isNull();

            final Task addedTask = taskService.createTask(src);

            assertThat(addedTask.getId()).isNotNull();
        }

        @Test
        @DisplayName("등록된 task의 개수를 1만큼 증가시킨다.")
        void it_increments_the_number_of_tasks_registered_by_one() {
            final int oldSize = taskService.getTasks().size();
            addOneTask();
            final int newSize = taskService.getTasks().size();

            assertThat(newSize - oldSize).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_Update_Method {

        @Nested
        @DisplayName("등록된 task가 없을 때,")
        class Context_When_None_Tasks_Registered {

            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다.")
            void it_throws_a_TaskNotFoundException() {
                final Long id = 1L;
                final Task task = TaskGenerator.generateTaskWithRandomTitle();

                assertThatThrownBy(() -> taskService.updateTask(id, task))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
            }
        }

        @Nested
        @DisplayName("매핑된 task가 없는 id를 인자로 호출하면")
        class Context_With_Id_Tasks_Mapped_To_Which_Is_None {

            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다.")
            void it_throws_a_TaskNotFoundException() {
                addOneTask();
                final Long id = Long.MAX_VALUE;
                final Task task = TaskGenerator.generateTaskWithRandomTitle();

                assertThatThrownBy(() -> taskService.updateTask(id, task))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
            }
        }

        @Nested
        @DisplayName("매핑된 task가 있는 id를 인자로 호출하면")
        class Context_With_Id_Tasks_Mapped_To_Which_Exists {

            @Test
            @DisplayName("id에 매핑된 task의 title이 변경된다.")
            void it_changes_title_of_the_task_mapped_to_id() {
                addOneTask();
                final Task addedTask = taskService.getTasks().get(0);
                final String oldTitle = addedTask.getTitle();

                final Long id = addedTask.getId();
                Task src = TaskGenerator.generateTaskWithRandomTitle();

                while (addedTask.getTitle().equals(src.getTitle())) {
                    src = TaskGenerator.generateTaskWithRandomTitle();
                }

                final String newTitle = taskService.updateTask(id, src).getTitle();

                assertThat(newTitle).isNotEqualTo(oldTitle);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_Delete_Method {

        @Nested
        @DisplayName("등록된 task가 없을 때,")
        class Context_When_None_Tasks_Registered {

            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다.")
            void it_throws_a_TaskNotFoundException() {
                final Long id = 1L;

                assertThatThrownBy(() -> taskService.deleteTask(id))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
            }
        }

        @Nested
        @DisplayName("매핑된 task가 없는 id를 인자로 호출하면")
        class Context_With_Id_Tasks_Mapped_To_Which_Is_None {

            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다.")
            void it_throws_a_TaskNotFoundException() {
                addOneTask();
                final Long id = Long.MAX_VALUE;

                assertThatThrownBy(() -> taskService.deleteTask(id))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
            }
        }

        @Nested
        @DisplayName("매핑된 task가 있는 id를 인자로 호출하면")
        class Context_With_Id_Tasks_Mapped_To_Which_Exists {

            @Test
            @DisplayName("id에 매핑된 task 삭제된다..")
            void it_deletes_the_task_mapped_to_id() {
                addOneTask();
                final Task addedTask = taskService.getTasks().get(0);
                final Long id = addedTask.getId();

                taskService.deleteTask(id);

                assertThatThrownBy(() -> taskService.deleteTask(id))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
            }

            @Test
            @DisplayName("등록된 task의 개수가 1만큼 줄어든다.")
            void it_decrements_the_number_of_tasks_registered_by_one() {
                addOneTask();
                final int oldSize = taskService.getTasks().size();

                final Long id = taskService.getTasks().get(0).getId();
                taskService.deleteTask(id);

                final int newSize = taskService.getTasks().size();

                assertThat(newSize - oldSize).isEqualTo(-1);
            }
        }
    }
}
