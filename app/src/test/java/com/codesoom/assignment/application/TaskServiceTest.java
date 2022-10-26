package com.codesoom.assignment.application;

import com.codesoom.assignment.RandomTitleGenerator;
import com.codesoom.assignment.exceptions.NoneTaskRegisteredException;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
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
        final Task task = new Task(null, RandomTitleGenerator.getRandomTitle());
        taskService.createTask(task);
    }

    private void addNumberOfTasks(int number) {
        for (int i = 0; i < number; i++) {
            addOneTask();
        }
    }

    private int getRandomNumberBetweenOneAndOneHundred() {
        return new Random().nextInt(101);
    }

    private Long getRandomNotNegativeId() {
        long id = new Random().nextLong();
        return id < 0 ? -id : id;
    }

    private Long getIdHavingMappedTask() {
        final Collection<Task> taskCollection = taskService.getTasks();

        for (Task task : taskCollection) {
            return task.getId();
        }

        throw new NoneTaskRegisteredException();
    }

    private Long getIdNotHavingMappedTask() {
        return Long.MAX_VALUE;
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

            private int number;

            @BeforeAll
            void setUp() {
                number = getRandomNumberBetweenOneAndOneHundred();
                addNumberOfTasks(number);
            }

            @Test
            @DisplayName("비어 있지 않은 리스트를 반환한다.")
            void it_returns_a_not_empty_list() {
                assertThat(taskService.getTasks()).isNotEmpty();
            }

            @Test
            @DisplayName("등록한 task의 개수와 동일한 사이즈를 가지는 컬렉션을 리턴한다.")
            void it_returns_a_list_with_length_equals_to_tasks_registered() {
                final Collection<Task> taskCollection = taskService.getTasks();
                assertThat(taskCollection.size()).isEqualTo(number);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_GetTask_Method {

        @Nested
        @DisplayName("등록된 task가 없을 때,")
        class Context_When_None_Tasks_Registered {

            @Nested
            @DisplayName("임의의 0 또는 양수의 id를 인자로 호출하면")
            class Context_With_Temporary_Not_Negative_Id {

                private final Long temporaryNotNegativeId = getRandomNotNegativeId();

                @Test
                @DisplayName("TaskNotFoundException 예외를 발생시킨다.")
                void it_throws_a_TaskNotFoundException() {
                    assertThatThrownBy(() -> taskService.getTask(temporaryNotNegativeId))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + temporaryNotNegativeId);
                }
            }
        }

        @Nested
        @DisplayName("등록된 task가 있을 때,")
        class Context_When_Tasks_Registered_Exists {

            @BeforeAll
            void setUp() {
                int number = getRandomNumberBetweenOneAndOneHundred();
                addNumberOfTasks(number);
            }

            @Nested
            @DisplayName("매핑된 task가 있는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Exists {

                private final Long idHavingMappedTask = getIdHavingMappedTask();

                @Test
                @DisplayName("id에 매핑된 task를 반환한다.")
                void it_returns_a_task_mapped_to_id() {
                    final Task task = taskService.getTask(idHavingMappedTask);

                    assertThat(task.getId()).isEqualTo(idHavingMappedTask);
                }
            }

            @Nested
            @DisplayName("매핑된 task가 없는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Is_None {

                private final Long idNotHavingMappedTask = Long.MAX_VALUE;

                @Test
                @DisplayName("TaskNotFoundException를 발생시킨다.")
                void it_throws_a_TaskNotFoundException() {
                    assertThatThrownBy(() -> taskService.getTask(idNotHavingMappedTask))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + idNotHavingMappedTask);
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
            final Task src = new Task(null, RandomTitleGenerator.getRandomTitle());
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
    @DisplayName("updateTask 메소드는")
    class Describe_Update_Method {

        @Nested
        @DisplayName("등록된 task가 없을 때,")
        class Context_When_None_Tasks_Registered {

            @Nested
            @DisplayName("음수가 아닌 id를 인자로 호출하면")
            class Context_With_Not_Negative_Id {

                private final Long temporaryNotNegativeId = getRandomNotNegativeId();

                @Test
                @DisplayName("TaskNotFoundException을 발생시킨다.")
                void it_throws_a_TaskNotFoundException() {
                    final Task task = new Task(null, RandomTitleGenerator.getRandomTitle());

                    assertThatThrownBy(() -> taskService.updateTask(temporaryNotNegativeId, task))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + temporaryNotNegativeId);
                }
            }
        }

        @Nested
        @DisplayName("등록된 task가 있을 때")
        class Context_With_Tasks_Registered_Exists {

            @BeforeAll
            void setUp() {
                int number = getRandomNumberBetweenOneAndOneHundred();
                addNumberOfTasks(number);
            }

            @Nested
            @DisplayName("매핑된 task가 없는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Is_None {

                private final Long idNotHavingMappedTask = getIdNotHavingMappedTask();

                @Test
                @DisplayName("TaskNotFoundException을 발생시킨다.")
                void it_throws_a_TaskNotFoundException() {
                    final Task task = new Task(null, RandomTitleGenerator.getRandomTitle());

                    assertThatThrownBy(() -> taskService.updateTask(idNotHavingMappedTask, task))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + idNotHavingMappedTask);
                }
            }

            @Nested
            @DisplayName("매핑된 task가 있는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Exists {

                private final Long idHavingMappedTask = getIdHavingMappedTask();

                @Test
                @DisplayName("id에 매핑된 task의 title이 변경된다.")
                void it_changes_title_of_the_task_mapped_to_id() {
                    final Long id = idHavingMappedTask;

                    final String oldTitle = taskService.getTask(id).getTitle();

                    final Task src = new Task(null, getRandomTitleDifferentFrom(oldTitle));

                    final String newTitle = taskService.updateTask(id, src).getTitle();

                    assertThat(newTitle).isNotEqualTo(oldTitle);
                }

                private String getRandomTitleDifferentFrom(String s) {
                    String title;

                    do {
                        title = RandomTitleGenerator.getRandomTitle();
                    } while (title.equals(s));

                    return title;
                }
            }

        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_Delete_Method {

        @Nested
        @DisplayName("등록된 task가 없을 때,")
        class Context_When_None_Tasks_Registered {

            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다.")
            void it_throws_a_TaskNotFoundException() {
                final Long id = getRandomNotNegativeId();

                assertThatThrownBy(() -> taskService.deleteTask(id))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
            }
        }

        @Nested
        @DisplayName("등록된 task가 있을 때,")
        class Context_When_Tasks_Registered_Exists {

            @BeforeAll
            void setUp() {
                final int number = getRandomNumberBetweenOneAndOneHundred();
                addNumberOfTasks(number);
            }

            @Nested
            @DisplayName("매핑된 task가 없는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Is_None {

                private final Long idNotHavingMappedTask = getIdNotHavingMappedTask();

                @Test
                @DisplayName("TaskNotFoundException을 발생시킨다.")
                void it_throws_a_TaskNotFoundException() {
                    assertThatThrownBy(() -> taskService.deleteTask(idNotHavingMappedTask))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + idNotHavingMappedTask );
                }
            }

            @Nested
            @DisplayName("매핑된 task가 있는 id를 인자로 호출하면")
            class Context_With_Id_Tasks_Mapped_To_Which_Exists {

                private final Long idHavingMappedTask = getIdHavingMappedTask();

                @Test
                @DisplayName("id에 매핑된 task 삭제된다..")
                void it_deletes_the_task_mapped_to_id() {
                    final Long id = idHavingMappedTask;

                    assertThatNoException().isThrownBy(() -> taskService.getTask(id));

                    taskService.deleteTask(id);

                    assertThatThrownBy(() -> taskService.getTask(id))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessage(TASK_NOT_FOUND_EXCEPTION_MESSAGE_PREFIX + id);
                }

                @Test
                @DisplayName("등록된 task의 개수가 1만큼 줄어든다.")
                void it_decrements_the_number_of_tasks_registered_by_one() {
                    final int oldSize = taskService.getTasks().size();

                    taskService.deleteTask(idHavingMappedTask);

                    final int newSize = taskService.getTasks().size();

                    assertThat(oldSize - newSize).isEqualTo(1);
                }
            }
        }
    }
}
