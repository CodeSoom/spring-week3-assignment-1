package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스의")
class TaskServiceTest {
    private TaskService taskService;
    private final Long givenId = 0L;
    private final String givenTitle = "BJP";

    @BeforeEach
    void setUp() {
        taskService = new TaskService(new TaskRepository());
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_get_task {
        @Nested
        @DisplayName("주어진 식별자를 가진 작업이 존재하면")
        class Context_with_id {
            @Test
            @DisplayName("해당 작업을 리턴한다")
            void it_returns_task_with_id() {
                taskService.createTask(givenTitle);

                assertThat(taskService.getTask(0L)).isEqualTo(new Task(givenId, givenTitle));
            }
        }

        @Nested
        @DisplayName("주어진 식별자를 가진 작업이 없으면")
        class Context_without_id {
            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> taskService.getTask(givenId))
                        .isExactlyInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_get_tasks {
        @Nested
        @DisplayName("작업을 가진 목록이 주어지면")
        class Context_with_tasks {
            @Test
            @DisplayName("작업 목록을 리턴한다")
            void It_returns_tasks() {
                taskService.createTask(givenTitle);
                taskService.createTask(givenTitle);

                assertThat(taskService.getTasks().size()).isEqualTo(2);
            }
        }

        @Nested
        @DisplayName("빈 작업 목록이 주어지면")
        class Context_with_empty_tasks {
            @Test
            @DisplayName("빈 배열을 리턴한다")
            void It_returns_empty_array() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_create_task {
        @Nested
        @DisplayName("제목이 주어지면")
        class Context_with_title {
            final String givenTitle = "주어진 제목";

            @Test
            @DisplayName("주어진 제목을 가진 작업을 생성하고 리턴한다.")
            void It_returns_task_with_id() {
                assertThat(taskService.createTask(givenTitle)).isEqualTo(new Task(0L, givenTitle));
                assertThat(taskService.createTask(givenTitle)).isEqualTo(new Task(1L, givenTitle));
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_update_task {
        @Nested
        @DisplayName("제목, 식별자가 주어지고 주어진 식별자와 같은 식별자를 가진 작업이 존재할 때")
        class Context_with_id_and_title_and_exist_task {
            final String givenTitleToChange = "변경된 제목";

            @Test
            @DisplayName("해당 작업의 제목을 변경하고 리턴한다")
            void It_change_title_and_return() {
                taskService.createTask(givenTitle);

                assertThat(taskService.updateTask(givenId, givenTitleToChange))
                        .isEqualTo(new Task(givenId, givenTitleToChange));
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_delete_task {
        @Nested
        @DisplayName("주어진 식별자와 같은 식별자를 가진 작업이 있다면")
        class Context_with_task_and_id {
            @Test
            @DisplayName("해당 작업을 제거하고 제거한 작업을 리턴한다")
            void It_remove_task() {
                taskService.createTask(givenTitle);

                assertThat(taskService.deleteTask(givenId)).isEqualTo(new Task(givenId, givenTitle));
            }
        }
    }
}
