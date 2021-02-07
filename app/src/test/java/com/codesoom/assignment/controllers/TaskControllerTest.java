package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 클래스")
class TaskControllerTest {
    final long givenID = 1L;
    final String givenTitle = "sample";
    final String givenModifyTitle = "modify sample";

    final TaskController subject(String... titles) {
        TaskService taskService = new TaskService();

        for (String title : titles) {
            Task task = new Task();
            task.setTitle(title);

            taskService.createTask(task);
        }

        return new TaskController(taskService);
    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {

        @Nested
        @DisplayName("등록된 Task 가 하나도 없을 때")
        class Context_without_task {

            @Test
            @DisplayName("빈 집합을 리턴한다.")
            void It_returns_void_array() {
                TaskController subject = subject();

                assertThat(subject.list()).hasSize(0);
            }
        }

        @Nested
        @DisplayName("등록된 Task 가 있을 때")
        class Context_with_task {

            @Test
            @DisplayName("등록된 Task 를 리턴한다.")
            void It_returns_exists_array() {
                TaskController subject = subject(givenTitle);

                List<Task> actual = subject.list();

                assertThat(actual).hasSize(1);
                assertThat(actual.get(0).getId()).isEqualTo(givenID);
                assertThat(actual.get(0).getTitle()).isEqualTo(givenTitle);
            }
        }
    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {

        @Nested
        @DisplayName("찾는 id가 없을 때")
        class Context_without_target_id {

            @Test
            @DisplayName("Task 가 존재하지 않는다는 예외를 던진다.")
            void It_throws_task_not_found_exception() {
                TaskController subject = subject();

                assertThatThrownBy(() -> subject.detail(givenID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("찾는 id가 있을 때")
        class Context_with_target_id {

            @Test
            @DisplayName("Task 를 리턴한다.")
            void It_returns_task() {
                TaskController subject = subject(givenTitle);

                Task actual = subject.detail(givenID);

                assertThat(actual.getId()).isEqualTo(givenID);
                assertThat(actual.getTitle()).isEqualTo(givenTitle);
            }
        }
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        private Task source() {
            Task source = new Task();
            source.setTitle(givenTitle);
            return source;
        }

        @Test
        @DisplayName("생성된 Task 를 리턴한다.")
        void It_returns_created_task() {
            TaskController subject = subject();
            Task source = source();

            Task actual = subject.create(source);

            assertThat(actual.getId()).isEqualTo(givenID);
            assertThat(actual.getTitle()).isEqualTo(givenTitle);
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {

        private Task source() {
            Task source = new Task();
            source.setTitle(givenModifyTitle);
            return source;
        }

        @Nested
        @DisplayName("주어진 id가 없을 때")
        class Context_without_target_id {

            @Test
            @DisplayName("Task 를 찾을 수 없다는 예외를 던진다.")
            void It_throws_task_not_found_exception() {
                TaskController subject = subject();
                Task source = source();

                assertThatThrownBy(() -> subject.update(givenID, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("주어진 id가 있을 때")
        class Context_with_target_id {

            @Test
            @DisplayName("변경된 Task 를 리턴한다.")
            void It_returns_modified_task() {
                TaskController subject = subject(givenTitle);
                Task source = source();

                Task actual = subject.update(givenID, source);

                assertThat(actual.getId()).isEqualTo(givenID);
                assertThat(actual.getTitle()).isEqualTo(givenModifyTitle);
            }
        }
    }

    @Nested
    @DisplayName("patch 메서드는")
    class Describe_patch {

        private Task source() {
            Task source = new Task();
            source.setTitle(givenModifyTitle);
            return source;
        }

        @Nested
        @DisplayName("주어진 id가 없을 때")
        class Context_without_target_id {

            @Test
            @DisplayName("Task 를 찾을 수 없다는 예외를 던진다.")
            void It_throws_task_not_found_exception() {
                TaskController subject = subject();
                Task source = source();

                assertThatThrownBy(() -> subject.patch(givenID, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("주어진 id가 있을 때")
        class Context_with_target_id {

            @Test
            @DisplayName("변경된 Task 를 리턴한다.")
            void It_returns_modified_task() {
                TaskController subject = subject(givenTitle);
                Task source = source();

                Task actual = subject.patch(givenID, source);

                assertThat(actual.getId()).isEqualTo(givenID);
                assertThat(actual.getTitle()).isEqualTo(givenModifyTitle);
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("주어진 id가 없을 때")
        class Context_without_target_id {

            @Test
            @DisplayName("Task 를 찾을 수 없다는 예외를 던진다.")
            void It_throws_task_not_found_exception() {
                TaskController subject = subject();

                assertThatThrownBy(() -> subject.delete(givenID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("주어진 id가 있을 때")
        class Context_with_target_id {

            @Test
            @DisplayName("삭제 후 주어진 id를 조회하면 Task 를 찾을 수 없다는 예외를 던진다.")
            void It_returns_modified_task() {
                TaskController subject = subject(givenTitle);

                subject.delete(givenID);

                assertThatThrownBy(() -> subject.detail(givenID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
