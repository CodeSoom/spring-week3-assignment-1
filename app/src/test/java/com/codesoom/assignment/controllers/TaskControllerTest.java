package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskControllerTest {
    abstract class Context_didCreateTwoTasks {
        @BeforeEach
        void context() {
            final Task task1 = new Task();
            task1.setTitle(FIXTURE_TITLE + 1);
            final Task task2 = new Task();
            task2.setTitle(FIXTURE_TITLE + 2);
            controller.create(task1);
            controller.create(task2);
        }
    }

    public static final String FIXTURE_TITLE = "title";
    private TaskController controller;

    @BeforeEach
    void setup() {
        controller = new TaskController(new TaskService());
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("생성되어 있는 할 일이 없다면")
        class Context_didNotCreateTask {
            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returnsEmptyList() {
                List<Task> result = controller.list();

                assertThat(result).isEmpty();
            }
        }

        @Nested
        @DisplayName("할일을 생성했었을 때")
        class Context_didCreateContext extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("생성된 할일 목록을 반환한다")
            void it_returnsTasks() {
                List<Task> result = controller.list();

                assertThat(result).isNotEmpty();
                assertThat(result.get(0).getTitle()).isEqualTo(FIXTURE_TITLE + 1);
                assertThat(result.get(0).getId()).isEqualTo(1L);
                assertThat(result.get(1).getTitle()).isEqualTo(FIXTURE_TITLE + 2);
                assertThat(result.get(1).getId()).isEqualTo(2L);
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("찾을 수 없는 id로 조회했을 때")
        class Context_withNotFindableTaskId {
            @Test
            @DisplayName("할일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFound() {
                assertThrows(TaskNotFoundException.class, () -> {
                    controller.detail(0L);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id로 조회했을 떄")
        class Context_didCreateContext extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("조회한 할일을 반환한다")
            void it_returnsTasks() {
                Task result = controller.detail(1L);

                assertThat(result.getId()).isEqualTo(1L);
                assertThat(result.getTitle()).isEqualTo(FIXTURE_TITLE + 1);
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        @Nested
        @DisplayName("찾을 수 없는 id로 요청했을 때")
        class Context_withNotFindableTaskId {
            @Test
            @DisplayName("할일을 찾을 수 없다는 에러를 던진다")
            void it_throwsTaskNotFoundException() {
                Task newTask = new Task();
                newTask.setTitle(FIXTURE_TITLE);
                assertThrows(TaskNotFoundException.class, () -> {
                    controller.update(0L, newTask);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id로 요청했을 때")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("수정된 할일을 반환한다")
            void it_returnsUpdatedTask() {
                Task newTask = new Task();
                newTask.setTitle(FIXTURE_TITLE + 1);
                Task updatedTask = controller.update(1L, newTask);

                assertThat(updatedTask.getId()).isEqualTo(1L);
                assertThat(updatedTask.getTitle()).isEqualTo(FIXTURE_TITLE + 1);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @Nested
        @DisplayName("찾을 수 없는 할일 id로 요청했을 때")
        class Context_withNotFindableTaskId {
            @Test
            @DisplayName("할일을 찾을 수 없다는 에러를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    controller.delete(0L);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id로 요청했을 때")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("요청된 할일을 삭제한다")
            void it_deletesTask() {
                controller.delete(1L);

                assertThrows(TaskNotFoundException.class, () -> {
                    controller.detail(1L);
                });
            }
        }
    }
}
