package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    final String givenTitle = "sample";
    final String givenTitle2 = "sample2";
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
        @DisplayName("등록된 task 가 없을 때")
        class Context_without_tasks {
            TaskService subject = subject();

            @Test
            @DisplayName("비어 있는 집합을 리턴한다.")
            void It_returns_empty_ArrayList() {
                assertThat(subject.getTasks()).isEmpty();
            }
        }

        @Nested
        @DisplayName("등록된 task 가 있을 때")
        class Context_with_tasks {
            TaskService subject = subject(givenTitle, givenTitle2);

            @Test
            @DisplayName("tasks 가 들어있는 집합을 리턴한다.")
            void It_returns_empty_ArrayList() {
                assertThat(subject.getTasks()).hasSize(2);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {
        @Nested
        @DisplayName("주어진 id 가 없을 때")
        class Context_not_exists_target_id {
            TaskService subject = subject();

            @Test
            @DisplayName("id를 찾을 수 없다는 예외를 던진다.")
            void It_throws_TaskNotFoundException() {
                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> subject.getTask(givenID));
            }
        }

        @Nested
        @DisplayName("주어진 id 가 있을 때")
        class Context_exists_target_id {
            TaskService subject = subject(givenTitle);
            Task expect = new Task(givenID, givenTitle);

            @Test
            @DisplayName("task 를 리턴한다.")
            void It_returns_task() {
                assertThat(subject.getTask(givenID)).isEqualTo(expect);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {
        TaskService subject = subject();
        Task source = new Task(givenID, givenTitle);

        @Test
        @DisplayName("생성된 task 를 리턴한다.")
        void It_returns_created_task() {
            Task actual = subject.createTask(source);

            assertThat(actual.getTitle()).isEqualTo(source.getTitle());
        }
    }

    @Nested
    @DisplayName("updateTask 메서드는")
    class Describe_updateTask {

        @Nested
        @DisplayName("주어진 id 가 없을 때")
        class Context_not_exists_target_id {
            TaskService subject = subject();

            @Test
            @DisplayName("id를 찾을 수 없다는 예외를 던진다.")
            void It_throws_TaskNotFoundException() {
                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> subject.updateTask(givenID, new Task()));
            }
        }

        @Nested
        @DisplayName("주어진 id 가 있을 때")
        class Context_exists_target_id {
            Task source = new Task(givenID, givenTitle);
            TaskService subject = subject(givenTitle);

            @Test
            @DisplayName("변경된 task 를 리턴한다.")
            void It_returns_modified_task() {
                assertThat(subject.updateTask(givenID, source)).isEqualTo(source);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메서드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("주어진 id 가 없을 때")
        class Context_not_exists_target_id {
            TaskService subject = subject();

            @Test
            @DisplayName("id를 찾을 수 없다는 예외를 던진다.")
            void It_throws_TaskNotFoundException() {
                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> subject.deleteTask(givenID));
            }
        }

        @Nested
        @DisplayName("주어진 id 가 있을 때")
        class Context_exists_target_id {
            TaskService subject = subject(givenTitle);
            Task expect = new Task(givenID, givenTitle);

            @Test
            @DisplayName("삭제된 task 를 리턴한다.")
            void It_returns_modified_task() {
                assertThat(subject.deleteTask(givenID)).isEqualTo(expect);
            }
        }
    }
}
