package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    final String givenTitle = "sample";
    final long givenID = 1L;

    TaskService subject(String... titles) {
        TaskService taskService = new TaskService();

        for (String title : titles) {
            Task task = new Task();
            task.setTitle(title);

            taskService.createTask(task);
        }

        return taskService;
    }

    @Nested
    @DisplayName("getTasks 메서드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("tasks 가 없을 때")
        class Context_without_tasks {

            @Test
            @DisplayName("비어 있는 집합을 리턴한다.")
            void It_returns_empty_ArrayList() {
                TaskService subject = subject();

                assertThat(subject.getTasks()).isEmpty();
            }
        }

        @Nested
        @DisplayName("tasks 가 있을 때")
        class Context_with_tasks {

            @Test
            @DisplayName("tasks 가 들어있는 집합을 리턴한다.")
            void It_returns_empty_ArrayList() {
                TaskService subject = subject(givenTitle);

                assertThat(subject.getTasks()).isNotEmpty();
                assertThat(subject.getTasks()).hasSize(1);
                assertThat(subject.getTasks())
                        .first()
                        .hasFieldOrPropertyWithValue("title", givenTitle);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {
        @Nested
        @DisplayName("대상 id 가 없을 때")
        class Context_not_exists_target_id {

            @Test
            @DisplayName("id를 찾을 수 없다는 예외를 던진다.")
            void It_throws_TaskNotFoundException() {
                TaskService subject = subject();

                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> subject.getTask(givenID));
            }
        }

        @Nested
        @DisplayName("대상 id 가 있을 때")
        class Context_exists_target_id {

            @Test
            @DisplayName("task 를 리턴한다.")
            void It_returns_task() {
                TaskService subject = subject(givenTitle);

                assertThat(subject.getTask(givenID))
                        .hasFieldOrPropertyWithValue("title", givenTitle);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {

        @Test
        @DisplayName("생성된 task 를 리턴한다.")
        void It_returns_created_task() {
            TaskService subject = subject();

            Task source = new Task();
            source.setTitle(givenTitle);

            assertThat(subject.createTask(source))
                    .hasFieldOrPropertyWithValue("title", givenTitle)
                    .hasFieldOrPropertyWithValue("id", givenID);
        }
    }

    @Nested
    @DisplayName("updateTask 메서드는")
    class Describe_updateTask {

        @Nested
        @DisplayName("대상 id 가 없을 때")
        class Context_not_exists_target_id {

            @Test
            @DisplayName("id를 찾을 수 없다는 예외를 던진다.")
            void It_throws_TaskNotFoundException() {
                TaskService subject = subject();
                Task task = new Task();

                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> subject.updateTask(givenID, task));
            }
        }

        @Nested
        @DisplayName("대상 id 가 있을 때")
        class Context_exists_target_id {

            @Test
            @DisplayName("변경된 task 를 리턴한다.")
            void It_returns_modified_task() {
                TaskService subject = subject(givenTitle);
                Task task = new Task();

                assertThat(subject.updateTask(givenID, task))
                        .hasFieldOrPropertyWithValue("title", null)
                        .hasFieldOrPropertyWithValue("id", givenID);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메서드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("대상 id 가 없을 때")
        class Context_not_exists_target_id {

            @Test
            @DisplayName("id를 찾을 수 없다는 예외를 던진다.")
            void It_throws_TaskNotFoundException() {
                TaskService subject = subject();

                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> subject.deleteTask(givenID));
            }
        }

        @Nested
        @DisplayName("대상 id 가 있을 때")
        class Context_exists_target_id {

            @Test
            @DisplayName("삭제된 task 를 리턴한다.")
            void It_returns_modified_task() {
                TaskService subject = subject(givenTitle);

                assertThat(subject.deleteTask(givenID))
                        .hasFieldOrPropertyWithValue("title", givenTitle)
                        .hasFieldOrPropertyWithValue("id", givenID);
            }
        }
    }
}
